package com.example.pawdaw.election_calculator.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pawdaw.election_calculator.R;
import com.example.pawdaw.election_calculator.model.service.PeselValidator;
import com.example.pawdaw.election_calculator.model.service.Service;
import com.example.pawdaw.election_calculator.model.service.Validator;
import com.example.pawdaw.election_calculator.model.storage.Storage;



public class MainActivity extends AppCompatActivity{


    public Context context;
    public PeselValidator peselValidator;
    public SharedPreferences sharedPreferences;

    private EditText nameEditText,surnameEditText,peselEditText;
    private Log log;

    // Shard Preferences, FILE NAME
    private static final String MY_PREFERENCES_FILE = "MyStorage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // required for test
        setContext(this);

        // required for Shared Precedences in the Service
        sharedPreferences = getSharedPreferences(MY_PREFERENCES_FILE,0);


        Storage.getInstance().partyWithVotes.clear();


        // Check Internet Connection
        if (Service.getInstance().isNetworkAvailable(this)){

            // AsyncTask From Service
            Service.getInstance().getDataFromServerJSONBlocked(this,sharedPreferences);


        }else{

            // NO Internet Connection

            //pass from sharedPreferences all blocked PESELs and save to Array located on the Storage.
            Service.getInstance().retrieveBlockedPeselsFromSharedP(sharedPreferences);
            //pass from sharedPreferences all voted PESELs and save to Array located on the Storage.
            Service.getInstance().retrieveVotedPersonsPeselFromSharedP(sharedPreferences);

            if(Storage.getInstance().getBlockedPESELS().isEmpty()){
                Toast.makeText(getApplicationContext(), "No internet connection available", Toast.LENGTH_SHORT).show();
            }

        }

        // Set up the login form.
        nameEditText = (EditText) findViewById(R.id.name);
        surnameEditText = (EditText) findViewById(R.id.surname);
        peselEditText = (EditText) findViewById(R.id.peselEditText);

    }

    // required for test
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    // required for Shared Precedences in the Service
    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }


    // Button Click
    public void onClick_button_Login(View view) {

        attemptLogin();
    }


    private void attemptLogin() {


        // Reset errors.
        nameEditText.setError(null);
        surnameEditText.setError(null);
        peselEditText.setError(null);

        // Store values at the time of the login attempt.
        String name = nameEditText.getText().toString().trim();
        String surname = surnameEditText.getText().toString().trim();
        String pesel = peselEditText.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid name.
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_field_required));
            focusView = nameEditText;
            cancel = true;
        }else if (!Validator.getInstance().checkIfString(name)) {
            nameEditText.setError(getString(R.string.error_field_word));
            focusView = nameEditText;
            cancel = true;
        }


        // Check for a valid surname.
        if (TextUtils.isEmpty(surname)) {
            surnameEditText.setError(getString(R.string.error_field_required));
            focusView = surnameEditText;
            cancel = true;
        }else if (!Validator.getInstance().checkIfString(surname)) {
            surnameEditText.setError(getString(R.string.error_field_word));
            focusView = surnameEditText;
            cancel = true;
        }


        peselValidator = new PeselValidator(pesel);


        // Check for a valid Pesel number.
        if(TextUtils.isEmpty(pesel)){
            peselEditText.setError(getString(R.string.error_field_required));
            focusView = peselEditText;
            cancel = true;
        }else if(pesel.length() < 11){
            peselEditText.setError(getString(R.string.error_Pesel_to_short));
            focusView = peselEditText;
            cancel = true;
        }else if (pesel.length() > 11){
            peselEditText.setError(getString(R.string.error_Pesel_to_long));
            focusView = peselEditText;
            cancel = true;
        }else if (!peselValidator.isValid()){
            peselEditText.setError(getString(R.string.error_Pesel_Invalid));
            focusView = peselEditText;
            cancel = true;
        }
        else if (Validator.getInstance().calculateAge(peselValidator) < 18){
            peselEditText.setError(getString(R.string.error_To_Young));
            focusView = peselEditText;
            cancel = true;
        }
        else if(!Validator.getInstance().checkIfSharedPreferencesKeyExist(Service.getInstance().BLOCKED_PESEL_KEY,this,sharedPreferences)){
            peselEditText.setError(getString(R.string.error_NO_Interent_to_Load_Data));
            focusView = peselEditText;
            cancel = true;
        }
        else if (Validator.getInstance().checkIfExistBlockedPesel(pesel)){
            peselEditText.setError(getString(R.string.error_Pesel_Black_list));
            focusView = peselEditText;
            cancel = true;
        }
        else if (Validator.getInstance().checkIfExistVotedPesel(pesel)){
            peselEditText.setError(getString(R.string.error_Pesel_Voted_pesel));
            focusView = peselEditText;
            cancel = true;
        }

        log.e("info :","blocked Pesel " + Validator.getInstance().checkIfExistBlockedPesel(pesel));
        log.e("info :","file MyStorage.xml " + Validator.getInstance().checkIfSharedPreferencesKeyExist(Service.getInstance().BLOCKED_PESEL_KEY,this,sharedPreferences));


        if (cancel) {

            // There was an ERROR
            focusView.requestFocus();

        } else {

            //
            // save pesel to SharedPreferences, the file MyStorage.xml will contains PESEL number
            Service.getInstance().savePeselToSP(pesel,sharedPreferences);

            // launch Candidates Activity
            Intent toCandidateList = new Intent(MainActivity.this,CandidatesActivity.class);
            startActivity(toCandidateList);

        }
    }
}

