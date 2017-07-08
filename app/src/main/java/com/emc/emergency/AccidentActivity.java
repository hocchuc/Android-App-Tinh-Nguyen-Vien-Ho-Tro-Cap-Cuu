package com.emc.emergency;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.emc.emergency.Fragment.fragment_accident_page;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;

public class AccidentActivity extends AppCompatActivity implements fragment_accident_page.OnListFragmentInteractionListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);
        BuildFragment();
    }

    private void BuildFragment() {
        fragment_accident_page fragment_accident = new fragment_accident_page();
        getSupportFragmentManager().beginTransaction().add(R.id.content,fragment_accident).commit();

    }

    @Override
    public void onListFragmentInteraction(Accident item) {

    }
}