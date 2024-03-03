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
import com.roysha.gts.ApplicationData;
import com.roysha.gts.Login;
import com.roysha.gts.MainActivity;
import com.roysha.gts.R;
import com.roysha.gts.Splash;
import com.roysha.gts.UpdateDBActivity;
import com.roysha.gts.databinding.FragmentSettingBinding;
import android.widget.SimpleAdapter;

public class SettingFragment extends Fragment {

    private FragmentSettingBinding binding;
    FirebaseAuth mAuth;
    Button btnLogout;
    Button btnUpdateDb;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingViewModel notificationsViewModel =
                new ViewModelProvider(this).get(SettingViewModel.class);

        binding = FragmentSettingBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        final TextView textViewEmail = binding.Email;

        // if connected to firebase user, copy the email to the ui
        if (currentUser != null) {
            textViewEmail.setText(currentUser.getEmail());
        } else
        {
            textViewEmail.setText("");
        }

        final TextView textViewUserType = binding.UserType;

        // check if the user is admin / regular.
        // if it admin, we will let him to be able to update the DB from UI
        if (ApplicationData.isUserAdmin()) {
            textViewUserType.setText("User is Admin");
        } else
        {
            textViewUserType.setText("User is Regular");
        }


        btnLogout = binding.Logout;
        View root = binding.getRoot();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                // if user connected to firebase, then we will signout from firebase
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if(currentUser != null){
                    mAuth.signOut();
                    JumpToSplash();


                }
            }
        });

        btnUpdateDb = binding.UpdateDB;
        btnUpdateDb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ApplicationData.isUserAdmin()) {
                    // if user if from type admin, we will intent to update DB activity
                    Intent intent = new Intent(btnUpdateDb.getContext(), UpdateDBActivity.class);
                    startActivity(intent);
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
        // if we signing out, we want to start the application
        Intent intent = new Intent(super.getActivity(), Login.class);
        startActivity(intent);
        super.getActivity().finish();
     }
}