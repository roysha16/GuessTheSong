package com.roysha.gts;
import java.util.Random;
public class Question {

    //Firebase require  to have public variables or getter/setter.
    public int CorrectAnswer;
    public int id;
    public String A1;
    public String A2;
    public String A3;
    public String A4;
    public String Question;
    public String Song;

    public Question(){

    }
    // Constractor to build random answer
    public  Question(Question correctAns,Question ans2,Question ans3, Question ans4)
    {
        String tmpString;
        Random random = new Random();
        int rand = random.nextInt(4)+1;
        this.A1 = "*" + correctAns.A1;
        this.A2 = ans2.A1;
        this.A3 = ans3.A1;
        this.A4 = ans4.A1;
        this.Question= correctAns.Question;
        this.id = correctAns.id;
        this.CorrectAnswer = rand;
        this.Song = correctAns.Song;

        // put the right answer in random place

        switch(CorrectAnswer) {
            case 1:
                //do nothing;
            break;

            case 2:
                tmpString = A2;
                A2 = A1;
                A1 = tmpString;
            break;

            case 3:
                tmpString = A3;
                A3 = A1;
                A1 = tmpString;
            break;

            case 4:
                tmpString = A4;
                A4 = A1;
                A1 = tmpString;
            break;
        }

    }
    public Question(int CorrectAnswer,int id, String A1, String A2, String A3, String A4, String Question,String Song){
        this.A1 = A1;
        this.A2 = A2;
        this.A3 = A3;
        this.A4 = A4;
        this.Question= Question;
        this.id = id;
        this.CorrectAnswer = CorrectAnswer;
        this.Song = Song;
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
    public String SongGetter()
    {
        return this.Song;
    }

    int getCorrectAnswer(){
        return this.CorrectAnswer;
    }
}