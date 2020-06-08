package com.example.absenku;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbsensiFragment extends Fragment {
    private RecyclerView absenList;
    private List<Kelas> arrayAbsen;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mhs_absensi,container,false);
        absenList =  view.findViewById(R.id.list_absen);
        arrayAbsen = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(),LinearLayoutManager.VERTICAL,false);
        absenList.setLayoutManager(layoutManager);
        adapter = new AbsenAdapter(getActivity().getApplicationContext(), arrayAbsen);
        absenList.setAdapter(adapter);

        getData();
        return view;
    }

    private void getData() {
        final String url = "http://192.168.43.27/absenku/mhs_retrieve_progress.php";
        SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());
        final String NIM = sharedPrefManager.getSPID();
        final String nimnodot = sharedPrefManager.getSPIDNODOT().toLowerCase();
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
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
                        kelas.setNama_kelas(jsonObject.getString("nama_kelas"));
                        kelas.setKode_kelas(jsonObject.getString("kode_kelas"));
                        kelas.setAbsen(jsonObject.getString("total_absen"));
                        arrayAbsen.add(kelas);
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
                params.put("nimnodot",nimnodot);
                params.put("nim",NIM);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity().getApplicationContext()).add(stringRequest);
    }
}
