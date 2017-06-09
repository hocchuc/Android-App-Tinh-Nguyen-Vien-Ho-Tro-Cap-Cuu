package com.emc.emergency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.emc.emergency.Fragment.fragment_accident_page;
import com.emc.emergency.Fragment.fragment_personal_info_page;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.Personal_Infomation;

public class Personal_Inf_Activity extends AppCompatActivity implements fragment_personal_info_page.OnListFragmentInteractionListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    fragment_accident_page fragment_accident = new fragment_accident_page();
//                    getSupportFragmentManager().beginTransaction().add(R.id.content,fragment_accident).commit();
                    return true;
                case R.id.navigation_dashboard:
//                   getSupportFragmentManager().isDestroyed();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        mTextMessage = (TextView) findViewById(R.id.message1);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BuildFragment();
    }

    private void BuildFragment() {
//        FragmentManager managerTop = getSupportFragmentManager();
        fragment_personal_info_page fragment_personal_info_page = new fragment_personal_info_page();
        getSupportFragmentManager().beginTransaction().add(R.id.content1,fragment_personal_info_page).commit();

    }

    @Override
    public void onListFragmentInteraction(Personal_Infomation mItem) {

    }
}
