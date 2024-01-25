package com.roysha.gts.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.roysha.gts.ApplicationData;
import com.roysha.gts.Question;
import com.roysha.gts.databinding.FragmentGameBinding;
import com.roysha.gts.ApplicationData;

import java.util.HashMap;
import java.util.Map;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    Button buttonSubmit;
    EditText editText;
    int scorei=67;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GameViewModel gameViewModel =
                new ViewModelProvider(this).get(GameViewModel.class);

        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // final TextView textView = binding.textGame;
       // gameViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        buttonSubmit = binding.submit;
        editText = binding.edittext;


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                scorei = Integer.valueOf(editText.getText().toString());
                ApplicationData.WriteScoreDb(scorei);
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