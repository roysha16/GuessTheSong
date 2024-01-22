package com.roysha.gts.ui.score;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.roysha.gts.databinding.FragmentScoreBinding;

public class ScoreFragment extends Fragment {

    private FragmentScoreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ScoreViewModel homeViewModel =
                new ViewModelProvider(this).get(ScoreViewModel.class);

        binding = FragmentScoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textViewGame1 = binding.game1;
        homeViewModel.getTextS1().observe(getViewLifecycleOwner(), textViewGame1::setText);
        final TextView textViewGame2 = binding.game2;
        homeViewModel.getTextS2().observe(getViewLifecycleOwner(), textViewGame2::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}