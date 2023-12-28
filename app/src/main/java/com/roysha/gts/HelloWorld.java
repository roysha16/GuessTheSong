package com.roysha.gts;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
class HelloWorld{
    public static void main(String[]    args) {
        Question Quest1 = new Question(2, "Question 1?", "Question 2?", "Question 3?", "Question 4?");
        System.out.println("Hello, World!");
        System.out.println(Quest1.getQuestion(1));
        System.out.println(Quest1.getQuestion(2));
        System.out.println(Quest1.getQuestion(3));
        System.out.println(Quest1.getQuestion(4));
        System.out.println(Quest1.getCorrectAnswer());

      //  private DatabaseReference mDatabase;





    }


}

