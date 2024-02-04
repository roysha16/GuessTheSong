package com.roysha.gts;

public class Score {

    public int score;
    public String email;
    public String uID;

    public String date;

    public Score() {

    }

    public Score(int score, String email, String uID,String date ) {
        this.score = score;
        this.email = email;
        this.uID = uID;
        this.date = date;
    }
    public String toString(){
        return String.valueOf(score) + " " + email + "at" + date;
    }
}

