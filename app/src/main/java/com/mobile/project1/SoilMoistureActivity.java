package com.mobile.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SoilMoistureActivity extends AppCompatActivity {

    private ImageView imsge_soil;
     TextView moisture;
     Button onbutton,offbutton,Set;
     EditText setValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soil_moisture);
        imsge_soil=(ImageView) findViewById(R.id.imagesoil_moisture);
        moisture=(TextView)findViewById(R.id.soil);
        onbutton=(Button)findViewById(R.id.onbutton);
        offbutton=(Button)findViewById(R.id.offbutton);
        setValue=(EditText)findViewById(R.id.setvalue);
        Set=(Button)findViewById(R.id.set);

        Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Moisture_Threshold_value_set");
            }
        });

        onbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Moisture_STATUS");
                myRef.setValue(1);
            }
        });

        offbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Moisture_STATUS");
                myRef.setValue(0);
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
