package com.emc.emergency.Personal_Information;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.emc.emergency.Main_Menu.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.Medical_Information;
import com.emc.emergency.Helper.Model.Personal_Information;

public class Personal_Inf_Activity extends AppCompatActivity implements fragment_personal_info_page.OnListFragmentInteractionListener
   ,fragment_medical_info_page.OnListFragmentInteractionListener,fragment_contract.OnFragmentInteractionListener {

    BottomNavigationView navigation;
    Toolbar toolbar3;
    TextView mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        addControls();
        addEvents();

    }

    private void addEvents() {
        xulyNavigation();
        toolbar3= (Toolbar) findViewById(R.id.toolbar3);

        setSupportActionBar(toolbar3);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
// Get access to the custom title view
         mTitle = (TextView) toolbar3.findViewById(R.id.toolbar_title);

        toolbar3.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent i=new Intent(Personal_Inf_Activity.this,MainMenuActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void addControls() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        fragment_personal_info_page fragment_personal_info_page = new fragment_personal_info_page();
        getSupportFragmentManager().beginTransaction().add(R.id.content1, fragment_personal_info_page).commit();

    }

    private void xulyNavigation() {
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment selectedFragment = null;
                try {
                    switch (item.getItemId()) {
                        case R.id.navigation_pi:
                            fragment_personal_info_page fragment_personal_info_page = new fragment_personal_info_page();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content1, fragment_personal_info_page).commit();
                            break;
                        case R.id.navigation_mi:
                            fragment_medical_info_page fragment_medical_info_page = new fragment_medical_info_page();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content1, fragment_medical_info_page).commit();
                            break;
                        case R.id.navigation_contract:
                            fragment_contract fragment_contract = new fragment_contract();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content1, fragment_contract).commit();
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });
    }



    @Override
    public void onListFragmentInteraction(Personal_Information mItem) {

    }

    @Override
    public void onListFragmentInteraction(Medical_Information mItem) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
