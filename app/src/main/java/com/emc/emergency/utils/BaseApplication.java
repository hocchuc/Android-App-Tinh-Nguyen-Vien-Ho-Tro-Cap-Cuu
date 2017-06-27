package com.emc.emergency.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by Admin on 27/6/2017.
 */

public class BaseApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
