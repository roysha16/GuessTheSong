package com.roysha.gts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button btnReg;
    Button btnLogin;
    FirebaseAuth mAuth;

    public void LoginSuccess() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(Login.this, currentUser.getEmail(),
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editEmail);
        editTextPassword = findViewById(R.id.editPassword);
        btnReg = findViewById(R.id.Register);
        btnLogin = findViewById(R.id.Login);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            LoginSuccess();
            return;
        }

        btnReg.setOnClickListener(new View.OnClickListener() {
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
        btnLogin.setOnClickListener(new View.OnClickListener() {
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