package com.example.plantcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.plantcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 * Activity for registering new users using Firebase Authentication
 */
public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextPassword2;
    private Button regButton;
    private FirebaseAuth auth;

    /**
     * Executes when activity is created - retrieves items and sets register listener
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextPassword2 = findViewById(R.id.password2);
        regButton=findViewById(R.id.register_button);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });
    }

    /**
     * Method to register new users
     * Firstly validates the input and then creates new user
     */
    private void registerNewUser(){
        String email, password, password2;
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());
        password2 = String.valueOf(editTextPassword2.getText());

        if(email.isEmpty()){
            editTextEmail.setError("Email required");
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Valid email required");
            return;
        }
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

        // Create new firebase user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration and sign in in success - open home activity and display toast message
                            Toast.makeText(RegisterActivity.this, "Registration Successful",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        } else {
                            // Registration failed - display toast message
                            Toast.makeText(RegisterActivity.this, "Registration failed. Try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}