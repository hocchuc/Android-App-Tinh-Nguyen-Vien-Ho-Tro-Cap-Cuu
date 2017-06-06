package com.emc.emergency.Accident;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.emc.emergency.Fragment.fragment_accident_page;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;

public class AccidentActivity extends AppCompatActivity implements fragment_accident_page.OnListFragmentInteractionListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        BuildFragment();
    }

    private void BuildFragment() {
        FragmentManager managerTop = getSupportFragmentManager();
        fragment_accident_page fragment_accident = new fragment_accident_page();
        getSupportFragmentManager().beginTransaction().add(R.id.content,fragment_accident).commit();

    }

    @Override
    public void onListFragmentInteraction(Accident item) {

    }
}
