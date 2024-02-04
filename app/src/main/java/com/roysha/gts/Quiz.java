package com.roysha.gts;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Random;

public class Quiz extends AppCompatActivity {

    Button buttonSubmit;
    RadioGroup AnswerGroup;
    RadioButton selectedButton;

    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioButton rb4;
    TextView tvScore;
    TextView tvIndex;
    TextView tvQuestion;
    TextView tvGameStatus;
    int CurrentGameScore=0;
    int CurrentGameIndex=0;

    boolean isGameIsOnGoing;

    enum GameStatus {
        None,
        WaitingForAnswer,


        EndOneQuestionCurrect,
        EndOneQuestionWrong,

        GameOver

    }
    enum GameEvent {
        StartNewGame,
        GetNewQuestion,
        SubmitedAnswer,

        CurrectAnswer,
        WrongAnswer
    }

    /*
      State: None/Create View
            1. Reset Global Score value
            2. Copy Questions Array from DB to local game Question Array
            2. Send Event GetNewQuestion
            --> Move to "Create New Question"

      State: GetNewQuestion
            1. GetRandom Question from local game Question Array.
            2. Remove Question from local game Question Array.
            3. Make random answers for current question
            4. update UI with questions, and button action = "Submit"
            --> Move to "WaitingForAnswer"

     State: WaitingForAnswer
            1. If not selected - do nothing
            2. If selected == question.currectanswer
                --> Move to "EndOneQuestionCurrect"


     */
    GameStatus currentGameStatus;
    ArrayList<Question> LocalGameQuestionsList =  new ArrayList<>();

    Question nextQuestion;

    GameStatus gameFlow(GameStatus nextStep, GameEvent event) {
        //return value indicate if we still in game or it's game over
        GameStatus rc = GameStatus.None;
        int radioButtonID = AnswerGroup.getCheckedRadioButtonId();
        selectedButton = findViewById(radioButtonID);
        int position = AnswerGroup.indexOfChild(selectedButton);
        Question ans[]= new Question[4];
        Random random = new Random();

        int size;

        boolean DidUserFindCurrectAnswer = false;

               // Level myVar = Level.MEDIUM;

        switch(event) {
            case StartNewGame:
                CurrentGameScore = 0;
                CurrentGameIndex = 1;
                size = ApplicationData.getQuestionsList().size();
                for(int i=0;i<size;i++) {
                    LocalGameQuestionsList.add( ApplicationData.getQuestionsList().get(i));
                }

            case  GetNewQuestion:
                AnswerGroup.clearCheck();
                setRadioGroupStatus(true);

                int rand = random.nextInt(LocalGameQuestionsList.size());
                ans[0] = LocalGameQuestionsList.get(rand);
                LocalGameQuestionsList.remove(rand);

                ArrayList<Question> LocalGameAnswerList =  new ArrayList<>();
                size = ApplicationData.getQuestionsList().size();
                for(int i=0;i<size;i++) {
                    LocalGameAnswerList.add( ApplicationData.getQuestionsList().get(i));
                }
                LocalGameAnswerList.remove(rand);

                for(int i=0;i<3;i++){
                    rand = random.nextInt(LocalGameAnswerList.size());
                    ans[i+1]=LocalGameAnswerList.get(rand);
                    LocalGameAnswerList.remove(rand);
                }

                nextQuestion = new Question(ans[0], ans[1], ans[2],  ans[3]);
                rb1.setText(nextQuestion.A1);
                rb2.setText(nextQuestion.A2);
                rb3.setText(nextQuestion.A3);
                rb4.setText(nextQuestion.A4);

                tvQuestion.setText(nextQuestion.Question);
                buttonSubmit.setText("Submit Answer");
                tvGameStatus.setText("In Middle Of Game");

                Toast.makeText(Quiz.this, "Start New Game", Toast.LENGTH_SHORT).show();
                rc = GameStatus.WaitingForAnswer;

                break;

            case SubmitedAnswer:
                Toast.makeText(Quiz.this, String.valueOf(radioButtonID) + " " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                if(position == -1){
                    rc = GameStatus.WaitingForAnswer;
                    tvGameStatus.setText("Pick an answer");
                    Toast.makeText(Quiz.this, "Pick Answer", Toast.LENGTH_SHORT).show();

                    break;
                }
                if(position == (nextQuestion.CorrectAnswer-1))
                {
                    tvGameStatus.setText("Currect Answer");
                    Toast.makeText(Quiz.this, "Currect Answer", Toast.LENGTH_SHORT).show();
                    selectedButton.setBackgroundColor(getColor(R.color.green));
                    setRadioGroupStatus(false);
                    buttonSubmit.setText("Get New Question");

                    rc = GameStatus.EndOneQuestionCurrect;
                } else {
                    tvGameStatus.setText("Wrong Answer");
                    Toast.makeText(Quiz.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                    selectedButton.setBackgroundColor(getColor(R.color.red));
                    setRadioGroupStatus(false);
                    buttonSubmit.setText("CloseGame");
                    rc = GameStatus.EndOneQuestionWrong;
                }


        }
        return rc;

    }

    void setRadioGroupStatus(boolean isEnabled)
    {
        //set default to false
        for(int i = 0; i < AnswerGroup.getChildCount(); i++){
            ((RadioButton)AnswerGroup.getChildAt(i)).setEnabled(isEnabled);
        }
    }
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        buttonSubmit = findViewById(R.id.submit);
        AnswerGroup = findViewById(R.id.simpleRadioButton);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
        tvScore = findViewById(R.id.Score);
        tvIndex = findViewById(R.id.Qnumber);
        tvQuestion = findViewById(R.id.question);
        tvGameStatus = findViewById(R.id.GameStatus);
        //buttonSubmit.setText("Submit");

        currentGameStatus = gameFlow(GameStatus.None,GameEvent.StartNewGame);



    AnswerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // TODO Auto-generated method stub
                    boolean isChecked=false;
                    // This will get the radiobutton that has changed in its check state
                        RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                    // This puts the value (true/false) into the variable
                    if(checkedRadioButton != null)
                        isChecked = checkedRadioButton.isChecked();
                    // If the radiobutton that has changed in check state is now checked...

                    //Toast.makeText(Quiz.this, String.valueOf(isChecked) + " " + String.valueOf(checkedId), Toast.LENGTH_SHORT).show();

                    rb1.setBackgroundColor(getColor(R.color.black));
                    rb2.setBackgroundColor(getColor(R.color.black));
                    rb3.setBackgroundColor(getColor(R.color.black));
                    rb4.setBackgroundColor(getColor(R.color.black));
                    if(isChecked)
                        checkedRadioButton.setBackgroundColor(getColor(R.color.grey));



                }
            });



        buttonSubmit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {

                switch (currentGameStatus) {


                    case None:
                    case WaitingForAnswer:

                        currentGameStatus = gameFlow(GameStatus.WaitingForAnswer,GameEvent.SubmitedAnswer);
                        break;
                   case EndOneQuestionCurrect:
                       currentGameStatus = gameFlow(GameStatus.WaitingForAnswer,GameEvent.GetNewQuestion);
                       CurrentGameScore +=5;
                       CurrentGameIndex +=1;
                       tvScore.setText(String.valueOf(CurrentGameScore));
                       tvIndex.setText(String.valueOf(CurrentGameIndex));

                       break;

                    case GameOver:
                    case EndOneQuestionWrong:
                        Intent intent = new Intent(buttonSubmit.getContext(), MainActivity.class);
                        startActivity(intent);
                        ApplicationData.WriteScoreDb(CurrentGameScore);
                        CurrentGameScore = 0;
                        CurrentGameIndex = 1;
                        finish();
                        break;
                }

               // if (isGameIsOnGoing == false) {

                    /*  Intent intent = new Intent(buttonSubmit.getContext(), MainActivity.class);
                    startActivity(intent);
                    ApplicationData.WriteScoreDb(CurrentGameScore);
                    CurrentGameScore = 0;
*/
             //   }

            }
        });
    }
}

// Old POP up code
/*
                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                TextView TVQuestion = popupView.findViewById(R.id.textViewQuestion);

                TextView TVAnswer = popupView.findViewById(R.id.textViewAnswer);
                TextView TVScore = popupView.findViewById(R.id.textViewScore);

                TVQuestion.setText(gameQuestion);
                TVAnswer.setText(gameAnswer);
                TVScore.setText(String.valueOf(CurrentGameScore));
                ConstraintLayout cl = (ConstraintLayout)popupView.findViewById(R.id.popup_window);

                if(DidUserFindCurrectAnswer) {
                    cl.setBackgroundColor(0xFF000000);


                }
               if(DidUserFindCurrectAnswer) {

                   cl.setBackgroundColor(getColor(R.color.green));//null));
                    //cl.setBackgroundColor(0xff10b566);
                //    popupView.setBackgroundColor(0xff10b566);


                }
                else {
                   // cl.setBackgroundColor(0xffe72a16);
                   cl.setBackgroundColor(getColor(R.color.red));//,null));

                 //   popupView.setBackgroundColor(R.color.red);

                }


                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it

                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);




                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        buttonSubmit.setText("Submit");
                        if(isGameIsOnGoing == false) {
                            Intent intent = new Intent(popupView.getContext(), MainActivity.class);
                            startActivity(intent);
                            ApplicationData.WriteScoreDb(CurrentGameScore);
                            finish();

                        }
                        return true;
                    }
                });

                return  rc;

            }*/
