package com.roysha.gts.ui.score;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.roysha.gts.ApplicationData;
import com.roysha.gts.Score;

public class ScoreViewModel extends ViewModel {

    private final MutableLiveData<String> mTextScore1;
    private final MutableLiveData<String> mTextScore2;
    Score nextScore;


    public ScoreViewModel() {
        mTextScore1 = new MutableLiveData<>();
        nextScore=ApplicationData.getScore(0);
        mTextScore1.setValue(nextScore.toString());
        mTextScore2 = new MutableLiveData<>();
        nextScore=ApplicationData.getScore(1);
        mTextScore2.setValue(nextScore.toString());
    }

    public LiveData<String> getTextS1() {
        return mTextScore1;
    }
    public LiveData<String> getTextS2() {
        return mTextScore2;
    }

}