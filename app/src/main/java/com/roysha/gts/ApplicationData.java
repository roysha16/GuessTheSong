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

public class ApplicationData {

    ///Roysha test
    // Firebase Database.


    ArrayList<Question> QuestionsList = new ArrayList<>();
    static ArrayList<Score> Scores = new ArrayList<>();//Score[10];

    public ApplicationData() {

    }

    static public Score getScore(int indx) {
        Score rcScore = new Score(0, "", "", "");
        if (indx >= Scores.size())
            return rcScore;

        return Scores.get(indx);
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

        int size = Scores.size();
        int i = 0;
        /// check if need to store score
        for(;i<size;i++)  {
            if(newScore > Scores.get(i).score) {
              //  Scores.add(i,nScore);
                if (i+1 < 5)
                    dbScoreReference.child(String.valueOf(i+1)).setValue(Scores.get(i));
                dbScoreReference.child(String.valueOf(i)).setValue(nScore);

                break;
            }
        }
        if ((i==size) && (size < 5)) {
            //Scores.add(nScore);
            dbScoreReference.child(String.valueOf(i)).setValue(nScore);

        }
        //dbScoreReference.setValue(Scores);


        //   Map<String, Question> Qs = new HashMap<>();
    //    Qs.put("1", new  Question(2, "Question 1?", "Question 2?", "Question 3?", "Question 4?"));
    //    Qs.put("2", new  Question(3, "Question 21?", "Question 22?", "Question 3?", "Question 4?"));
        //  dbQReference.setValue(Qs);
     //   Question Quest1 = new Question(4, "Question4 1?", "Question4 2?", "Qu4estion 3?", "Question 4?");
      //  dbQReference.child("4").setValue(Quest1);
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
               // if(previousChildName == null)
                 //   Scores.clear();
                //  QuestionsList.add(dataSnapshot.getKey(),question);
                int iKey = Integer.valueOf(key);
                Scores.set(iKey,score);

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
    }
}
