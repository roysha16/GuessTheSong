package com.roysha.gts;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApplicationData {

    ///Roysha test
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;
    // Reference for Firebase.
    DatabaseReference dbQReference;
    DatabaseReference dbScoreReference;


    ArrayList<Question> QuestionsList = new ArrayList<>();
    static ArrayList<Score> Scores = new ArrayList<>();//Score[10];

    public ApplicationData() {

    }

    static public Score getScore(int indx) { return Scores.get(indx);}



    public void WriteScoreDb(int newScore) {

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        Score nScore = new Score(newScore, currentUser.getEmail(), currentUser.getUid(),formattedDate );

        dbScoreReference.child(String.valueOf(newScore)).setValue(nScore);


        //   Map<String, Question> Qs = new HashMap<>();
    //    Qs.put("1", new  Question(2, "Question 1?", "Question 2?", "Question 3?", "Question 4?"));
    //    Qs.put("2", new  Question(3, "Question 21?", "Question 22?", "Question 3?", "Question 4?"));
        //  dbQReference.setValue(Qs);
     //   Question Quest1 = new Question(4, "Question4 1?", "Question4 2?", "Qu4estion 3?", "Question 4?");
      //  dbQReference.child("4").setValue(Quest1);
    }
    public void InitDB() {

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
                if(previousChildName == null)
                    Scores.clear();
                //  QuestionsList.add(dataSnapshot.getKey(),question);
                Scores.add(score);

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
    }
}
