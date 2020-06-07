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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

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

        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(this);

        if (sharedPrefManager.getSPSudahLogin()){
            if(sharedPrefManager.getSPUserType().matches("dsn"))
            {
                Intent loggedin = new Intent(LoginActivity.this, dsnLoggedin.class);
                startActivity(loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            else if(sharedPrefManager.getSPUserType().matches("mhs"))
            {
                Intent loggedin = new Intent(LoginActivity.this, mhsLoggedin.class);
                startActivity(loggedin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
            finish();
        }

        radio_mhs = (AppCompatRadioButton) findViewById(R.id.radio_mhs);
        radio_dsn = (AppCompatRadioButton) findViewById(R.id.radio_dsn);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.btn_login);
        txtUser = (TextView) findViewById(R.id.txtUser);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEmpty(username) || isEmpty(password))
                {
                    Toast.makeText(getApplicationContext(), "Please input credentials", Toast.LENGTH_SHORT).show();
                }
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
                    radio_mhs.setTextColor(Color.WHITE);
                    radio_dsn.setTextColor(Color.BLACK);
                    usertype = "mhs";
                    username.setHint("NIM Mahasiswa");
                    txtUser.setText("NIM");
                }
                break;
            case R.id.radio_dsn:
                if(isSelected){
                    radio_mhs.setTextColor(Color.BLACK);
                    radio_dsn.setTextColor(Color.WHITE);
                    usertype = "dsn";
                    username.setHint("NIDN Dosen");
                    txtUser.setText("NIDN");
                }
                break;
        }
    }
    public void login_mhs(){
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.27/absenku/login_mhs.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String credential = jsonObject.getString("username");
                            if(credential.equals("invalid")){
                                Toast.makeText(getApplicationContext(), "Wrong NIM or Password", Toast.LENGTH_SHORT).show();
                            }
                            else if(credential.equalsIgnoreCase(username.getText().toString())){
                                SharedPrefManager sharedPrefManager;
                                sharedPrefManager = new SharedPrefManager(LoginActivity.this);
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN,true);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_UserType,"mhs");
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_ID,credential);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA,jsonObject.getString("nama"));
                                Intent a = new Intent(LoginActivity.this,mhsLoggedin.class);
                                startActivity(a);
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Not Connected to Database", Toast.LENGTH_SHORT).show();

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
        StringRequest request = new StringRequest(Request.Method.POST, "http://192.168.43.27/absenku/login_dsn.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String credential = jsonObject.getString("username");
                            if(credential.equals("invalid")){
                                Toast.makeText(getApplicationContext(), "Wrong NIDN or Password", Toast.LENGTH_SHORT).show();
                            }
                            else if(credential.equalsIgnoreCase(username.getText().toString())){
                                SharedPrefManager sharedPrefManager;
                                sharedPrefManager = new SharedPrefManager(LoginActivity.this);
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN,true);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_UserType,"dsn");
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_ID,credential);
                                sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA,jsonObject.getString("nama"));
                                Intent a = new Intent(LoginActivity.this,dsnLoggedin.class);
                                startActivity(a);
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Not Connected to Database", Toast.LENGTH_SHORT).show();

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

    private boolean isEmpty(EditText myeditText) {
        return myeditText.getText().toString().trim().length() == 0;
    }
}
