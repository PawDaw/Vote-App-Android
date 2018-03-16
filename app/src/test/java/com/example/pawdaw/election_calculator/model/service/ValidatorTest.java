package com.example.pawdaw.election_calculator.model.service;

import android.content.SharedPreferences;
import android.os.Build;

import com.example.pawdaw.election_calculator.BuildConfig;
import com.example.pawdaw.election_calculator.controller.MainActivity;
import com.example.pawdaw.election_calculator.model.service.PeselValidator;
import com.example.pawdaw.election_calculator.model.service.Service;
import com.example.pawdaw.election_calculator.model.service.Validator;
import com.example.pawdaw.election_calculator.model.storage.Storage;

import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

// Static imports for assertion methods

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricTestRunner.class)

/**
 * Created by pawdaw on 16/03/17.
 */
public class ValidatorTest {

    private MainActivity mainActivity;
    private static final String TEST_KEY = "test";
    private static final String BLOCKED_PESEL_KEY = "blockedPesel";

    @Before
    public void setUp() throws Exception {


        mainActivity = Robolectric.setupActivity(MainActivity.class);

        Service.getInstance().getDataFromServerJSONBlocked(mainActivity.getContext(),mainActivity.getSharedPreferences());

    }



    @Test
    public void checkIfString() throws Exception {



        //TRUE
        assertTrue(Validator.getInstance().checkIfString("pawel"));

        //FALSE, wrong format with numbers
        assertTrue(!Validator.getInstance().checkIfString("pawel1234"));


    }


    @Test
    public void checkIfExistBlockedPesel() throws Exception {


        String pesel = "73101015127";

        //TRUE
        assertTrue(Validator.getInstance().checkIfExistBlockedPesel(pesel));

    }

    @Test
    public void checkIfExistVotedPesel() throws  Exception {

        String blockedPesel = "73101015127";

        // let check if blocked PESEL exist in voted list.
        // Expect FALSE
        assertTrue(!Validator.getInstance().checkIfExistVotedPesel(blockedPesel));


        // lets save the same PESEL to Shared Preferences
        Service.getInstance().savePeselToSP(blockedPesel,mainActivity.getSharedPreferences());
        Service.getInstance().passPeselToStorageArray(mainActivity.getSharedPreferences());

        // now check if exist
        // Expect TRUE
        assertTrue(Validator.getInstance().checkIfExistVotedPesel(blockedPesel));

        // now remove added PESEL from Storage
        Storage.getInstance().removeVotedPersonsPesel(blockedPesel);
        // update Shared Preferences
        Service.getInstance().passPeselToStorageArray(mainActivity.getSharedPreferences());


    }

    @Test
    public void checkIfSharedPreferencesKey() throws Exception {


        //the file MyStorage.xml
        SharedPreferences sharedPreferences = mainActivity.getSharedPreferences();

        // save to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEST_KEY, "test_value");
        editor.apply();

        //check method
        assertTrue(Validator.getInstance().checkIfSharedPreferencesKeyExist(TEST_KEY,mainActivity.getContext(),mainActivity.getSharedPreferences()));

    }

    @Test
    public void calculateAge() throws Exception {

        String pesel = "84041312573";
        PeselValidator peselValidator = new PeselValidator(pesel);


        int yearOfBirth = peselValidator.getBirthYear();
        int monthOfBirth = peselValidator.getBirthMonth();
        int dayOfBirth = peselValidator.getBirthDay();

        LocalDate birthDate = new LocalDate(yearOfBirth,monthOfBirth,dayOfBirth);

        //LocalDate currentDate = new LocalDate();
        LocalDate currentDate = new LocalDate(2017,3,13);

        int age = Years.yearsBetween(birthDate, currentDate).getYears();

        assertEquals(age,32);



    }
}