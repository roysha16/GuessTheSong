package com.roysha.gts;

import java.util.HashMap;
import java.util.Map;

public class Question {

    //Firebase require  to have public variables or getter/setter.
    public int idCorrectAnswer;
    public String Q1;
    public String Q2;
    public String Q3;
    public String Q4;
    public Question(){

    }
    public Question(int currentAns, String Q1, String Q2, String Q3, String Q4){
        this.idCorrectAnswer = currentAns;
        this.Q1 = Q1;
        this.Q2 = Q2;
        this.Q3 = Q3;
        this.Q4 = Q4;
    }
    public String getQuestion(int Id){
        String ReturnQ = "";
        switch(Id){
            case 1:
                ReturnQ = Q1;
                break;
            case 2:
                ReturnQ = Q2;
                break;
            case 3:
                ReturnQ = Q3;
                break;
            case 4:
                ReturnQ = Q4;
                break;
            default:
                break;
        }
        return ReturnQ;
    }

    int getCorrectAnswer(){
        return this.idCorrectAnswer;
    }
}