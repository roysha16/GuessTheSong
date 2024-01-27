package com.roysha.gts.ui.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roysha.gts.ApplicationData;
import com.roysha.gts.R;
import com.roysha.gts.Score;
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
        ListView simpleListView = binding.simpleListView;
        ArrayList list=new ArrayList<>();
        for(int i=0;i<ApplicationData.getScoreLen();i++){
            HashMap<String,String> item = new HashMap<String,String>();
            int sc = ApplicationData.getScore(i).score;
            item.put("line1", String.valueOf(i+1) + ". " + String.valueOf(sc));
            item.put( "line2", ApplicationData.getScore(i).email + " " + ApplicationData.getScore(i).date );
            item.put( "line3", "");//ApplicationData.getScore(i).date);

            list.add( item );
        }

        SimpleAdapter sa = new SimpleAdapter(getActivity(), list,
                R.layout.activity_listview,
                new String[] { "line1","line2","line3"},
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c});

        simpleListView.setAdapter(sa);

       // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, R.id.textView, ApplicationData.getScoreArr());
       // simpleListView.setAdapter(arrayAdapter);

      //  ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), R.layout.activity_listview, R.id.textViewNumber, courseList);
      //  simpleListView.setAdapter(arrayAdapter2);
        //final TextView textViewGame1 = binding.game1;
       // homeViewModel.getTextS1().observe(getViewLifecycleOwner(), textViewGame1::setText);
       // final TextView textViewGame2 = binding.game2;
       // homeViewModel.getTextS2().observe(getViewLifecycleOwner(), textViewGame2::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}