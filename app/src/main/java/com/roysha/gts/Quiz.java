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
import android.widget.RadioGroup;
import android.widget.Toast;

public class Quiz extends AppCompatActivity {

    Button buttonSubmit;
    RadioGroup AnswerGroup;
    RadioButton selectedButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        buttonSubmit = findViewById(R.id.submit);
        AnswerGroup = findViewById(R.id.simpleRadioButton);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {

            boolean gameFlow()
            {
                //return value indicate if we still in game or it's game over
                boolean rc = true;

                int radioButtonID = AnswerGroup.getCheckedRadioButtonId();
                selectedButton = findViewById(radioButtonID);
                int position = AnswerGroup.indexOfChild(selectedButton);

                Toast.makeText(Quiz.this, String.valueOf(radioButtonID) +" " + String.valueOf(position), Toast.LENGTH_SHORT).show();

                if(position ==3)
                {
                    buttonSubmit.setText("yeah");
                }

                return  rc;

            }
            public void onClick(View view) {
                boolean isGameIsOnGoing = gameFlow();

                if (isGameIsOnGoing == false) {

                    Intent intent = new Intent(buttonSubmit.getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}


