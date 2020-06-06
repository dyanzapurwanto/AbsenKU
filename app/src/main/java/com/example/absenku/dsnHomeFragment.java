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

public class dsnHomeFragment extends Fragment {
    TextView txtNIDN;
    String NIDN;
    String nidnnodot;
    Button CreateQR;
    Button dsnLogout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dsn_home,container,false);
        final SharedPrefManager sharedPrefManager;
        sharedPrefManager = new SharedPrefManager(getActivity().getApplicationContext());

        NIDN = sharedPrefManager.getSPID();
        nidnnodot = NIDN.replaceAll("[.]", "");
        txtNIDN = (TextView)view.findViewById(R.id.txtNIDN);
        txtNIDN.setText(NIDN);

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
        return view;
    }
}
