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


public class mhsHomeFragment extends Fragment {
    TextView txtNIM;
    TextView txtNama;
    String nama;
    String NIM;
    Button scan;
    Button mhsLogout;
    String nimnodot;

    private RecyclerView kelasList;
    private List<Kelas> arrayKelas;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mhs_home,container,false);
        final SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());

        kelasList = view.findViewById(R.id.mhsUpClass);
        arrayKelas = new ArrayList<>();
        adapter = new upKelasAdapter(getActivity().getApplicationContext(),arrayKelas);
        kelasList.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        kelasList.setLayoutManager(layoutManager);

        NIM = sharedPrefManager.getSPID();
        nama = sharedPrefManager.getSPNama();
        nimnodot = NIM.replaceAll("[.]", "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_IDNODOT,nimnodot);
        txtNIM = (TextView)view.findViewById(R.id.txtNIM);
        txtNIM.setText(NIM);
        txtNama = (TextView)view.findViewById(R.id.txtNamaMhs);
        txtNama.setText(nama);

        scan = (Button)view.findViewById(R.id.scanQR);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getActivity().getApplicationContext(),ScanActivity.class);
                startActivity(a);
            }
        });

        mhsLogout = (Button)view.findViewById(R.id.mhsLogout);
        mhsLogout.setOnClickListener(new View.OnClickListener() {
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
        final String url = "http://192.168.43.27/absenku/mhs_retrieve_upclass.php";
        final SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        final String nimnodot = sharedPrefManager.getSPIDNODOT();
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
                    JSONObject status = array.getJSONObject(0);
                    if(status.getString("nama_kelas").matches("Tidak ada jadwal"))
                    {
                        Kelas kelas = new Kelas();
                        kelas.setJam_kelas("Enjoy ur day");
                        kelas.setNama_kelas("No Classes Today");

                        arrayKelas.add(kelas);
                    }
                    else
                    {
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            Kelas kelas = new Kelas();
                            String jam_mulai = jsonObject.getString("jam_mulai");
                            String jam_selesai = jsonObject.getString("jam_selesai");
                            kelas.setNama_kelas(jsonObject.getString("nama_kelas"));
                            kelas.setJam_kelas(jam_mulai+" - "+jam_selesai);

                            arrayKelas.add(kelas);
                        }
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
                params.put("nim",nimnodot);
                params.put("hari",hari);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }
}

