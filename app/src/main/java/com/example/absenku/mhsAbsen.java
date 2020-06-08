package com.example.absenku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class mhsAbsen extends AppCompatActivity {

    String nmKelas;
    String kdKelas;
    String pertemuan;
    String tabel_absensi;
    Button absen;

    TextView ketKelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs_absen);
        Intent a = getIntent();
        String kode_absen = a.getStringExtra("kode_absen");
        ketKelas = (TextView)findViewById(R.id.ket_kelas);
        absen = (Button)findViewById(R.id.absen);
        absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAbsen(tabel_absensi,pertemuan);
            }
        });
        getData(kode_absen);
    }

    private void doAbsen(final String tblAbsen,final String minggu){
        final String url = "http://192.168.43.27/absenku/mhs_absen.php";
        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        final String NIM = sharedPrefManager.getSPID();
        final ProgressDialog progressDialog = new ProgressDialog(mhsAbsen.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("berhasil"))
                {
                    Toast.makeText(getApplicationContext(),"Absen berhasil",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Absen Gagal",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                Intent a = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"NOT CONNECTED",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("nim",NIM);
                params.put("tabel_absen",tblAbsen);
                params.put("pertemuan",minggu);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);


    }

    private void getData(final String kdAbsen){
        final String url = "http://192.168.43.27/absenku/mhs_retrieve_absen.php";
        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        final String NIM = sharedPrefManager.getSPID();
        final ProgressDialog progressDialog = new ProgressDialog(mhsAbsen.this);
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    nmKelas = jsonObject.getString("nama_kelas");
                    kdKelas = jsonObject.getString("kode_kelas");
                    pertemuan = jsonObject.getString("minggu");
                    tabel_absensi = jsonObject.getString("tabel_absensi");
                    ketKelas.setText("Current Active Class: \n"+nmKelas+
                            "\nPertemuan: "+pertemuan+
                            "\nKode kelas: "+kdKelas);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"ERROR!",Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"NOT CONNECTED",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<>();
                params.put("kode_absen",kdAbsen);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}
