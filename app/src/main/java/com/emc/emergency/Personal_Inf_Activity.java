package com.emc.emergency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;



import com.emc.emergency.Fragment.fragment_medical_info_page;
import com.emc.emergency.Fragment.fragment_personal_info_page;
import com.emc.emergency.model.Medical_Info;
import com.emc.emergency.model.Personal_Infomation;

public class Personal_Inf_Activity extends AppCompatActivity implements fragment_personal_info_page.OnListFragmentInteractionListener,fragment_medical_info_page.OnListFragmentInteractionListener {
    BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        fragment_personal_info_page fragment_personal_info_page = new fragment_personal_info_page();
        getSupportFragmentManager().beginTransaction().add(R.id.content1, fragment_personal_info_page).commit();

        xulyNavigation();
    }

    private void xulyNavigation() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragment_personal_info_page fragment_personal_info_page = new fragment_personal_info_page();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content1, fragment_personal_info_page).commit();
                        break;
                    case R.id.navigation_dashboard:
                        fragment_medical_info_page fragment_medical_info_page = new fragment_medical_info_page();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content1, fragment_medical_info_page).commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onListFragmentInteraction(Medical_Info mItem) {

    }

    @Override
    public void onListFragmentInteraction(Personal_Infomation mItem) {

    }
}
