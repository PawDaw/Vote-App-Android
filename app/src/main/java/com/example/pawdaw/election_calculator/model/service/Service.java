package com.example.pawdaw.election_calculator.model.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.ListView;

import com.example.pawdaw.election_calculator.model.mainClasses.Candidate;
import com.example.pawdaw.election_calculator.model.mainClasses.CandidatesAdapter;
import com.example.pawdaw.election_calculator.model.mainClasses.Party;
import com.example.pawdaw.election_calculator.model.storage.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by pawdaw on 04/03/17.
 */

public class Service {

    private StringBuilder stringOfVotedPesel;
    private ProgressDialog dialog;
    private StringBuilder stringOfBlockedPesels;
    private Log log;



    // Shared Preferences, KEY names
    public static final String BLOCKED_PESEL_KEY = "blockedPesel";
    public static final String INVALID_VOTE_KEY = "invalidVote";
    public static final String VOTED_PERSONS_PESEL_KEY = "votedPresonsPesel";
    public static final String PESEL_KEY = "pesel";

    // JSON FILE NAME
    private static final String MY_JSON_FILE = "JSON.js";



    // --------- Singleton instance  ----------------

    private static Service instance = new Service();

    public static Service getInstance() {

        return instance;
    }

    private Service() {
        //
    }


    //  ------ saving Data to Shared Preferences ------

    public void passBlockedPeselsToArray(SharedPreferences sharedPreferencesWRITE){

        String wordString = sharedPreferencesWRITE.getString(BLOCKED_PESEL_KEY,"empty");
        String[] items = wordString.split(",");

        for(String s:items){
            Storage.getInstance().setBlockedPESELS(s);
        }


    }

    public void retrieveBlockedPeselsFromSharedP(SharedPreferences sharedPreferencesREAD){

        String wordString = sharedPreferencesREAD.getString(BLOCKED_PESEL_KEY,"empty");

        if(!wordString.equals("empty")){

            String[] items = wordString.split(",");

            for(String s:items){
                Storage.getInstance().setBlockedPESELS(s);
            }
        }

    }


    public void addOneInvalidVote(SharedPreferences sharedPreferences){

        String amountOfInvalidVotes = sharedPreferences.getString(INVALID_VOTE_KEY, String.valueOf(0));

        // save stringOfBlockedPesels to SharedPreferences, the file MyStorage.xml will contains my all blockedPesels
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // increase amount of invalid votes
        int amount = Integer.parseInt(amountOfInvalidVotes) +1;
        editor.putString(INVALID_VOTE_KEY, String.valueOf(amount));

        log.e("info :"," number of invalid votes "+ amount);
        editor.apply();


    }



    public void retrieveVotedPersonsPeselFromSharedP(SharedPreferences sharedPreferencesREAD){

        String wordString = sharedPreferencesREAD.getString(VOTED_PERSONS_PESEL_KEY,"empty");

        if(!wordString.equals("empty")){

            String[] items = wordString.split(",");

            for(String s:items){
                Storage.getInstance().setVotedPersonsPesel(s);
            }
        }



    }

    // save current pesel to SharedPreferences, the file MyStorage.xml will contains PESEL number
    public void savePeselToSP(String pesel,SharedPreferences sharedPreferencesWRITE){

        SharedPreferences.Editor editor = sharedPreferencesWRITE.edit();
        editor.putString(PESEL_KEY, pesel);
        editor.apply();
    }

    // save the Pesel ARRAY located in STORAGE to Shared Preferences.
    private void passVotedPeselFromStorageArrayToSP(SharedPreferences sharedPreferencesWRITE){

        if(!Storage.getInstance().votedPersonsPesel.isEmpty()){

            stringOfVotedPesel = new StringBuilder();

            for (String s:Storage.getInstance().getVotedPersonsPesel()) {

                //pass all pesels to stringBuilder
                stringOfVotedPesel.append(s);
                stringOfVotedPesel.append(",");

            }

            // save stringOfBlockedPesels to SharedPreferences, the file MyStorage.xml will contains my all blockedPesels
            SharedPreferences.Editor editor = sharedPreferencesWRITE.edit();
            editor.putString(VOTED_PERSONS_PESEL_KEY, stringOfVotedPesel.toString());
            editor.apply();
        }



    }

    //  pass PESEL to Storage ARRAY.Pesel belongs to the person who current voted.
    public void passPeselToStorageArray(SharedPreferences sharedPreferencesREAD){

        String pesel = sharedPreferencesREAD.getString(PESEL_KEY,"empty");

        if(!pesel.equals("empty")){
            // pass PESEL to ARRAY.Pesel belongs to the person who voted.
            Storage.getInstance().setVotedPersonsPesel(pesel);

            // Save the Pesel ARRAY located in STORGE to Sharedpreferences.
            Service.getInstance().passVotedPeselFromStorageArrayToSP(sharedPreferencesREAD);
        }
    }



    // ------------ Internet Connection ---------


    // return TRUE if Connected
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

//    -------  Saving to file JSON --------------------


    // ----- Save JSON data to file  -----
    public void saveData(Context context) {

        File directory = new File(context.getFilesDir().getAbsolutePath() + File.separator);

        try {

            OutputStream os = new FileOutputStream(directory + File.separator + MY_JSON_FILE);

            //  method below to write JSON
            writeJsonStream(os,Storage.getInstance().getCandidates());

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.e("info :","............ JSON SAVED ........... ");

    }


    // ----- read JSON data from file  -----
    public void readFile(Context context) {

        File directory = new File(context.getFilesDir().getAbsolutePath() + File.separator);

        try {

            InputStream in = new FileInputStream(directory + File.separator + MY_JSON_FILE);

            //  method below to read JSON
            readJSONStream(in);

        } catch (IOException e) {
            e.printStackTrace();
        }

        log.e("info :","............ JSON READ ........... ");
    }



    //  --------- write JSON ---------

    public void writeJsonStream(OutputStream out, ArrayList<Candidate> candidates) throws IOException{

        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent(" ");

        //method below
        writeTravelArray(writer, candidates);

        writer.close();

    }

    public void writeTravelArray(JsonWriter writer, ArrayList<Candidate> candidates) throws IOException{

        // write array
        writer.beginArray();

        for(Candidate c: candidates){

            // method below
            writeTravel(writer,c);

        }
        writer.endArray();
    }

    public void writeTravel(JsonWriter writer, Candidate candidate) throws IOException{

        // write Object
        writer.beginObject();

        writer.name("name").value(candidate.getName());
        writer.name("party").value(candidate.getParty());
        writer.name("amountOfVotes").value(candidate.getAmountOfvotes());

        writer.endObject();
    }



    //  ---------- read JSON ------------

    public ArrayList<Candidate> readJSONStream (InputStream in) throws IOException{

        JsonReader reader = new JsonReader(new InputStreamReader(in,"UTF-8"));
        try{
            //method below
            return readTravelArray(reader);
        }finally {
            reader.close();
        }
    }

    private ArrayList<Candidate> readTravelArray(JsonReader reader) throws IOException {

        reader.beginArray();

        while (reader.hasNext()){
           Storage.getInstance().setCandidates(readTravel(reader));
        }
        reader.endArray();
        return Storage.getInstance().getCandidates();
    }
    // convert JSON object into a Candidate class instance
    private Candidate readTravel(JsonReader reader) throws IOException {

        String name = null;
        String party = null;
        String amountOfVotes = null;

        reader.beginObject();

        while (reader.hasNext()){

            String value = reader.nextName();

            if(value.equals("name")){
                name = reader.nextString();
            }else if(value.equals("party")){
                party = reader.nextString();
            }else if(value.equals("amountOfVotes")){
                amountOfVotes = reader.nextString();
            } else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Candidate(name,party,Integer.parseInt(amountOfVotes));
    }

    //   ------------ END read JSON ------------------




    // Method used for checkboxes in CandidateAdapter, go through in all items retrieved from Storage and compare based on Candidate name from current adapter
    public Boolean checkIfExist(String candidateName){

        boolean exist = false;

        for(Candidate o : Storage.getInstance().getChosenCandidateChecbox()){

            if(candidateName.equals(o.getName())){
                exist = true;
            }
        }

        return exist;
    }




    // Create new Array with Party with amount of votes
    public void createPartyArray(){

        for(Candidate c:Storage.getInstance().getCandidates()){
            Storage.getInstance().setHset(c.getParty());
        }

        for(String s:Storage.getInstance().getHset()){
            Party party = new Party(s);
            for(Candidate c2:Storage.getInstance().getCandidates()){
                if(party.getName().equals(c2.getParty())){
                    party.setAmountOfVotes(party.getAmountOfVotes()+c2.getAmountOfvotes());
                }
            }
            Storage.getInstance().setPartyWithVotes(party);
        }
    }



    // ------ ASYNC TASK Main Activity -----




    // ------- Get data from JSON Server, Launch Async TASK -------------

    public void getDataFromServerJSONBlocked(Context context,SharedPreferences sharedPreferences){

        dialog = new ProgressDialog(context);
        new JSON_AsyncTask_Main_Activity(dialog,sharedPreferences).execute();

    }


    // ----------------- CLASS  JSON Async TASK , Main Activity -------------------

    class JSON_AsyncTask_Main_Activity extends AsyncTask<String, String, JSONObject> {

        //URL to get JSON Array
        private  String url = "http://webtask.future-processing.com:8069/blocked";
        // http://luzik.comlu.com/android/blocked.xml

        private ProgressDialog pDialog;
        private SharedPreferences shared;

        JSON_AsyncTask_Main_Activity(ProgressDialog dialog,SharedPreferences sharedPreferences){
            shared=sharedPreferences;
            pDialog=dialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            pDialog.setMessage("loading Data ... ");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            // Class JSONParser connecting to the internet
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);


            return json;

        }
        @Override
        protected void onPostExecute(JSONObject json) {


            try {

                stringOfBlockedPesels = new StringBuilder();

                JSONObject jsonDisallowed = json.getJSONObject("disallowed");
                JSONArray pesels = jsonDisallowed.getJSONArray("person");

                //Iterate the jsonArray and print the info of JSONObjects
                for (int  i=0; i< pesels.length(); i++) {

                    // read pesel number
                    String pesel = pesels.getJSONObject(i).getString("pesel");

                    //pass all pesels to stringBuilder
                    stringOfBlockedPesels.append(pesel);
                    stringOfBlockedPesels.append(",");

                }


                // save stringOfBlockedPesels to SharedPreferences, the file MyStorage.xml will contains my all blockedPesels
                SharedPreferences.Editor editor = shared.edit();
                editor.putString(BLOCKED_PESEL_KEY, stringOfBlockedPesels.toString());
                editor.apply();

                //pass from sharedPreferences all blocked pesels to Array located on the Storage.
                Service.getInstance().passBlockedPeselsToArray(shared);
                log.e("info :","Blocked PESELS are updated " + Storage.getInstance().getBlockedPESELS());


                pDialog.dismiss();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }


    // ------ ASYNC TASK Candidates Activity -----



    // ------- Get data from JSON Server, Launch Async TASK -------------

    public void getDataFromServerJSONCandidatesUpdate(AsyncTaskCompleteListener callBack,Context context){

        dialog = new ProgressDialog(context);
        new JSON_AsyncTask_Candidates_Activity(callBack,context,dialog).execute();

    }

    public void getDataFromServerJSONCandidates(Context context){

        dialog = new ProgressDialog(context);
        new JSON_AsyncTask_Candidates_Activity(context,dialog).execute();

    }


    // ----------------- CLASS JSON Async TASK -------------------

    class JSON_AsyncTask_Candidates_Activity extends AsyncTask<String, String, JSONObject> {

        //URL to get JSON Array
        private  String url = "http://webtask.future-processing.com:8069/candidates";

        private ProgressDialog dialog;
        private Context context;


        private AsyncTaskCompleteListener callback;


        // Constructor for Candidates Activity UPDATE
        public JSON_AsyncTask_Candidates_Activity(AsyncTaskCompleteListener cCallBack,Context cContext,ProgressDialog cDialog){
            context = cContext;
            dialog = cDialog;
            callback = cCallBack;

        }


        // Constructor for Main Activity first load
        public JSON_AsyncTask_Candidates_Activity(Context cContext, ProgressDialog cDialog) {
            context = cContext;
            dialog = cDialog;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setMessage("Getting Data ... from JSON URL");
            dialog.setIndeterminate(false);
            //dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {

            // Class JSONParser connecting to the internet
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);


            return json;

        }
        @Override
        protected void onPostExecute(JSONObject json) {

            try {


                JSONObject jsonCandidates = json.getJSONObject("candidates");
                JSONArray persons = jsonCandidates.getJSONArray("candidate");


                for (int  i=0; i< persons.length(); i++) {

                    // read pesel number
                    String name = persons.getJSONObject(i).getString("name");
                    String party = persons.getJSONObject(i).getString("party");

                    Candidate candidate = new Candidate(name,party,0);
                    Storage.getInstance().setCandidates(candidate);

//                    log.e("info :","candidates: " + Storage.getInstance().getCandidates() );
                }

                dialog.dismiss();

                if (callback != null){
                    callback.onTaskComplete();

                }



                Service.getInstance().saveData(context);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }



}
