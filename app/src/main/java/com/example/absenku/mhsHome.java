package com.example.absenku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class mhsHome extends AppCompatActivity {
TextView txtNIM;
String NIM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs_home);
        Intent a = getIntent();
        NIM = a.getStringExtra("NIM");
        txtNIM = (TextView)findViewById(R.id.txtNIM);
        txtNIM.setText(NIM);

    }
}
