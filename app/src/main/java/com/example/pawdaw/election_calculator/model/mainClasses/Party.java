package com.example.pawdaw.election_calculator.model.mainClasses;

/**
 * Created by pawdaw on 05/03/17.
 */

public class Party {

    private String name;
    private int amountOfVotes;


    public Party(String name) {
        this.name = name;
    }

    public Party(String name, int amountOfVotes) {
        this.name = name;
        this.amountOfVotes = amountOfVotes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountOfVotes() {
        return amountOfVotes;
    }

    public void setAmountOfVotes(int amountOfVotes) {
        this.amountOfVotes = amountOfVotes;
    }

    @Override
    public String toString() {
        return name;
    }
}
