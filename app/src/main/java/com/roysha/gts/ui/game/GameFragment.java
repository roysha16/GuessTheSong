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
import com.roysha.gts.Login;
import com.roysha.gts.MainActivity;
import com.roysha.gts.Question;
import com.roysha.gts.R;
import com.roysha.gts.Splash;
import com.roysha.gts.databinding.FragmentGameBinding;
import com.roysha.gts.ApplicationData;
import com.roysha.gts.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameFragment extends Fragment {

    private FragmentGameBinding binding;
    Button buttonStart;
    EditText editText;
    int scorei=67;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GameViewModel gameViewModel =
                new ViewModelProvider(this).get(GameViewModel.class);

        binding = FragmentGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView simpleListView = binding.simpleListView;
        ArrayList list=new ArrayList<>();
        for(int i=0;i<1;i++){
            HashMap<String,String> item = new HashMap<String,String>();
            int sc = ApplicationData.getLastScore().score;
            //  item.put("line1", String.valueOf(i+1) + ". " + String.valueOf(sc));
             item.put("line1", String.valueOf(sc));
            item.put( "line2", ApplicationData.getLastScore().email);
            item.put( "line3", ApplicationData.getLastScore().date);

            list.add( item );
        }

        SimpleAdapter sa = new SimpleAdapter(getActivity(), list,
                R.layout.activity_listview,
                new String[] { "line1","line2","line3"},
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c});

        simpleListView.setAdapter(sa);
        // final TextView textView = binding.textGame;
       // gameViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        buttonStart = binding.StartNewGame;

        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
             //   scorei = Integer.valueOf(editText.getText().toString());
             //   ApplicationData.WriteScoreDb(scorei);
                Intent intent = new Intent(buttonStart.getContext(), Quiz.class);
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