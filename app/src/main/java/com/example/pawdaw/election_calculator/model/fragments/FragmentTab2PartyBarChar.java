package com.example.pawdaw.election_calculator.model.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawdaw.election_calculator.model.mainClasses.Party;
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

public class FragmentTab2PartyBarChar extends Fragment {

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
        for(Party p : Storage.getInstance().getPartyWithVotes()){

//            entries.add(new Entry(data.getValueX(), data.getValueY()));
            entries.add(new BarEntry(p.getAmountOfVotes(),i++));

        }

        ArrayList<String> names = new ArrayList<String>();

        for(Party p2 : Storage.getInstance().getPartyWithVotes()){
            names.add(p2.getName());
        }

//        log.e("info :","names char bar party: " + names );


        BarDataSet dataSet = new BarDataSet(entries, "Votes"); // add entries to dataset
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        BarData data = new BarData(names,dataSet);
        barChart.setScaleXEnabled(true);
        barChart.animateY(4000);
        barChart.setData(data);
        barChart.invalidate();

        return view;

    }
}
