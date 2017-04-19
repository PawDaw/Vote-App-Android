package com.example.pawdaw.election_calculator.model.mainClasses;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


import com.example.pawdaw.election_calculator.R;
import com.example.pawdaw.election_calculator.model.mainClasses.Candidate;
import com.example.pawdaw.election_calculator.model.service.Service;
import com.example.pawdaw.election_calculator.model.storage.Storage;

import java.util.ArrayList;


/**
 * Created by pawdaw on 04/03/17.
 */

public class CandidatesAdapter extends ArrayAdapter<Candidate> {


    Context context;
    private ArrayList<Candidate> candidates;
    Log log;

    public CandidatesAdapter(Context context, ArrayList<Candidate> candidates) {
        super(context,0, candidates);
        this.candidates = candidates;
        this.context = context;
    }

    // method to reload ViewList
    public void refreshViewList() {

        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {



        View row = convertView;

        // view lookup cache stored in tag
            ViewHolder holder;

        // Get the data item for this position
        final Candidate candidate = getItem(position);

        if (row == null) {

//           Refresh List View
            refreshViewList();

            holder = new ViewHolder();


            row = LayoutInflater.from(getContext()).inflate(R.layout.row_check_box, parent, false);

            holder.name = (TextView) row.findViewById(R.id.name);
            holder.party = (TextView) row.findViewById(R.id.party);
            holder.checkBox = (CheckBox) row.findViewById(R.id.checkBox);
            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }



        // Populate the data into the template view using the data object
        holder.name.setText(candidate.getName());
        holder.party.setText(candidate.getParty());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                if (((CheckBox) view).isChecked()) {

                    Storage.getInstance().setChosenCandidateChecbox(candidate);
                    log.e("info :","setChosenCandidateChecbox " + Storage.getInstance().getChosenCandidateChecbox());


                } else {
                    Storage.getInstance().removeChosenCandidateChecbox(candidate);
                    log.e("info :","removeChosenCandidateChecbox " + Storage.getInstance().getChosenCandidateChecbox());
                }

            }
        });


        // this part of the code turn on or off the checkbox based on the saved items in database
        if (Service.getInstance().checkIfExist(candidate.getName())) {

            holder.checkBox.setChecked(true);
        }
        else {
            holder.checkBox.setChecked(false);
        }


        // Return the completed view to render on screen
        return row;
    }


    public class ViewHolder {
        TextView name;
        TextView party;
        CheckBox checkBox;
    }





}
