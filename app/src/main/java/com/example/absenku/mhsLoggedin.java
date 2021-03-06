package com.example.absenku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class mhsLoggedin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActionBarTitle("Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mhs_loggedin);

        BottomNavigationView bottomnav = findViewById(R.id.mhs_bottom_nav);
        bottomnav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.mhs_fragment_container,
                new mhsHomeFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            setActionBarTitle("Home");
                            selectedFragment = new mhsHomeFragment();
                            break;
                        case R.id.nav_kelas:
                            setActionBarTitle("Kelas");
                            selectedFragment = new mhsKelasFragment();
                            break;
                        case R.id.nav_absensi:
                            setActionBarTitle("Absensi");
                            selectedFragment = new AbsensiFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.mhs_fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
