package com.example.plantcareapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {


    FirebaseAuth auth;
    FirebaseUser user;
    Button logoutButton;
    TextView userEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        logoutButton = v.findViewById(R.id.logout);
        userEmail = v.findViewById(R.id.userEmail);

        userEmail.setText(user.getEmail());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}