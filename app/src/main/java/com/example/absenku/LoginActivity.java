package com.example.absenku;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    AppCompatRadioButton radio_mhs,radio_dsn;
    String usertype="mhs";
    EditText username;
    EditText password;
    Button login;
    TextView txtUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        radio_mhs = (AppCompatRadioButton) findViewById(R.id.radio_mhs);
        radio_dsn = (AppCompatRadioButton) findViewById(R.id.radio_dsn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        txtUser = (TextView) findViewById(R.id.txtUser);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("mhs"))
                {
                    login_mhs();
                }
                else if(usertype.equals("dsn"))
                {
                    login_dsn();
                }
            }
        });

    }
    public void onRadioButtonClicked(View view){
        boolean isSelected = ((AppCompatRadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.radio_mhs:
                if(isSelected){
                    radio_mhs.setTextColor(Color.BLACK);
                    radio_dsn.setTextColor(Color.WHITE);
                    usertype = "mhs";
                    username.setHint("NIM Mahasiswa");
                    txtUser.setText("NIM");
                }
                break;
            case R.id.radio_dsn:
                if(isSelected){
                    radio_mhs.setTextColor(Color.WHITE);
                    radio_dsn.setTextColor(Color.BLACK);
                    usertype = "dsn";
                    username.setHint("NIDN Dosen");
                    txtUser.setText("NIDN");
                }
                break;
        }
    }
    public void login_mhs(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://siakads.000webhostapp.com/login_mhs.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("invalid user")){
                            Toast.makeText(getApplicationContext(), "Wrong NIM or Password", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.equals(username.getText().toString())){
                            Intent a = new Intent(LoginActivity.this,mhsHome.class);
                            a.putExtra("NIM",response);
                            startActivity(a);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("username",username.getText().toString());
                params.put("password",password.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void login_dsn(){
        StringRequest request = new StringRequest(Request.Method.POST, "https://siakads.000webhostapp.com/login_dsn.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("invalid user")){
                            Toast.makeText(getApplicationContext(), "Wrong NIDN or Password", Toast.LENGTH_SHORT).show();
                        }
                        else if(response.equals(username.getText().toString())){
                            Intent a = new Intent(LoginActivity.this,dsnHome.class);
                            a.putExtra("NIDN",response);
                            startActivity(a);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("username",username.getText().toString());
                params.put("password",password.getText().toString());
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
