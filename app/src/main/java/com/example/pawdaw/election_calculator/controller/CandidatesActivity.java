package com.example.pawdaw.election_calculator.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pawdaw.election_calculator.model.mainClasses.CandidatesAdapter;
import com.example.pawdaw.election_calculator.R;
import com.example.pawdaw.election_calculator.model.mainClasses.Candidate;
import com.example.pawdaw.election_calculator.model.service.AsyncTaskCompleteListener;
import com.example.pawdaw.election_calculator.model.service.JSONParser;
import com.example.pawdaw.election_calculator.model.mainClasses.Party;
import com.example.pawdaw.election_calculator.model.service.Service;
import com.example.pawdaw.election_calculator.model.storage.Storage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by pawdaw on 04/03/17.
 */

public class CandidatesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,AsyncTaskCompleteListener {


    private ListView listview;
    private CandidatesAdapter candidatesAdapter;
    private Log log = null;
    private AlertDialog dialog;  // instance variable
    public SharedPreferences sharedPreferences;

    // Shard Preferences, FILE NAME
    private static final String MY_PREFERENCES_FILE = "MyStorage";

    // JSON FILE NAME
    private static final String MY_JSON_FILE = "JSON.js";


    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.candidates_list);
        listview = (ListView) findViewById(R.id.listView);

        // swipes down the view to REFRESH
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);


        // required for Shared Precedences in the Service
        sharedPreferences = getSharedPreferences(MY_PREFERENCES_FILE,0);

        //  check if JSON file exist if not load data form the server and create the JSON file
        File directory = new File(CandidatesActivity.this.getFilesDir().getAbsolutePath()+ File.separator + MY_JSON_FILE);

        if(directory.exists()){

             // Read from the JSON file, when Candidates array is empty
            if(Storage.getInstance().getCandidates().isEmpty()){
                Service.getInstance().readFile(this);
                log.e("info :","............ File loaded from DATABASE ...........  " + Storage.getInstance().getCandidates());

            }

        }else {

            if (Service.getInstance().isNetworkAvailable(this)){


                // AsyncTask From Service
                Service.getInstance().getDataFromServerJSONCandidatesUpdate(this,this);
                log.e("info :","............ File saved ...........  " );

            }else{
                Toast.makeText(getApplicationContext(), "You don't have an internet connection", Toast.LENGTH_SHORT).show();
            }


        }


        candidatesAdapter = new CandidatesAdapter(this, Storage.getInstance().getCandidates());
        listview.setAdapter(candidatesAdapter);
        candidatesAdapter.notifyDataSetChanged();


    };

    // is triggered whenever user swipes down the view.
    @Override
    public void onRefresh() {

        if (Service.getInstance().isNetworkAvailable(this)){
            Service.getInstance().getDataFromServerJSONCandidatesUpdate(this,this);

        }
        else{
            // stopping swipe refresh
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(), "To refresh turn on an internet connection", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onTaskComplete() {

        candidatesAdapter = new CandidatesAdapter(this, Storage.getInstance().getCandidates());
        listview.setAdapter(candidatesAdapter);
        candidatesAdapter.notifyDataSetChanged();

        // stopping swipe refresh
        swipeRefreshLayout.setRefreshing(false);

    }



    // On Click Button
    public void voteButton(View view) {


        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("Confirm ");

        if(Storage.getInstance().getChosenCandidateChecbox().isEmpty()){
            alert.setMessage("Can you confirm that you will not vote for anyone. "+" Your vote will be invalid ");
        }else if(Storage.getInstance().getChosenCandidateChecbox().size()>1){
            alert.setMessage("Are you sure to vote on : " +Storage.getInstance().getChosenCandidateChecbox().size() +" candidates ? " +" Your vote will be invalid. ");
        }else {
            alert.setMessage("Are you sure to vote on : " +Storage.getInstance().getChosenCandidateChecbox() +" ? ");
        }

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // VOTE OK
                // The user has chosen one candidate, the user vote is valid
                if(Storage.getInstance().getChosenCandidateChecbox().size()==1){
                    for(Candidate c:Storage.getInstance().getChosenCandidateChecbox()){
                        for(Candidate c2:Storage.getInstance().getCandidates()){
                            if(c.getName().equals(c2.getName())){
                                c2.setAmountOfvotes(c2.getAmountOfvotes()+1);
                            }
                        }
                    }
                    Service.getInstance().saveData(CandidatesActivity.this);
                    Toast.makeText(getApplicationContext(), "Thanks for your vote ;)", Toast.LENGTH_SHORT).show();

                    // update the party array with amount of votes
                    Service.getInstance().createPartyArray();

                    for(Party p:Storage.getInstance().getPartyWithVotes()){
                        log.e("info :"," Party " + p.getName() +" votes : "+ p.getAmountOfVotes());
                    }

                // INVALID VOTE
                }else{

                    // The user has chosen more than one candidate or not vote for anyone, the user vote is invalid
                    Service.getInstance().addOneInvalidVote(sharedPreferences);
                    // update the party array with amount of votes
                    Service.getInstance().createPartyArray();
                }


                //  pass PESEL to Storage ARRAY.Pesel belongs to the person who current voted.
                Service.getInstance().passPeselToStorageArray(sharedPreferences);

                //  Go to SumUpActivity
                Intent toSumUpactivity = new Intent(CandidatesActivity.this,SumUpActivity.class);
                startActivity(toSumUpactivity);
            }
        });


        alert.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        dialog = alert.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.log_out, menu);
        return true;
    }

    //    -------  MENU BUTTON ----------
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {


        // ---- ALERT ----
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        alert.setTitle("LOGOUT");
        alert.setMessage("Do you want Logout? ");

        // Button positive "YES"
        alert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Intent toMainActivity = new Intent(CandidatesActivity.this,MainActivity.class);
                        startActivity(toMainActivity);
                        Storage.getInstance().blockedPESELS.clear();

                    }

                });
        alert.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alert.show();


        return super.onOptionsItemSelected(item);
    }



    // used when the AlertDialog appear in this time we switching to another app.
    @Override
    protected void onPause() {
        super.onPause();
        Storage.getInstance().chosenCandidateChecbox.clear();
        candidatesAdapter.notifyDataSetChanged();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Storage.getInstance().chosenCandidateChecbox.clear();
    }

    //       -----  BACK Button Pressed ----

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {

            getFragmentManager().popBackStack();
        } else {
            Toast.makeText(this, "Please use the LOGOUT button", Toast.LENGTH_SHORT).show();
        }
    }



}
