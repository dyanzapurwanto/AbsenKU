package com.example.absenku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class mhsHome extends AppCompatActivity {
TextView txtNIM;
String NIM;
Button scan;
Button mhsLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs_home);
        Intent a = getIntent();
        NIM = a.getStringExtra("NIM");
        txtNIM = (TextView)findViewById(R.id.txtNIM);
        txtNIM.setText(NIM);

        scan = (Button)findViewById(R.id.scanQR);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(mhsHome.this,ScanActivity.class);
                startActivity(a);
            }
        });

        mhsLogout = (Button)findViewById(R.id.mhsLogout);
        mhsLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(mhsHome.this,LoginActivity.class);
                startActivity(b);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
