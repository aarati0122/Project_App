package com.mobile.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class UpdateProfile extends AppCompatActivity {

    private EditText newUsername,newEmail,newMobile,newAddress,newPassword;
    private Button Update;
   private ImageView profileimage;
   private FirebaseAuth auth;
   private FirebaseDatabase firebaseDatabase;
   String fullname,mobile,address,email,password;

    private StorageReference storageReference;
    private static int PICK_IMAGE = 123;
    private FirebaseStorage firebaseStorage;
    //private StorageReference storageReference;
    Uri imagePath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData()!=null)
        {
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imagePath);
                profileimage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        newUsername=(EditText) findViewById(R.id.userupdate);
        newEmail=(EditText) findViewById(R.id.emailupdate);
        newMobile=(EditText) findViewById(R.id.mobileupdate);
        newAddress=(EditText)findViewById(R.id.addressupdate);
        newPassword=(EditText)findViewById(R.id.passwordupdate);
        Update=(Button)findViewById(R.id.update);

        profileimage=(ImageView) findViewById(R.id.ivProfileUpadate);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();


        final DatabaseReference databaseReference = firebaseDatabase.getReference(auth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Database database=dataSnapshot.getValue(Database.class);
                newUsername.setText(database.getFullname());
                newEmail.setText(database.getEmail());
                newMobile.setText(database.getMobile());
                newAddress.setText(database.getAddress());
                newPassword.setText(database.getPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UpdateProfile.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        final StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(auth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //imageView.setImageURI(uri);
                Picasso.get().load(uri).fit().centerCrop().into(profileimage);
            }
        });



        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname= newUsername.getText().toString();
                email = newEmail.getText().toString();
                mobile = newMobile.getText().toString();
                address= newAddress.getText().toString();
                password=newPassword.getText().toString();
                Database database = new Database(fullname,email,mobile,address,password);
                databaseReference.setValue(database);


                StorageReference imageReference = storageReference.child(auth.getUid()).child("Images").child("Profile Pic");//user id/Image/Profile Pic
                UploadTask uploadTask = imageReference.putFile(imagePath);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateProfile.this, "UnSuccessfully upload" , Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(UpdateProfile.this, "Successfully upload" , Toast.LENGTH_SHORT).show();
                    }
                });


                finish();
            }
        });

        profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");  //application/* audio/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"select image"),PICK_IMAGE);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
