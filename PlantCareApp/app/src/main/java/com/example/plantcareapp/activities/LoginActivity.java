package com.example.plantcareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.plantcareapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity for signing in existing users using Firebase Authentication
 */
public class LoginActivity extends AppCompatActivity {

    private TextView registerButton;
    private EditText editTextEmail, editTextPassword;
    private Button loginButton;
    private FirebaseAuth auth;

    /**
     * Executes when activity is created - retrieves items and sets relevant listeners
     * @param savedInstanceState bundle object passed to onCreate
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        loginButton=findViewById(R.id.login_button);
        registerButton=findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    /**
     * Method to login users
     * Firstly validates the input and then logs user in
     */
    private void loginUser(){
        String email, password;
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());

        if(email.isEmpty()){
            editTextEmail.setError("Email required");
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password required");
            return;
        }

        //Sign in firebase user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success - open home activity and display toast message
                            Toast.makeText(LoginActivity.this, "Login Successful",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            // Sign in failed - display toast message
                            Toast.makeText(LoginActivity.this, "Login failed. Try again",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}