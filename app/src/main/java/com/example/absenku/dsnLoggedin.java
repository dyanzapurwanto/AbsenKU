package com.example.absenku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class dsnLoggedin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActionBarTitle("Home");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsn_loggedin);

        BottomNavigationView bottomnav = findViewById(R.id.dsn_bottom_nav);
        bottomnav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.dsn_fragment_container,
                new dsnHomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.dsn_nav_home:
                            setActionBarTitle("Home");
                            selectedFragment = new dsnHomeFragment();
                            break;
                        case R.id.dsn_nav_kelas:
                            setActionBarTitle("Kelas");
                            selectedFragment = new dsnKelasFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.dsn_fragment_container,
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
