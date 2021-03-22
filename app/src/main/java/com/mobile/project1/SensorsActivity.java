package com.mobile.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class SensorsActivity extends AppCompatActivity {

    private Button temp,soil,bolt_data;
    private TextView status;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors);

        bolt_data=(Button)findViewById(R.id.bolt_link);
        temp=(Button)findViewById(R.id.tempbtn);
        soil=(Button)findViewById(R.id.soilbtn);
        status=(TextView)findViewById(R.id.status);
        radioButton=(RadioButton)findViewById(R.id.radioButton);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorsActivity.this,SoilMoistureActivity.class));
            }
        });

        soil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorsActivity.this,TempActivity.class));
            }
        });

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SensorsActivity.this,SystemOnOff.class));
            }
        });



    }

    public void bolt_link(View view){
        Intent boltInent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cloud.boltiot.com/login?next=%2Fhome%2Fhttps://cloud.boltiot.com/login?next=%2Fhome%2F"));
        startActivity(boltInent);
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
