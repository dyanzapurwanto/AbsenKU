package com.example.absenku;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class mhsHomeFragment extends Fragment {
    TextView txtNIM;
    String NIM;
    Button scan;
    Button mhsLogout;
    String nimnodot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        final SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());

        NIM = sharedPrefManager.getSPID();
        nimnodot = NIM.replaceAll("[.]", "");
        sharedPrefManager.saveSPString(SharedPrefManager.SP_IDNODOT,nimnodot);
        txtNIM = (TextView)view.findViewById(R.id.txtNIM);
        txtNIM.setText(NIM);

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
        return view;
    }
}

