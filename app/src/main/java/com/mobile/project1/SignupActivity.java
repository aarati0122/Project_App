package com.mobile.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
//import android.support.annotation.NonNull;
//import androidx.support.v7.app.AppCompatActivity;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
//import com.firebaseloginapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.BitSet;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputFullname,inputAddress,inputMobile;     //hit option + enter if you on mac , for windows hit ctrl + enter
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private ImageView userprofilepic;
    Database database;
    DatabaseReference databaseReference;
    String email,fullname,address,mobile,password;

    private static int PICK_IMAGE = 123;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    Uri imagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                userprofilepic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setupUIViews();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference();
       // StorageReference myref =storageReference.child(auth.getUid()).getParent().getRoot();


        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputFullname = (EditText) findViewById(R.id.fullname);
        inputAddress = (EditText) findViewById(R.id.address);
        inputMobile= (EditText) findViewById(R.id.mobile);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        userprofilepic=(ImageView) findViewById(R.id.ivProfilePic);


        database = new Database();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Database");

        userprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");  //application/* audio/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE);
            }
        });



        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 fullname = inputFullname.getText().toString().trim();
                 email = inputEmail.getText().toString().trim();
                 password = inputPassword.getText().toString().trim();
                 address = inputAddress.getText().toString().trim();
                 mobile = inputMobile.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {

                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    sendUserData();
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
              /*  database.setFullname(inputFullname.getText().toString().trim());
                database.setEmail(inputEmail.getText().toString().trim());
                database.setPassword(inputPassword.getText().toString().trim());
                database.setAddress(inputAddress.getText().toString().trim());
                database.setMobile(inputMobile.getText().toString().trim());
                // databaseReference.child(String.valueOf(maxid+1)).push().setValue(database);
                databaseReference.push().setValue(database);
                Toast.makeText(SignupActivity.this, "data inserted", Toast.LENGTH_SHORT).show();*/

            }
        });
    }


    private void setupUIViews() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    public void updateUI(FirebaseUser currentUser) {
        String keyid = databaseReference.push().getKey();
        databaseReference.child(keyid).setValue(database); //adding user info to database
       //Intent loginIntent = new Intent(this, ProfileActivity.class);
       //startActivity(loginIntent);
    }
   private  void  sendUserData(){
       FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
       databaseReference=firebaseDatabase.getReference(auth.getUid());
       StorageReference imageReference = storageReference.child(auth.getUid()).child("Images").child("Profile Pic");//user id/Image/Profile Pic
       UploadTask uploadTask = imageReference.putFile(imagePath);
       uploadTask.addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(SignupActivity.this, "UnSuccessfully upload" , Toast.LENGTH_SHORT).show();
           }
       }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

               Toast.makeText(SignupActivity.this, "Successfully upload" , Toast.LENGTH_SHORT).show();
           }
       });
       Database database=new Database(fullname,email,password,address,mobile);
       databaseReference.setValue(database);
    }
}
