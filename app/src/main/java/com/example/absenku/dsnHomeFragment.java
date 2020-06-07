package com.example.absenku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class dsnHomeFragment extends Fragment {
    TextView txtNIDN;
    TextView txtNama;
    String NIDN;
    String nidnnodot;
    Button CreateQR;
    Button dsnLogout;
    String nama;

    private RecyclerView kelasList;
    private List<Kelas> arrayKelas;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dsn_home,container,false);
        final SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        kelasList =  view.findViewById(R.id.dsnUpClass);
        arrayKelas = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        kelasList.setLayoutManager(layoutManager);
        adapter = new upKelasAdapter(getActivity().getApplicationContext(), arrayKelas);
        kelasList.setAdapter(adapter);

        NIDN = sharedPrefManager.getSPID();
        nama  = sharedPrefManager.getSPNama();
        nidnnodot = NIDN.replaceAll("[.]", "");
        txtNIDN = (TextView)view.findViewById(R.id.txtNIDN);
        txtNIDN.setText(NIDN);
        txtNama = (TextView)view.findViewById(R.id.txtNamaDsn);
        txtNama.setText(nama);
        CreateQR = (Button)view.findViewById(R.id.generateQR);

        CreateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent b = new Intent(getActivity().getApplicationContext(),generateQR.class);
                b.putExtra("NIDN",NIDN);
                startActivity(b);
            }
        });

        dsnLogout = (Button)view.findViewById(R.id.dsnLogout);

        dsnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                getActivity().finish();
            }
        });
        getData();
        return view;
    }

    private void getData() {
        final String url = "http://192.168.43.27/absenku/dsn_retrieve_upclass.php";
        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        final String NIDN = sharedPrefManager.getSPID();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        Kelas kelas = new Kelas();
                        String jam_mulai = jsonObject.getString("jam_mulai");
                        String jam_selesai = jsonObject.getString("jam_selesai");
                        kelas.setNama_kelas(jsonObject.getString("nama_kelas"));
                        kelas.setJam_kelas(jam_mulai+" - "+jam_selesai);

                        arrayKelas.add(kelas);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
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
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
        if(hari.matches("Sabtu") || hari.matches("Minggu"))
        {
            Kelas kelas = new Kelas();
            kelas.setJam_kelas("Enjoy ur day");
            kelas.setNama_kelas("No Classes Today");

            arrayKelas.add(kelas);
        }
    }
}
