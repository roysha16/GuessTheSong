package com.roysha.gts;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.roysha.gts.Question;
public class ApplicationData {

    ///Roysha test
    // Firebase Database.
    private static final int MAX_SCORE_LIST = 10;



    static ArrayList<Question> QuestionsList = new ArrayList<>();
    static ArrayList<Score> Scores = new ArrayList<>();
    static Score lastGameScore = new Score(99,"roysha@ss.com","111","160606");

    public ApplicationData() {

    }

    static public Score getLastScore(){return lastGameScore;}
    static public void setLastScore(Score newscore){lastGameScore=newscore;}
    static ArrayList<Question> getQuestionsList(){return QuestionsList;}

    static public Score getScore(int indx) {
        Score rcScore = new Score(0, "", "", "");
        if (indx >= Scores.size())
            return rcScore;

        return Scores.get(indx);
    }

    static public boolean isUserAdmin() {
        boolean rc = false;

        FirebaseAuth Auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = Auth.getCurrentUser();

        if(currentUser != null)
        {
            if(currentUser.getEmail().compareTo("q@q.com")==0)
                rc = true;
        }
        return rc;
    }
    static public int getScoreLen() {return Scores.size();}

    static public String[] getScoreArr(){
        int size = Scores.size();
        String rcString[] = new String[size];

        for (int i = 0; i < size; i++) {
            rcString[i]= String.valueOf(i+1) + ". " + getScore(i).toString();
        }

        return rcString;
    }




    static public void WriteScoreDb(int newScore) {
        FirebaseDatabase firebaseDatabase;
        // Reference for Firebase.
        //DatabaseReference dbQReference;
        DatabaseReference dbScoreReference;

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbScoreReference = firebaseDatabase.getReference("Scores");

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);

        Score nScore = new Score(newScore, currentUser.getEmail(), currentUser.getUid(),formattedDate );

        int sizeCurrentTable = Scores.size();
        int sizeMaxAllowedTable = MAX_SCORE_LIST;
        int indxOfMyScore = 0;

        /// check if need to store score
        for(;indxOfMyScore<sizeCurrentTable;indxOfMyScore++)  {
            if(newScore > Scores.get(indxOfMyScore).score) {
              //  Scores.add(i,nScore);
              //  if (i+1 < 5)
                //    dbScoreReference.child(String.valueOf(i+1)).setValue(Scores.get(i));
                //dbScoreReference.child(String.valueOf(i)).setValue(nScore);
                // my score is better then what we have in the table
                break;
            }
        }

        Score scoreAfter=nScore;
        for (int i = indxOfMyScore;
             (indxOfMyScore < sizeMaxAllowedTable) && (indxOfMyScore <= sizeCurrentTable);
             ++indxOfMyScore)
        {
            if ((indxOfMyScore + 1 < sizeMaxAllowedTable) && (indxOfMyScore < sizeCurrentTable)){

                scoreAfter = Scores.get(indxOfMyScore);
            }
            dbScoreReference.child(String.valueOf(indxOfMyScore)).setValue(nScore);
            nScore = scoreAfter;

        }
    }
    static public void WriteQuestionDb( String indx,
             int CorrectAnswer, int id,
             String A1, String A2, String A3, String A4, String Quest){
        FirebaseDatabase firebaseDatabase;
        // Reference for Firebase.
        DatabaseReference dbQReference;
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbQReference = firebaseDatabase.getReference("Questions");

        //Question nQuestion = Question( CorrectAnswer,id, A1, A2, A3, A4, Quest);


          //  dbQReference.child(indx).setValue(nQuestion);

    }
    public void InitDB() {
        FirebaseDatabase firebaseDatabase;
        // Reference for Firebase.
        DatabaseReference dbQReference;
        DatabaseReference dbScoreReference;

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbQReference = firebaseDatabase.getReference("Questions");
        dbScoreReference = firebaseDatabase.getReference("Scores");


        dbQReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Question question = dataSnapshot.getValue(Question.class);
                if(previousChildName == null)
                    QuestionsList.clear();
                //  QuestionsList.add(dataSnapshot.getKey(),question);
                  QuestionsList.add(question);

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        dbScoreReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Score score = dataSnapshot.getValue(Score.class);
                String key = dataSnapshot.getKey();
                if(previousChildName == null)
                    Scores.clear();
                //  QuestionsList.add(dataSnapshot.getKey(),question);
                int iKey = Integer.valueOf(key);
                Scores.add(iKey,score);



            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Score score = dataSnapshot.getValue(Score.class);
                String key = dataSnapshot.getKey();
                //if(previousChildName == null)
                 //   Scores.clear();
                //  QuestionsList.add(dataSnapshot.getKey(),question);
                int iKey = Integer.valueOf(key);
                Scores.set(iKey,score);

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Score score = dataSnapshot.getValue(Score.class);
                String key = dataSnapshot.getKey();
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Score score = dataSnapshot.getValue(Score.class);
                String key = dataSnapshot.getKey();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
