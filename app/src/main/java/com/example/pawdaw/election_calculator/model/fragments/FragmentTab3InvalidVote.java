package com.example.pawdaw.election_calculator.model.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pawdaw.election_calculator.controller.MainActivity;
import com.example.pawdaw.election_calculator.R;
import com.example.pawdaw.election_calculator.controller.SumUpActivity;

/**
 * Created by pawdaw on 05/03/17.
 */

public class FragmentTab3InvalidVote extends Fragment {

    private TextView amonutOfvotes;
    private SharedPreferences sharedPreferencesREAD;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(container == null)
            return  null;

        SumUpActivity activity = (SumUpActivity) getContext();
        sharedPreferencesREAD = activity.getSharedPreferences();

        View view = inflater.inflate(R.layout.fragment_tab_3_invalid_vote,container,false);

//        read from Shared Preferences
        String amountOfInvalidVotes = sharedPreferencesREAD.getString("invalidVote", String.valueOf(0));

        amonutOfvotes = (TextView) view.findViewById(R.id.amount);
        amonutOfvotes.setText(amountOfInvalidVotes);
        return view;
    }
}
