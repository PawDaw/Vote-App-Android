package com.example.pawdaw.election_calculator.model.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.pawdaw.election_calculator.model.mainClasses.Candidate;
import com.example.pawdaw.election_calculator.R;
import com.example.pawdaw.election_calculator.model.storage.Storage;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Created by pawdaw on 05/03/17.
 */

public class FragmentTab1CandidateBarChar extends Fragment {

    private BarChart barChart;
    private Log log;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(container == null)
            return  null;

        View view = inflater.inflate(R.layout.fragment_tab_1_candidate_bar_char,container,false);

        barChart = (BarChart) view.findViewById(R.id.bar_char);

        ArrayList<BarEntry> entries = new ArrayList<>();

        int i=0;
        for(Candidate c : Storage.getInstance().getCandidates()){

//            entries.add(new Entry(data.getValueX(), data.getValueY()));
            entries.add(new BarEntry(c.getAmountOfvotes(),i++));

        }

        ArrayList<String> names = new ArrayList<String>();

        for(Candidate c: Storage.getInstance().getCandidates()){
            names.add(c.getName());
        }

//        log.e("info :","names char bar: " + names );

        BarDataSet dataSet = new BarDataSet(entries, "Votes"); // add entries to dataset
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        BarData data = new BarData(names,dataSet);
        barChart.animateY(2000);
        barChart.setScaleXEnabled(true);
        barChart.setData(data);
        barChart.invalidate();

        return view;

    }
}
