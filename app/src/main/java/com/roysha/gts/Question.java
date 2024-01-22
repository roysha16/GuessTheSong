package com.roysha.gts;

import java.util.HashMap;
import java.util.Map;

public class Question {

    //Firebase require  to have public variables or getter/setter.
    public int CorrectAnswer;
    public int id;
    public String A1;
    public String A2;
    public String A3;
    public String A4;
    public String Question;

    public Question(){

    }
    public Question(int CorrectAnswer,int id, String A1, String A2, String A3, String A4, String Question){
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
        this.A4 = A4;
        this.Question= Question;
        this.id = id;
        this.CorrectAnswer = CorrectAnswer;
    }
    public String getQuestion(int Id){
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