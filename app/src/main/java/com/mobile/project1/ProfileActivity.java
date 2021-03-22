package com.mobile.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

  private TextView ProfileName,ProfileEmail,ProfileAddress,ProfileMobile,ProfilePassword;
  private  ImageView imageView,emailimage,addressimage,phoneimage;
  private Button ProfileUpdate,changepassword;

  private FirebaseAuth auth;
  private FirebaseDatabase firebaseDatabase;
  private FirebaseDatabase database;
  private FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ProfileName=(TextView)findViewById(R.id.tvProfileName);
        ProfileEmail=(TextView)findViewById(R.id.tvProfileEmail);
        ProfileAddress=(TextView)findViewById(R.id.tvProfileAddress);
        ProfileMobile=(TextView)findViewById(R.id.tvProfileContact);
       // ProfilePassword=(TextView)findViewById(R.id.tvProfilePassword);

        ProfileUpdate=(Button)findViewById(R.id.btnProfileUpdate);
        //changepassword=(Button)findViewById(R.id.btnChangePassword);

        imageView=(ImageView)findViewById(R.id.ivProfilePic);//profile pic

        emailimage=(ImageView)findViewById(R.id.email_image);
        addressimage=(ImageView)findViewById(R.id.address_image);
        phoneimage=(ImageView)findViewById(R.id.phone_image);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference(auth.getUid());

        StorageReference storageReference=firebaseStorage.getReference();
        storageReference.child(auth.getUid()).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //imageView.setImageURI(uri);
                Picasso.get().load(uri).fit().centerCrop().into(imageView);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Database database=dataSnapshot.getValue(Database.class);
                ProfileName.setText(database.getFullname());
                ProfileEmail.setText(database.getEmail());
                ProfileAddress.setText(database.getAddress());
                ProfileMobile.setText(database.getMobile());
                //ProfilePassword.setText(database.getPassword());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getCode(),Toast.LENGTH_SHORT).show();
            }
        });

        ProfileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this,UpdateProfile.class));
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
