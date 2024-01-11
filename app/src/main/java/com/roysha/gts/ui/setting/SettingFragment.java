package com.roysha.gts.ui.setting;

import static androidx.core.app.NavUtils.navigateUpTo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roysha.gts.Login;
import com.roysha.gts.MainActivity;
import com.roysha.gts.R;
import com.roysha.gts.Splash;
import com.roysha.gts.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    FirebaseAuth mAuth;
    Button buttonLogout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel notificationsViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        final TextView textViewEmail = binding.Email;

        if (currentUser != null) {
            textViewEmail.setText(currentUser.getEmail());
        } else
        {
            textViewEmail.setText("");
        }


        buttonLogout = binding.Logout;
        View root = binding.getRoot();

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    mAuth.signOut();
                    JumpToSplash();

                }
            }
        });



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void JumpToSplash() {
        Intent intent = new Intent(super.getActivity(), Login.class);
        startActivity(intent);

     }
}