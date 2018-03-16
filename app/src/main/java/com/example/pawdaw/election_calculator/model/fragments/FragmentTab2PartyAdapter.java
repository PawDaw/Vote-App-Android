package com.example.pawdaw.election_calculator.model.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pawdaw.election_calculator.model.mainClasses.Party;
import com.example.pawdaw.election_calculator.R;

import java.util.ArrayList;

/**
 * Created by pawdaw on 05/03/17.
 */

public class FragmentTab2PartyAdapter extends ArrayAdapter<Party> {


    private Context context;
    private ArrayList<Party> parties;


    public FragmentTab2PartyAdapter(Context context, ArrayList<Party> parties) {
        super(context,0, parties);
        this.parties = parties;
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
        final Party party = getItem(position);

        if (row == null) {

//           Refresh List View
            refreshViewList();

            holder = new ViewHolder();


            row = LayoutInflater.from(getContext()).inflate(R.layout.fragment_tab_2_party_row, parent, false);

            holder.name = (TextView) row.findViewById(R.id.party_name);
            holder.amountOfvotes = (TextView) row.findViewById(R.id.amountOfVote_2);

            row.setTag(holder);

        } else {
            holder = (ViewHolder) row.getTag();
        }


        // Populate the data into the template view using the data object
        holder.name.setText(party.getName());
        holder.amountOfvotes.setText(String.valueOf(party.getAmountOfVotes()));


        // Return the completed view to render on screen
        return row;
    }


    public class ViewHolder {
        TextView name;
        TextView amountOfvotes;
    }





}
