package com.example.pawdaw.election_calculator.model.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.pawdaw.election_calculator.model.storage.Storage;

import org.joda.time.LocalDate;
import org.joda.time.Years;

/**
 * Created by pawdaw on 15/03/17.
 */

public class Validator {


    // --------- Singleton instance  ----------------

    private static Validator instance = new Validator();

    public static Validator getInstance() {

        return instance;
    }

    private Validator() {
        //
    }


    private LocalDate birthDate;
    private Log log;


    // check if String. True = String, False = numbers or string with numbers
    public boolean checkIfString(String string) {

        return string.matches("[a-zA-Z]+");
    }

    // return TRUE if exist that mean BLOCK, FALSE that mean allowed to login
    public boolean checkIfExistVotedPesel(String pesel){
        boolean exist = false;

        if(!Storage.getInstance().getVotedPersonsPesel().isEmpty()){

            for(String s:Storage.getInstance().getVotedPersonsPesel()){
                if(s.equals(pesel)){
                    exist=true;
                }
            }

        }
        return exist;
    }

    // return TRUE if exist that mean BLOCK, FALSE that mean allowed to login
    public boolean checkIfExistBlockedPesel(String pesel) {

        boolean exist = false;


        if(!Storage.getInstance().getBlockedPESELS().isEmpty()){
            for(String s:Storage.getInstance().getBlockedPESELS()){
                if(s.equals(pesel)){
                    exist=true;
                }
            }
        }


        return exist;
    }


    // return TRUE if exist that mean ALLOW
        public boolean checkIfSharedPreferencesKeyExist(String SharedPreferencesKey, Context context, SharedPreferences sharedPreferences){

        boolean exist = false;

        // Check if JSON file exist if not load data form the server and create the JSON file
        //    String fileName = "MyStorage.xml";
        //    File directory = new File("/data/data/com.example.pawdaw.election_calculator/shared_prefs/"+ fileName);
        //    File directory = new File(context.getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/" + fileName);
        //    File directory = new File("/data/data/" + context.getPackageName() +  "/shared_prefs/" + fileName);

        log.e("info :","Shared Preferences  Blocked Pesel LIST : "+ sharedPreferences.contains(SharedPreferencesKey));

        //SharedPreferences has a contains(String key) method, which can be used to check if an entry with the given key exists.
        if(sharedPreferences.contains(SharedPreferencesKey)){

            exist= true;

        }else{

            if (Service.getInstance().isNetworkAvailable(context)){

                // load blocked Pesel from the JSON Server
                Service.getInstance().getDataFromServerJSONBlocked(context,sharedPreferences);

                // load candidates from the JSON Server
                Service.getInstance().getDataFromServerJSONCandidates(context);


            }else{

                //pass from sharedPreferences all blocked PESELs and save to Array located on the Storage.
                Service.getInstance().retrieveBlockedPeselsFromSharedP(sharedPreferences);
                //pass from sharedPreferences all voted PESELs and save to Array located on the Storage.
                Service.getInstance().retrieveVotedPersonsPeselFromSharedP(sharedPreferences);

            }
//            Launching the first time the App requires an internet connection to load important data :-)
            Toast.makeText(context, "Please connect to the internet, launching the first time the App", Toast.LENGTH_SHORT).show();

        }


        return exist;

    }





    // return AGE
    public int calculateAge(PeselValidator peselValidator) {

        //LocalDate birthDate = new LocalDate(1984,4,13);

        int yearOfBirth = peselValidator.getBirthYear();
        int monthOfBirth = peselValidator.getBirthMonth();
        int dayOfBirth = peselValidator.getBirthDay();
        birthDate = new LocalDate(yearOfBirth,monthOfBirth,dayOfBirth);

        LocalDate currentDate = new LocalDate();
        int age = Years.yearsBetween(birthDate, currentDate).getYears();
        log.e("info :","Age: " + age);
        return age;
    }
}
