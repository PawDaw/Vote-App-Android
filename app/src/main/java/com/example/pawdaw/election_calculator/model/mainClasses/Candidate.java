package com.example.pawdaw.election_calculator.model.mainClasses;

/**
 * Created by pawdaw on 04/03/17.
 */

public class Candidate {

    private String name;
    private String party;
    private int amountOfVotes;


    public Candidate(String name, String party, int amountOfvotes) {
        this.name = name;
        this.party = party;
        this.amountOfVotes = amountOfvotes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public int getAmountOfvotes() {
        return amountOfVotes;
    }

    public void setAmountOfvotes(int amountOfvotes) {
        this.amountOfVotes = amountOfvotes;
    }


    // ------- HashMAP ------
    // required for hashmap, avoid duplicate item
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Candidate other = (Candidate) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    // ------- end HashMAP ------



    @Override
    public String toString() {
        return name;
    }
}
