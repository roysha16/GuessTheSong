package com.roysha.gts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
/*
    ///Roysha test
    Map<String, Question> QuestionsList = new HashMap<>();

    public void basicWriteDb() {

        // Firebase Database.
        FirebaseDatabase firebaseDatabase;
        // Reference for Firebase.
        DatabaseReference dbQReference;

        firebaseDatabase = FirebaseDatabase.getInstance();
        dbQReference = firebaseDatabase.getReference("Questions");

        Map<String, Question> Qs = new HashMap<>();
        Qs.put("1", new  Question(2, "Question 1?", "Question 2?", "Question 3?", "Question 4?"));
        Qs.put("2", new  Question(3, "Question 21?", "Question 22?", "Question 3?", "Question 4?"));
        dbQReference.setValue(Qs);
        Question Quest1 = new Question(4, "Question4 1?", "Question4 2?", "Qu4estion 3?", "Question 4?");
        dbQReference.child("4").setValue(Quest1);
    }
    private void basicReadDb() {

        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Questions");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Question question = dataSnapshot.getValue(Question.class);
                if(previousChildName == null)
                    QuestionsList.clear();
                QuestionsList.put(dataSnapshot.getKey(),question);
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
    }*/

    ////end RoySha
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        buttonReg = findViewById(R.id.Register);
        mAuth = FirebaseAuth.getInstance();

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this, "Account created.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            Exception excp= task.getException();
                            String str=excp.getMessage();
                            Toast.makeText(Login.this, str,
                                    Toast.LENGTH_LONG).show();
                            //           Log.w("login", "signInWithEmail:failure", task.getException());


                        }
                    }
                });
            }
        });
    }
}