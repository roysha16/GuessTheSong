package com.roysha.gts;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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

    GameStatus gameFlow(View view,GameStatus nextStep, GameEvent event) {
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
                tvGameStatus.setBackgroundColor(getColor(R.color.black));

                size = LocalGameQuestionsList.size();
                if(size ==0)
                {
                    Toast.makeText(Quiz.this, "End Question List", Toast.LENGTH_SHORT).show();
                    rc = GameStatus.EndOneQuestionWrong;
                    break;

                }
                int rand = random.nextInt(size);
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

                //Toast.makeText(Quiz.this, "Start New Game", Toast.LENGTH_SHORT).show();
                rc = GameStatus.WaitingForAnswer;

                break;

            case SubmitedAnswer:
               // Toast.makeText(Quiz.this, String.valueOf(radioButtonID) + " " + String.valueOf(position), Toast.LENGTH_SHORT).show();
                if(position == -1){
                    rc = GameStatus.WaitingForAnswer;
                    tvGameStatus.setText("Pick an answer");
                    //Toast.makeText(Quiz.this, "Pick Answer", Toast.LENGTH_SHORT).show();

                    break;
                }
                if(position == (nextQuestion.CorrectAnswer-1))
                {
                    tvGameStatus.setText("Currect Answer");
                    tvGameStatus.setBackgroundColor(getColor(R.color.green));
                    Toast.makeText(Quiz.this, "Currect Answer", Toast.LENGTH_SHORT).show();
                    selectedButton.setBackgroundColor(getColor(R.color.green));
                    setRadioGroupStatus(false);
                    CreatePopUpVideoMusic(view,nextQuestion.Question,nextQuestion.getQuestion(nextQuestion.CorrectAnswer), nextQuestion.Song);
                   // CreatePopUpVideoMusic(view,"https://www.youtube.com/embed/skVg5FlVKS0");

                    buttonSubmit.setText("Get New Question");

                    rc = GameStatus.EndOneQuestionCurrect;
                } else {
                    tvGameStatus.setText("Wrong Answer");
                    Toast.makeText(Quiz.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                    selectedButton.setBackgroundColor(getColor(R.color.red));
                    tvGameStatus.setBackgroundColor(getColor(R.color.red));
                    setRadioGroupStatus(false);
                    CreatePopUpVideoMusic(view,nextQuestion.Question,"try again, maybe next time ..", "https://youtu.be/AdqxnVkQ6-U");
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

    void CreatePopUpVideoMusic(View view, String question, String answer, String url) {
        // New Activiy in full view
        //   startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uriString)));
        //   Toast.makeText(Quiz.this, "Playing Video", Toast.LENGTH_SHORT).show();

        //   Toast.makeText(Quiz.this, "Playing Video", Toast.LENGTH_SHORT).show();


        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window_music, null);

         TextView TVQuestion = popupView.findViewById(R.id.textViewQuestion);
        TVQuestion.setText(question);

         TextView TVAnswer = popupView.findViewById(R.id.textViewAnswer);
        TVAnswer.setText(answer);



        WebView webView = popupView.findViewById(R.id.simpleWebView);
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());



        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);


        webView.loadUrl(url+"&mute=0");

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
               // simpleVideoView.stopPlayback();
                popupWindow.dismiss();
                return true;
            }
        });


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

        currentGameStatus = gameFlow(this.getCurrentFocus(),GameStatus.None,GameEvent.StartNewGame);

            // GetContent creates an ActivityResultLauncher<String> to let you pass
// in the mime type you want to let the user select
            ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                    new ActivityResultCallback<Uri>() {
                        @Override
                        public void onActivityResult(Uri uri) {
                            Toast.makeText(Quiz.this, "onActivityResult", Toast.LENGTH_SHORT).show();
                            // Handle the returned Uri
                        }
                    });



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

                        currentGameStatus = gameFlow(view, GameStatus.WaitingForAnswer,GameEvent.SubmitedAnswer);
                        break;
                   case EndOneQuestionCurrect:
                       currentGameStatus = gameFlow(view, GameStatus.WaitingForAnswer,GameEvent.GetNewQuestion);
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


            }
        });
    }
}


