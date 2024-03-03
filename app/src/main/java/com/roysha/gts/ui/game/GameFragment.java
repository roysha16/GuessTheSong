package com.roysha.gts.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.roysha.gts.ApplicationData;
import com.roysha.gts.R;
import com.roysha.gts.databinding.FragmentGameBinding;

import com.roysha.gts.Quiz;

import java.util.ArrayList;
import java.util.HashMap;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    Button btnStart;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GameViewModel gameViewModel =
                new ViewModelProvider(this).get(GameViewModel.class);

        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView simpleListView = binding.ScoreListView;

        // build array of last score. for now we decided that we keep only one last score
        // but if we like we can build from it a list
        ArrayList list=new ArrayList<>();

        for(int i=0;i<1;i++){
            HashMap<String,String> item = new HashMap<String,String>();
            int sc = ApplicationData.getLastScore().score;

            if(sc >=0) {//only if we start a game score will be >=0

                item.put("line0", "");
                item.put("line1", "Score is " + String.valueOf(sc));
                item.put("line2", "User: " + ApplicationData.getLastScore().email);
                item.put("line3", "Time: " + ApplicationData.getLastScore().date);
            } else {
                // no one played yet
                item.put("line0", "");
                item.put("line1", "Still didn't play");
                item.put("line2", "Let's Play a game");
                item.put("line3", "Good Luck!!!");
            }

            list.add( item );
        }


        SimpleAdapter sa = new SimpleAdapter(getActivity(), list,
                R.layout.activity_scorelistview,
                new String[]{"line0", "line1", "line2", "line3"},
                new int[]{R.id.line_0, R.id.line_a, R.id.line_b, R.id.line_c});

        simpleListView.setAdapter(sa);

        btnStart = binding.StartNewGame;

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // start new game, intent to quiz activity for the game
                Intent intent = new Intent(btnStart.getContext(), Quiz.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}