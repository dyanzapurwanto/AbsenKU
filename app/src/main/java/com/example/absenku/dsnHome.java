package com.example.absenku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class dsnHome extends AppCompatActivity {
    TextView txtNIDN;
    String NIDN;
    Button CreateQR;
    Button dsnLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsn_home);



    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
