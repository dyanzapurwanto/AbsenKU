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
        Intent a = getIntent();
        NIDN = a.getStringExtra("NIDN");
        txtNIDN = (TextView)findViewById(R.id.txtNIDN);
        txtNIDN.setText(NIDN);

        CreateQR = (Button)findViewById(R.id.generateQR);

        CreateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(dsnHome.this,generateQR.class);
                b.putExtra("NIDN",NIDN);
                startActivity(b);
            }
        });

        dsnLogout = (Button)findViewById(R.id.dsnLogout);

        dsnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(c);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
