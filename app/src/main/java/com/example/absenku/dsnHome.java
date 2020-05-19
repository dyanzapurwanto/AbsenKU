package com.example.absenku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class dsnHome extends AppCompatActivity {
    TextView txtNIDN;
    String NIDN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsn_home);
        Intent a = getIntent();
        NIDN = a.getStringExtra("NIDN");
        txtNIDN = (TextView)findViewById(R.id.txtNIDN);
        txtNIDN.setText(NIDN);
    }
}
