package com.example.pawdaw.election_calculator.model.storage;

import com.example.pawdaw.election_calculator.model.mainClasses.Candidate;
import com.example.pawdaw.election_calculator.model.mainClasses.Party;
import com.example.pawdaw.election_calculator.model.service.Service;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by pawdaw on 04/03/17.
 */

public class Storage {


    // Singleton instance

    private static Storage instance = new Storage();

    public static Storage getInstance() {

        return instance;
    }

//---------------Array ---------------


    //avoid duplicate item
    public HashSet<Candidate> candidatesHashSetTemp = new HashSet<Candidate>();
    public HashSet<String> partyHashSetTemp = new HashSet<String>();
    public HashSet<String> blockedPESELS = new HashSet<String>();


    public ArrayList<String> votedPersonsPesel = new ArrayList<String>();
    public ArrayList<Candidate> chosenCandidateChecbox = new ArrayList<Candidate>();
    public ArrayList<Party> partyWithVotes = new ArrayList<Party>();


//------------------------------------


    public ArrayList<String> getBlockedPESELS() {

        return new ArrayList<String>(blockedPESELS);
    }

    public void setBlockedPESELS(String pesel) {

        blockedPESELS.add(pesel);
    }


    public ArrayList<Candidate> getCandidates() {
        return new ArrayList<Candidate>(candidatesHashSetTemp);
    }

    public void setCandidates(Candidate candidate) {

        candidatesHashSetTemp.add(candidate);
    }

    public ArrayList<String> getVotedPersonsPesel() {
        return new ArrayList<String>(votedPersonsPesel);
    }

    public void setVotedPersonsPesel(String pesel) {
        votedPersonsPesel.add(pesel);
    }
    public void removeVotedPersonsPesel(String pesel){
        votedPersonsPesel.remove(pesel);
    }

    public ArrayList<Candidate> getChosenCandidateChecbox() {
        return new ArrayList<Candidate>(chosenCandidateChecbox);
    }

    public void setChosenCandidateChecbox(Candidate candidate) {
        chosenCandidateChecbox.add(candidate);
    }

    public void removeChosenCandidateChecbox(Candidate candidate){
        chosenCandidateChecbox.remove(candidate);
    }


    public HashSet<String> getHset() {
        return partyHashSetTemp;

    }

    public void setHset(String partyName) {

        this.partyHashSetTemp.add(partyName);
    }

    public ArrayList<Party> getPartyWithVotes() {
        return new ArrayList<Party>(partyWithVotes);
    }

    public void setPartyWithVotes(Party party) {
        partyWithVotes.add(party);
    }








}
