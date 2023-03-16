package com.example.plantcareapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {


    FirebaseAuth auth;
    FirebaseUser user;
    Button logoutButton, deleteButton, resetButton;
    TextView userEmail;
    EditText editTextPassword, editTextPassword2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        logoutButton = v.findViewById(R.id.logout);
        resetButton=v.findViewById(R.id.resetButton);
        userEmail = v.findViewById(R.id.userEmail);
        deleteButton = v.findViewById(R.id.delete);
        editTextPassword = v.findViewById(R.id.resetPassword);
        editTextPassword2=v.findViewById(R.id.resetPassword2);


        userEmail.setText(user.getEmail());

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password, password2;
                password = String.valueOf(editTextPassword.getText());
                password2 = String.valueOf(editTextPassword2.getText());
                if(password.isEmpty()){
                    editTextPassword.setError("Password required");
                    return;
                }
                if(!password2.equals(password)){
                    editTextPassword2.setError("Password does not match");
                    return;
                }
                if(password.length()<6){
                    editTextPassword.setError("Password must be 6 characters");
                    return;
                }
                user.updatePassword(password)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Password reset.",
                                            Toast.LENGTH_SHORT).show();
                                    editTextPassword.setText("");
                                    editTextPassword2.setText("");
                                }
                                else{
                                    Toast.makeText(getActivity(), "Error resetting. Try again later.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                alertDialogBuilder.setTitle("Warning");

                alertDialogBuilder.setMessage("Account will be permanently deleted and all information will be unretrievable. Please confirm.")
                        .setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Account deleted.",
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                                    startActivity(intent);
                                                }else{
                                                    Toast.makeText(getActivity(), "Error deleting. Try again later.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                alertDialogBuilder.create().show();
            }
        });
        return v;
    }
}