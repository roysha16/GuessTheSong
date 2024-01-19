package com.roysha.gts;

import java.util.HashMap;
import java.util.Map;

public class Question {

    //Firebase require  to have public variables or getter/setter.
    public int CorrectAnswer;
    public int id;
    public String Question;
    public String A1;
    public String A2;
    public String A3;
    public String A4;
    public Question(){

    }
    public Question(int currentAns, int id, String Quest, String A1, String A2, String A3, String A4){

        this.CorrectAnswer = currentAns;
        this.id = id;
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
        this.A4 = A4;
    }
    public String getAnswer(int Id){
        String ReturnQ = "";
        switch(Id){
            case 1:
                ReturnQ = A1;
                break;
            case 2:
                ReturnQ = A2;
                break;
            case 3:
                ReturnQ = A3;
                break;
            case 4:
                ReturnQ = A4;
                break;
            default:
                break;
        }
        return ReturnQ;
    }

    int getCorrectAnswer(){
        return this.CorrectAnswer;
    }
}