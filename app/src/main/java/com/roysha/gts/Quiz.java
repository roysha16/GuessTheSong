package com.roysha.gts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Quiz extends AppCompatActivity {

    Button buttonSubmit;
    static int score = 5; // just for testing

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        buttonSubmit = findViewById(R.id.submit);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // do the game
                score = score+ 3;
                ApplicationData.WriteScoreDb(score);;


                Intent intent = new Intent(buttonSubmit.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}


