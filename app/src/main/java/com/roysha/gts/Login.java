package com.roysha.gts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseUser;
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
    Button buttonLogin;
    FirebaseAuth mAuth;

    public void LoginSuccess() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(Login.this, currentUser.getEmail(),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
    }
    ////end RoySha
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        buttonReg = findViewById(R.id.Register);
        buttonLogin = findViewById(R.id.Login);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            LoginSuccess();
            return;
        }

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
                            LoginSuccess();
                        } else {
                            Toast.makeText(Login.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            Exception excp= task.getException();
                            String str=excp.getMessage();
                            Toast.makeText(Login.this, str,
                                    Toast.LENGTH_LONG).show();


                        }
                    }
                });
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
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
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Login.this, "Success Login",
                                    Toast.LENGTH_SHORT).show();
                            LoginSuccess();
                        } else {
                            Toast.makeText(Login.this, "Login failed.",
                                    Toast.LENGTH_SHORT).show();
                            Exception excp= task.getException();
                            String str=excp.getMessage();
                            Toast.makeText(Login.this, str,
                                    Toast.LENGTH_LONG).show();


                        }
                    }
                });
            }
        });
    }
}