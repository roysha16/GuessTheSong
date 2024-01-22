package com.roysha.gts;

public class Score {

    public int bestScore;
    public String email;
    public String uID;

    public String date;

    public Score() {

    }

    public Score(int bestScore, String email, String uID,String date ) {
        this.bestScore = bestScore;
        this.email = email;
        this.uID = uID;
        this.date = date;
    }
    public String toString(){
        return String.valueOf(bestScore) + ":" + email;
    }
}

