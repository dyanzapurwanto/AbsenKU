package com.example.absenku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class generateQR extends AppCompatActivity {
ImageView QR;
Button generate;
String code;
TextView current;

String[] nama_kelas;
String[] kode_kelas;
String[] pilihan;
ArrayAdapter adapter;
ArrayAdapter adapter2;
AutoCompleteTextView pilihKelas;
AutoCompleteTextView pilihMinggu;
String nmKelas;
String kdKelas;
String pertemuan;

ImageButton backFromQR;
private List<Kelas> arrayKelas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr);
        String[] minggu = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14"};
        adapter2 = new ArrayAdapter(generateQR.this, android.R.layout.select_dialog_item, minggu);
        pilihMinggu= (AutoCompleteTextView)findViewById(R.id.pertemuan);
        pilihMinggu.setAdapter(adapter2);
        arrayKelas = new ArrayList<>();
        QR = (ImageView)findViewById(R.id.QRCode);
        generate = (Button)findViewById(R.id.generate);
        code ="NONE";
        current = (TextView)findViewById(R.id.current);
        generate.setEnabled(false);
        generate.setBackground(getDrawable(R.drawable.capsule2dark));
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            QR.setImageBitmap(bitmap);
            current.setText("current active class: "+code);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generate(kdKelas);
            }
        });
        pilihKelas = (AutoCompleteTextView)findViewById(R.id.pilihKelas);
        getData();
        pilihKelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihKelas.showDropDown();
            }
        });
        pilihMinggu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pilihKelas.getText().toString().matches(""))
                {
                    pilihMinggu.showDropDown();
                }
            }
        });
        pilihKelas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i;
                for (i=0;i<pilihan.length;i++) {
                    if (pilihan[i].matches(pilihKelas.getText().toString()))
                    {
                        kdKelas = kode_kelas[i];
                    }
                }
            }
        });

        pilihMinggu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                generate.setEnabled(true);
                generate.setBackground(getDrawable(R.drawable.capsule2));
            }
        });

        backFromQR = findViewById(R.id.back_from_qr);
        backFromQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    public void generate(final String classcode){

        final String url = "http://192.168.43.27/absenku/dsn_set_qr.php";
        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        final String NIDN = sharedPrefManager.getSPID();
        final ProgressDialog progressDialog = new ProgressDialog(generateQR.this);
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
                        code = jsonObject.getString("kode_absensi");
                        current.setText("Current Active Class: "+nmKelas+
                                "\nPertemuan: "+pilihMinggu.getText().toString()+
                                "\nKode kelas: "+classcode);
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(code, BarcodeFormat.QR_CODE,230,230);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            QR.setImageBitmap(bitmap);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
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
                    params.put("nidn",NIDN);
                    params.put("minggu","pertemuan"+pilihMinggu.getText().toString());
                    params.put("kode_kelas",classcode);
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        Toast.makeText(getApplicationContext(),"Code Generated !",Toast.LENGTH_SHORT).show();
    }

    private void getData() {
        final String url = "http://192.168.43.27/absenku/dsn_retrieve_upclass.php";
        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        final String NIDN = sharedPrefManager.getSPID();
        final ProgressDialog progressDialog = new ProgressDialog(generateQR.this);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String dow = "default";
        switch (day) {
            case Calendar.SUNDAY:
                dow = "Minggu";
                break;
            case Calendar.MONDAY:
                dow = "Senin";// Current day is Monday
                break;
            case Calendar.TUESDAY:
                dow = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                dow = "Rabu";
                break;
            case Calendar.THURSDAY:
                dow = "Kamis";
                break;
            case Calendar.FRIDAY:
                dow = "Jumat";
                break;
            case Calendar.SATURDAY:
                dow = "Sabtu";
                break;
        }
        final String hari = dow;
        progressDialog.setMessage("Loading");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    if(array.length()<=0)
                    {
                        Kelas kelas = new Kelas();
                        kelas.setKode_kelas("NONE");
                        kelas.setNama_kelas("No Classes");
                        arrayKelas.add(kelas);
                    }
                    else
                    {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            Kelas kelas = new Kelas();
                            kelas.setNama_kelas(jsonObject.getString("nama_kelas"));
                            kelas.setKode_kelas(jsonObject.getString("kode_kelas"));

                            arrayKelas.add(kelas);
                        }
                    }
                    kode_kelas = new String[arrayKelas.size()];
                    nama_kelas = new String[arrayKelas.size()];
                    pilihan = new String[arrayKelas.size()];
                    for(int i=0;i<arrayKelas.size();i++)
                    {
                        kode_kelas[i] = arrayKelas.get(i).getKode_kelas();
                        nama_kelas[i] = arrayKelas.get(i).getNama_kelas();
                        pilihan[i] = nama_kelas[i]+" - "+kode_kelas[i];
                    }
                    adapter = new ArrayAdapter<String>
                            (generateQR.this, android.R.layout.select_dialog_item, pilihan);
                    pilihKelas.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
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
                params.put("nidn",NIDN);
                params.put("hari",hari);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}
