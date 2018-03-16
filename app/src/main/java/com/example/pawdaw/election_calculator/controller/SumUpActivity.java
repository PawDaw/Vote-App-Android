package com.example.pawdaw.election_calculator.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.pawdaw.election_calculator.R;
import com.example.pawdaw.election_calculator.model.fragments.FragmentTab1Candidate;
import com.example.pawdaw.election_calculator.model.fragments.FragmentTab2Party;
import com.example.pawdaw.election_calculator.model.fragments.FragmentTab3InvalidVote;

/**
 * Created by pawdaw on 05/03/17.
 */

public class SumUpActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public SharedPreferences sharedPreferences;

    // Shard Preferences, FILE NAME
    private static final String MY_PREFERENCES_FILE = "MyStorage";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sum_up_activity);

        // required for Shared Precedences in the Service
        sharedPreferences = getSharedPreferences(MY_PREFERENCES_FILE,0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }

    public SharedPreferences getSharedPreferences(){
        return sharedPreferences;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bar_char, menu);
        return true;
    }

    public void logOut_menu_icon(MenuItem item) {

        // ---- ALERT ----
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
        alert.setTitle("LOGOUT");
        alert.setMessage("Do you want Logout? ");

        // Button positive "YES"
        alert.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Intent toMainActivity = new Intent(SumUpActivity.this,MainActivity.class);
                        startActivity(toMainActivity);
                    }

                });

        alert.setNegativeButton("NO",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alert.show();
    }

    public void bar_car_menu_icon(MenuItem item) {

        Intent toBarCharActivity = new Intent(SumUpActivity.this,BarChartActivity.class);
        startActivity(toBarCharActivity);

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FragmentTab1Candidate tab_1 = new FragmentTab1Candidate();
                    return tab_1;
                case 1:
                    FragmentTab2Party tab_2 = new FragmentTab2Party();
                    return tab_2;
                case 2:
                    FragmentTab3InvalidVote tab_3 = new FragmentTab3InvalidVote();
                    return tab_3;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Candidates";
                case 1:
                    return "Party";
                case 2:
                    return "Invalid Vote";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {

            getFragmentManager().popBackStack();
        } else {
            Toast.makeText(this, "Please use the LOGOUT button", Toast.LENGTH_SHORT).show();
        }
    }
}
