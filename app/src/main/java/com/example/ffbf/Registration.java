package com.example.ffbf;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ffbf.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class Registration extends AppCompatActivity {

    //Imposters declaration
    Button register, addHero;
    EditText fs, mail, pW, confirmPw, userNickname;
    ImageView picture;
    Uri url;

    // Link with database, storage, authentication and database reference
    FirebaseDatabase database;
    DatabaseReference dbref;
    StorageReference storeRef;
    FirebaseAuth mAuth;

    public static final int REQUEST = 1;
    public static String URL;

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //this statement will check if the user picked an image or not
        if (requestCode == REQUEST && resultCode == RESULT_OK && data.getData() != null){

            url = data.getData();
            Picasso.get().load(data.getData()).fit().into(picture);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_registration);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbref = database.getReference("users");
        storeRef = FirebaseStorage.getInstance().getReference("profile_hero");

        addHero = findViewById(R.id.reg_user_hero);
        register = findViewById(R.id.reg_btn);
        fs = findViewById(R.id.reg_ns);
        mail = findViewById(R.id.reg_email);
        pW = findViewById(R.id.reg_pass);
        confirmPw = findViewById(R.id.reg_confirm_pass);
        userNickname = findViewById(R.id.reg_username);

        addHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowDialogBox();

            }
        });

        // Register button call
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the input from the edit text fields
                String name = fs.getText().toString();
                String password = pW.getText().toString();
                String email = mail.getText().toString();

                //checking if the entered text is empty or not
                if (!TextUtils.isEmpty(name)
                        && !TextUtils.isEmpty(password)
                        && pW.getText().toString().equalsIgnoreCase(confirmPw.getText().toString())) {
                    //creating a user in the firebase authentication with their provided data
                    mAuth.createUserWithEmailAndPassword(mail.getText().toString(), pW.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //declaring which field should populate which data in firebase
                            User user = new User();
                            user.setFullName(fs.getText().toString());
                            user.setId(mAuth.getUid());
                            user.setUserType("User");
                            user.setEmail(mail.getText().toString());
                            user.setAvatar(URL);
                            user.setUser_name(userNickname.getText().toString());

                            //setting value in the database reference
                            dbref.child(user.getId()).setValue(user);

                            //once the registration is completed the app will sign the user out
                            //so they should be taken back to the login screen
                            mAuth.signOut();
                            //Toast.makeText(Register.this, mAuth.getUid().toString(), Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(Registration.this, MainActivity.class);
                            startActivity(i);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Registration.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }



        });

    }

    public void ShowDialogBox(){
        //creating the dialog box, linking it to a layout
        Dialog dialog = new Dialog(Registration.this);
        dialog.setContentView(R.layout.profile_picture_upload);
        dialog.show();

        //creating and linking the impostors inside the dialog box
        Button upload = dialog.findViewById(R.id.btn_upload_img);
        picture = dialog.findViewById(R.id.upload_icon);

        //setting up the picture button
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting up an intent, to open the gallery on the phone
                //and to only show the pictures from the gallery
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, REQUEST);
            }
        });

        //setting up the upload button
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting up database reference, telling it where to upload the picture
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference("users");
                String id = dbref.push().getKey();
                final StorageReference reference = storeRef.child(id + "." + getExtension(url));

                reference.putFile(url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            //on success the dialog will dismiss
                            public void onSuccess(Uri uri) {
                                URL = uri.toString();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                reference.delete();
                                Toast.makeText(Registration.this, "Error", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });


    }

    //getExtension method called in upload process
    private String getExtension(Uri path){
        ContentResolver resolver = getContentResolver();
        MimeTypeMap map = MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(path));
    }

}