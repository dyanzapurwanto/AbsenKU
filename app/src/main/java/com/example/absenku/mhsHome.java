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

        final SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(this);

        NIM = sharedPrefManager.getSPID();
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
                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(mhsHome.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
