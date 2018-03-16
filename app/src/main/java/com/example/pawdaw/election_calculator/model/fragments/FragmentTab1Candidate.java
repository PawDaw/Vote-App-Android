package com.example.pawdaw.election_calculator.model.fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawdaw.election_calculator.R;
import com.example.pawdaw.election_calculator.model.storage.Storage;

/**
 * Created by pawdaw on 05/03/17.
 */

public class FragmentTab1Candidate extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(container == null)
            return  null;

        View view = inflater.inflate(R.layout.fragment_tab_1_candidate,container,false);

        FragmentTab1CandidateAdapter adapter = new FragmentTab1CandidateAdapter(getActivity(), Storage.getInstance().getCandidates());
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;

    }
}
