package com.example.plantcareapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.plantcareapp.R;
import com.example.plantcareapp.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Profile fragment
 * Used for viewing and editing user account details
 */
public class ProfileFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private Button logoutButton, deleteButton, resetButton;
    private TextView userEmail, resetDropdown;
    private EditText editTextPassword, editTextPassword2;

    // Required public empty constructor
    public ProfileFragment(){
    }

    /**
     * Create new instance of ProfileFragment
     * @return a new instance of ProfileFragment
     */
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    /**
     * Executes when fragment is created
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate layout once fragment has been created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile,container,false);
        return v;
    }

    /**
     * Retrieves items and sets listeners once view has been created
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        logoutButton = view.findViewById(R.id.logout);
        resetButton=view.findViewById(R.id.resetButton);
        userEmail = view.findViewById(R.id.userEmail);
        deleteButton = view.findViewById(R.id.delete);
        editTextPassword = view.findViewById(R.id.resetPassword);
        editTextPassword2=view.findViewById(R.id.resetPassword2);
        userEmail.setText(user.getEmail());
        resetDropdown=view.findViewById(R.id.resetDropdown);

        resetDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReset();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
            }
        });
    }

    /**
     * Method to show/hide the dropdown for resetting password
     */
    private void showReset(){
        if(editTextPassword.getVisibility()!=View.VISIBLE){
            editTextPassword.setVisibility(View.VISIBLE);
            editTextPassword2.setVisibility(View.VISIBLE);
            resetButton.setVisibility(View.VISIBLE);
            resetDropdown.setText("Click to hide");
        }
        else if(editTextPassword.getVisibility()==View.VISIBLE){
            editTextPassword.setVisibility(View.GONE);
            editTextPassword2.setVisibility(View.GONE);
            resetButton.setVisibility(View.GONE);
            resetDropdown.setText("Click to reset password");
        }
    }

    /**
     * Method to reset current user password
     * Firstly validates input and then resets user password
     */
    private void resetPassword(){
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

        // Reset user password in Firebase
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Reset password successful - clear text and display toast message
                            Toast.makeText(getActivity(), "Password successfully reset.",
                                    Toast.LENGTH_LONG).show();
                            editTextPassword.getText().clear();
                            editTextPassword2.getText().clear();
                            showReset();
                        }
                        else{
                            // Reset password failed - display toast message
                            Toast.makeText(getActivity(), "Error resetting. Try again later.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Method to logout current user
     */
    private void logoutUser(){
        auth.signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }

    /**
     * Method to delete current user
     * Firstly displays an alert dialog to confirm deletion, before deleting user account
     */
    private void deleteUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Warning");
        alertDialogBuilder.setMessage("Account will be permanently deleted and all information will be irretrievable. Please confirm.")
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // On clicking confirm, delete user account
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Delete account successful - display toast message and return to main activity
                                            Toast.makeText(getActivity(), "Account successfully deleted.",
                                                    Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }else{
                                            // Delete account failed - display toast message.
                                            Toast.makeText(getActivity(), "Error deleting. Try again later.",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // On clicking cancel, close dialog
                        dialogInterface.cancel();
                    }
                });
        alertDialogBuilder.create().show();
    }
}