package com.emc.emergency.utils;

import android.content.res.Resources;

import java.util.Random;

public class SystemUtils {
    public static final String FCM_PROJECT_SENDER_ID = "728085231482";
    public static final String FCM_SERVER_CONNECTION = "@gcm.googleapis.com";
    public static final String BACKEND_ACTION_MESSAGE = "MESSAGE";
    public static final String BACKEND_ACTION_ACCIDENT = "new_accident";
    public static final String BACKEND_ACTION_ECHO = "com.emc.emergency.ECHO";
    public static final Random RANDOM = new Random();
    public static final String API_MODE="LOCAL";//"HEROKU" hoặc "LOCAL"
    public static final String HEROKU_API="https://app-tnv-ho-tro-cap-cuu.herokuapp.com/api/";
    public static final String LOCAL_API="http://192.168.1.223:8080/api/";

    public static final String PUT_USER_TO_ACCIDENT = "http://app-tnv-ho-tro-cap-cuu.herokuapp.com/api/accidents/";

    public static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_GO_MESSAGE = "message_go", TAG_EXIT = "exit", TAG_FIREBASE_MESSAGE = "fir   ebase_message",
    TAG_NEW_ACCIDENT = "new_accident", TAG_LOAD_MESSAGE="message_load", TAG_GO_MAP = "map_go", TAG_LOAD_MAP= "load_map";

    //for firebase
    final private String TYPE_HELPER = "helper";




    // for sharedPreferent
    public static final String EMAIL_PI = "EMAIL_PI";
    public static final String ID_PI = "ID_PI";
    public static final  String ID_USER = "id_user";
    public static final String AVATAR_PI = "AVATAR_PI";
    public static final String userState = "StoreUserState";
    public static final  String NAME_PI = "NAME_PI";
    public static final  String PI = "PERSONAL_INFO";



    public static int getScreenOrientation() {
        return Resources.getSystem().getConfiguration().orientation;
    }

    /**
     *
     * @return Địa chỉ base url của server
     * Dựa trên MODE ở trên
     */
    public static String getServerBaseUrl() {
         if(API_MODE.equals("LOCAL"))
         {
             return LOCAL_API;

         }
         else if (API_MODE.equals("HEROKU"))
        {
            return HEROKU_API;
        }
        return "Chỉnh thông tin trong model";
    }
}
