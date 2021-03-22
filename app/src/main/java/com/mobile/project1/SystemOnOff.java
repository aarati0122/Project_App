package com.mobile.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class SystemOnOff extends AppCompatActivity {
    Button On ,Off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_on_off);

        On=(Button)findViewById(R.id.on);
        Off=(Button)findViewById(R.id.off);

    }
}
