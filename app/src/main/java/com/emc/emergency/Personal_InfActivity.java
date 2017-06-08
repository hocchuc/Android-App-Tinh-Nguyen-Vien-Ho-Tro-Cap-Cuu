package com.emc.emergency;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Admin on 8/6/2017.
 */

public class Personal_InfActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        addControls();
        addEvents();
    }

    private void addEvents() {
    }

    private void addControls() {

    }
}
