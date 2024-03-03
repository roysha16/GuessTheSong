package com.roysha.gts.ui.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roysha.gts.ApplicationData;
import com.roysha.gts.R;
import com.roysha.gts.databinding.FragmentScoreBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreFragment extends Fragment {

    private FragmentScoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScoreViewModel homeViewModel =
                new ViewModelProvider(this).get(ScoreViewModel.class);

        binding = FragmentScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ListView simpleListView = binding.highscoreListView;
        ArrayList list=new ArrayList<>();

        // Build the high score list
        for(int i=0;i<ApplicationData.getScoreLen();i++){
            HashMap<String,String> item = new HashMap<String,String>();
            int sc = ApplicationData.getScore(i).score;
            item.put("line0", String.valueOf(i+1));
            item.put("line1", "Score is " + String.valueOf(sc));
            item.put( "line2", "User: " + ApplicationData.getScore(i).email);
            item.put( "line3", "Time: "+ ApplicationData.getScore(i).date);

            list.add( item );
        }

        SimpleAdapter sa = new SimpleAdapter(getActivity(), list,
                R.layout.activity_scorelistview,
                new String[] { "line0", "line1","line2","line3"},
                new int[] {R.id.line_0,R.id.line_a, R.id.line_b, R.id.line_c});

        simpleListView.setAdapter(sa);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}