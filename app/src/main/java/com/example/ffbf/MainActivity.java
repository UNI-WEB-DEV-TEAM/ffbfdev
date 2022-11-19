package com.example.ffbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView registerAccount;
    private EditText userEmail, userPassword;
    private Button loginBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Declare Views in the Login/Main Activity
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        registerAccount = findViewById(R.id.registerAccount);
        registerAccount.setOnClickListener(this);
        loginBtn = findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        // Database connection

        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginBtn:
                user_login();
                break;
            case R.id.registerAccount:
                startActivity(new Intent(this, Registration.class));
        }
    }

    private void user_login() {
        String email = userEmail.getText().toString().trim();
        String pass = userPassword.getText().toString().trim();

        // Check the password field for user input
        if(pass.isEmpty()) {
            userPassword.setError("Password is required");
            userPassword.requestFocus();
            return;
        }
        // Check the password length
        if(pass.length() < 6){
            userPassword.setError("Password has to contain a minimum fo 6 characters!");
            userPassword.requestFocus();
            return;
        }

        if(email.isEmpty()){
            userEmail.setError("Email is required!");
            userEmail.requestFocus();
            return;
        }
        //Validate E-mail form match
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.setError("Please provide a valid email address!");
            userEmail.requestFocus();
            return;
        }
        // User signing process after credential verification
        mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                startActivity(new Intent(MainActivity.this, Profile.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
}