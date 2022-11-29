package com.example.ffbf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ffbf.Models.ActiveSession;
import com.example.ffbf.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity{

    //declaring the impostors
    TextView registerAccount;
    EditText userEmail, userPassword;
    Button loginBtn;

    // Create Database Reference

    FirebaseDatabase dbMain;
    DatabaseReference dbRef;
    Query dbQry;
    //Database connection
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "Mainactivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        //Imposters links
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_pass);
        registerAccount = findViewById(R.id.register_account);
        loginBtn = findViewById(R.id.loginBtn);

        // Database connection

        mAuth = FirebaseAuth.getInstance();
        dbMain = FirebaseDatabase.getInstance();
        dbRef = dbMain.getReference("message");
        dbRef.setValue("Welcome user");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null){
                    //user signed in
                    Log.d(TAG, "User signed in");
                }
                else{
                    //user signed out
                    Log.d(TAG, "User signed out");
                }
            }
        };

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailString = userEmail.getText().toString();
                String pwd = userPassword.getText().toString();
                if (!emailString.equals("") && !pwd.equals("")){
                    //singing the user in with email and password, checking if the records are matching with the
                    //ones on the firebase authentication records
                    mAuth.signInWithEmailAndPassword(emailString,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //this statement will tell what the app should do if the checks above

                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                            //in case of completion this statement will check which user is logged in
                            else{
                                Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_SHORT).show();

                                Query dbrefq = FirebaseDatabase.getInstance().getReference("users").orderByChild("id").equalTo(mAuth.getUid());
                                dbrefq.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dts: snapshot.getChildren())
                                        {
                                            ActiveSession.OnSession.user = dts.getValue(User.class);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                //once the login gone all the way through, the next step in the app
                                //is going to be the homepage, where you can find the 3 submenus of
                                //all the eateries
                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        registerAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}