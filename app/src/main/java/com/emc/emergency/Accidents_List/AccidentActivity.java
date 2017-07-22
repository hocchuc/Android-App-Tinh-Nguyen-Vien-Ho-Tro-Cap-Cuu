package com.emc.emergency.Accidents_List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.emc.emergency.Main_Menu.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.Accident;

public class AccidentActivity extends AppCompatActivity implements fragment_accident_page.OnListFragmentInteractionListener{
    Toolbar toolbar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident);
        addControls();
        addEvents();
        BuildFragment();
    }

    private void addEvents() {
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
// Get access to the custom title view
        TextView mTitle = (TextView) toolbar1.findViewById(R.id.toolbar_title);

        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(AccidentActivity.this,MainMenuActivity.class);
        startActivity(i);
    }

    private void addControls() {
        toolbar1= (Toolbar) findViewById(R.id.toolbar1);
    }

    private void BuildFragment() {
        fragment_accident_page fragment_accident = new fragment_accident_page();
        getSupportFragmentManager().beginTransaction().add(R.id.content,fragment_accident).commit();

    }

    @Override
    public void onListFragmentInteraction(Accident item) {

    }
}