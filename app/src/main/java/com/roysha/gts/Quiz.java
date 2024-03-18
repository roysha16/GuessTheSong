package com.roysha.gts;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Random;

public class Quiz extends AppCompatActivity {
    private static final int HOW_LONG_TO_WAIT_ANSWER_IN_SEC = 30;

    Button btnSubmit;
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
    TextView tvSecToAnswer;
    CountDownTimer countDownTimer;
    ProgressBar pbTimer;
    int CurrentGameScore = 0;
    int CurrentGameIndex = 0;

    boolean isGameIsOnGoing;

    String videoId; // the ID of the yourtube video to play

    enum GameStatus {
        None,
        WaitingForAnswer,


        EndOneQuestionCorrect,
        EndOneQuestionWrong,

        EndGameNoQuestions,

        GameOver

    }

    enum GameEvent {
        StartNewGame,
        GetNewQuestion,
        SubmitedAnswer,

        TimeOver,

        CorrectAnswer,
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
            3. Make random answers for corrent question
            4. update UI with questions, and button action = "Submit"
            --> Move to "WaitingForAnswer"

     State: WaitingForAnswer
            1. If not selected - do nothing
            2. If selected == question.correctanswer
                --> Move to "EndOneQuestionCorrect"


     */
    GameStatus currentGameStatus;
    ArrayList<Question> LocalGameQuestionsList = new ArrayList<>();

    Question nextQuestion;

    void HandleCountDownTimer(boolean isStart) {
        if (isStart) {
            countDownTimer = new CountDownTimer(HOW_LONG_TO_WAIT_ANSWER_IN_SEC * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = millisUntilFinished / 1000;
                    tvSecToAnswer.setText(String.valueOf(seconds));
                    pbTimer.setProgress((int)seconds,true);

                    boolean shouldWeBeep = false;
                    if (seconds < 3) {
                        shouldWeBeep = true;
                        pbTimer.setOutlineAmbientShadowColor(R.color.red);
                    } else if ((seconds < 10) && (seconds % 2 == 0)) {
                        shouldWeBeep = true;
                    } else if (seconds % 5 == 0) {
                        shouldWeBeep = true;
                    }

                    if (shouldWeBeep) {
                        ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 50);
                    }
                }

                public void onFinish() {
                    tvSecToAnswer.setText("Time is Over!!");
                    currentGameStatus = gameFlow((View) btnSubmit.getParent(), GameStatus.WaitingForAnswer, GameEvent.TimeOver);
                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    toneG.startTone(ToneGenerator.TONE_SUP_ERROR, 1000);
                }
            }.start();
        } else {
            countDownTimer.cancel();
        }
    }


    GameStatus gameFlow(View view,GameStatus nextStep, GameEvent event) {
        //return value indicate if we still in game or it's game over
        GameStatus rc = GameStatus.None;
        int radioButtonID = AnswerGroup.getCheckedRadioButtonId();
        selectedButton = findViewById(radioButtonID);
        int position = AnswerGroup.indexOfChild(selectedButton);
        Question ans[]= new Question[4];
        Random random = new Random();

        int size;

        boolean DidUserFindCorrectAnswer = false;

        switch(event) {
            case StartNewGame:
                CurrentGameScore = 0;
                CurrentGameIndex = 0;
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
                    CreatePopUpVideoMusic(view,"You are the Winner","No More Questions in DB", "RNiflDIWtsk"); //https://www.youtube.com/embed/RNiflDIWtsk
                    btnSubmit.setText("Start New Game");
                    tvGameStatus.setText("You are the Winner!!!!");

                    rc = GameStatus.EndGameNoQuestions;
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
                // we need to find the index of the next question in the global answer list
                int questionId = ans[0].id;
                for(int i=0;i<size;i++) {
                    if(questionId == LocalGameAnswerList.get(i).id) {
                        /// found the correct question and remove it from answerList
                        LocalGameAnswerList.remove(i);
                        break;
                    }

                }

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
                CurrentGameIndex +=1;

                tvQuestion.setText(nextQuestion.Question);
                btnSubmit.setText("Submit Answer");
                tvGameStatus.setText("In Middle Of Game");
                tvIndex.setText(String.valueOf(CurrentGameIndex));

                HandleCountDownTimer(true);


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
                // stop timer
                HandleCountDownTimer(false);

                //tvSecToAnswer.setText("");
                if(position == (nextQuestion.CorrectAnswer-1))
                {
                    tvGameStatus.setText("Correct Answer");
                    tvGameStatus.setBackgroundColor(getColor(R.color.green));
                    Toast.makeText(Quiz.this, "Correct Answer", Toast.LENGTH_SHORT).show();
                    selectedButton.setBackgroundColor(getColor(R.color.green));
                    setRadioGroupStatus(false);
                    CurrentGameScore +=5;
                    tvScore.setText(String.valueOf(CurrentGameScore));

                    CreatePopUpVideoMusic(view,nextQuestion.Question,nextQuestion.getQuestion(nextQuestion.CorrectAnswer), nextQuestion.Song);
                    btnSubmit.setText("Get New Question");

                    rc = GameStatus.EndOneQuestionCorrect;
                } else {
                    tvGameStatus.setText("Wrong Answer");
                    Toast.makeText(Quiz.this, "Wrong Answer", Toast.LENGTH_SHORT).show();
                    selectedButton.setBackgroundColor(getColor(R.color.red));
                    tvGameStatus.setBackgroundColor(getColor(R.color.red));
                    setRadioGroupStatus(false);
                    CreatePopUpVideoMusic(view,nextQuestion.Question,"try again, maybe next time ..", "s5B188EFlvE"); // fulr url https://www.youtube.com/embed/s5B188EFlvE?si=OfRp8og6Gl5Nd5eJ
                    btnSubmit.setText("CloseGame");
                    rc = GameStatus.EndOneQuestionWrong;

                }
                break;
            case TimeOver:

                    tvGameStatus.setText("Wrong Answer - Time is Over");
                    Toast.makeText(Quiz.this, "Time Over", Toast.LENGTH_SHORT).show();
                //selectedButton.setBackgroundColor(getColor(R.color.red));
                    tvGameStatus.setBackgroundColor(getColor(R.color.red));
                    setRadioGroupStatus(false);
                    CreatePopUpVideoMusic(view,nextQuestion.Question,"try again, maybe next time ..", "s5B188EFlvE"); // fulr url https://www.youtube.com/embed/s5B188EFlvE?si=OfRp8og6Gl5Nd5eJ
                    btnSubmit.setText("CloseGame");
                    rc = GameStatus.EndOneQuestionWrong;

                break;


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
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window_music, null);

         TextView TVQuestion = popupView.findViewById(R.id.textViewQuestion);
        TVQuestion.setText(question);

         TextView TVAnswer = popupView.findViewById(R.id.textViewAnswer);
        TVAnswer.setText(answer);

        YouTubePlayerView youTubePlayerView = popupView.findViewById(R.id.videoPlayer);
        getLifecycle().addObserver(youTubePlayerView);
        videoId = url;

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                youTubePlayer.loadVideo(videoId, 0);
            }
        });

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
               // simpleVideoView.stopPlayback();
                popupWindow.dismiss();
                return true;
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                switch (currentGameStatus) {


                    case None:
                    case WaitingForAnswer:

                        break;

                    case EndOneQuestionCorrect:
                        currentGameStatus = gameFlow(view, GameStatus.WaitingForAnswer,GameEvent.GetNewQuestion);

                        break;
                    case GameOver:
                    //case EndOneQuestionWrong:
                    case EndGameNoQuestions:
                        ApplicationData.WriteScoreDb(CurrentGameScore);

                        Intent intent = new Intent(btnSubmit.getContext(), MainActivity.class);
                        startActivity(intent);
                        CurrentGameScore = 0;
                        CurrentGameIndex = 0;

                        finish();
                        break;
                }

            }
        });
        Button closeButton = popupView.findViewById(R.id.CloseBtn);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                popupWindow.dismiss();

            }
        });


    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                }
            });
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        btnSubmit = findViewById(R.id.submit);
        AnswerGroup = findViewById(R.id.simpleRadioButton);
        rb1 = findViewById(R.id.radioButton1);
        rb2 = findViewById(R.id.radioButton2);
        rb3 = findViewById(R.id.radioButton3);
        rb4 = findViewById(R.id.radioButton4);
        tvScore = findViewById(R.id.Score);
        tvIndex = findViewById(R.id.Qnumber);
        tvQuestion = findViewById(R.id.question);
        tvGameStatus = findViewById(R.id.GameStatus);
        tvSecToAnswer = findViewById(R.id.SecToAnswerTxt);
        pbTimer = findViewById(R.id.progressBarTimer);

        pbTimer.setMax(HOW_LONG_TO_WAIT_ANSWER_IN_SEC);

        currentGameStatus = gameFlow(this.getCurrentFocus(),GameStatus.None,GameEvent.StartNewGame);



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



        btnSubmit.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {

                switch (currentGameStatus) {


                    case None:
                    case WaitingForAnswer:

                        currentGameStatus = gameFlow(view, GameStatus.WaitingForAnswer,GameEvent.SubmitedAnswer);
                        break;
                   case EndOneQuestionCorrect:
                       currentGameStatus = gameFlow(view, GameStatus.WaitingForAnswer,GameEvent.GetNewQuestion);

                       break;

                    case GameOver:
                    case EndOneQuestionWrong:
                    case EndGameNoQuestions:
                        ApplicationData.WriteScoreDb(CurrentGameScore);
                        Intent intent = new Intent(btnSubmit.getContext(), MainActivity.class);
                        startActivity(intent);
                        CurrentGameScore = 0;
                        CurrentGameIndex = 0;
                        finish();
                        break;
                }


            }
        });
    }
}


