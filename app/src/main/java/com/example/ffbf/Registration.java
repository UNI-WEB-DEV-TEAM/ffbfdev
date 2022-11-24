package com.example.ffbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ffbf.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText regName, regEmail, regPass;
    private EditText userAge;
    private Button registerUser;
    ImageView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        //Add Activity Elements
        mAuth = FirebaseAuth.getInstance();
        regName = (EditText) findViewById(R.id.regName);
        regEmail = (EditText) findViewById(R.id.regEmail);
        regPass = (EditText) findViewById(R.id.regPass);
        userAge =  (EditText) findViewById(R.id.userAge);
        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);
        backToLogin = (ImageView) findViewById(R.id.backToLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backToLogin:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                userRegistration();
                break;
        }
    }

    private void userRegistration() {

        String email = regEmail.getText().toString().trim();
        String password = regPass.getText().toString().trim();
        String fullName = regName.getText().toString().trim();
        String age = userAge.getText().toString().trim();

        if(fullName.isEmpty()){
            regName.setError("Full name is required!");
            regName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            userAge.setError("Age is required!");
            userAge.requestFocus();
            return;
        }
        if(email.isEmpty()){
            regEmail.setError("Email is required!");
            regEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            regEmail.setError("Please enter a valid email address!");
            regEmail.requestFocus();
            return;
        }
        if(password.isEmpty()){
            regPass.setError("Password is required!");
            regPass.requestFocus();
            return;
        }
        if(password.length() < 6){
            regPass.setError("Password must contain at least 6 characters!");
            regPass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName, age, email);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(Registration.this, "User has been successfully registered!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Registration.this, MainActivity.class));
                                            } else {
                                                Toast.makeText(Registration.this, "Registration failed!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}