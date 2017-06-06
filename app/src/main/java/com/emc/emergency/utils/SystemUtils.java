package com.emc.emergency.utils;

import android.content.res.Resources;

import java.util.Random;

public class SystemUtils {
    public static final String FCM_PROJECT_SENDER_ID = "728085231482";
    public static final String FCM_SERVER_CONNECTION = "@gcm.googleapis.com";
    public static final String BACKEND_ACTION_MESSAGE = "MESSAGE";
    public static final String BACKEND_ACTION_ECHO = "com.emc.emergency.ECHO";
    public static final Random RANDOM = new Random();
    public static final String API_MODE="LOCAL";//"HEROKU" hoặc "LOCAL"
    public static final String HEROKU_API="https://app-tnv-ho-tro-cap-cuu.herokuapp.com/api";
    public static final String LOCAL_API="http://10.0.3.2/api";

    public static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_GO_MESSAGE = "message_go", TAG_EXIT = "exit", TAG_FIREBASE_MESSAGE = "firebase_message",
    TAG_NEW_ACCIDENT = "new_accident", TAG_LOAD_MESSAGE="message_load", TAG_GO_MAP = "map_go", TAG_LOAD_MAP= "load_map";



    public static int getScreenOrientation() {
        return Resources.getSystem().getConfiguration().orientation;
    }
}
