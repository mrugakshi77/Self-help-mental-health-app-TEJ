package com.example.mentalhealth;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mentalhealth.patient_navbar.patient_doclist.PatientDocListFragment;
import com.example.mentalhealth.patient_navbar.patient_home.PatientHomeFragment;
import com.example.mentalhealth.patient_navbar.myaccount.MyAccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Patient_feed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_feed);

        BottomNavigationView navView = findViewById(R.id.nav_view_patient);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_homeP, R.id.navigation_myaccountP)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_patient);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.navigation_homeP:
                        fragment = new PatientHomeFragment();
                        break;
                    case R.id.navigation_myaccountP:
                        fragment = new MyAccountFragment();
                        break;
                    case R.id.navigation_appointmentsP:
                        fragment = new PatientDocListFragment();
                        break;
                }
                loadFragment(fragment);
                return;
            }
        });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_patient, fragment).commit();
            return true;
        } else
            return false;
    }

}
