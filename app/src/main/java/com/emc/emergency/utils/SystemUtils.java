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
    public static final String LOCAL_API="http://192.168.1.31:8080/api/";

    public static final String PUT_USER_TO_ACCIDENT = "http://app-tnv-ho-tro-cap-cuu.herokuapp.com/api/accidents/";

    public static final String TAG_SELF = "self", TAG_NEW = "new",
            TAG_GO_MESSAGE = "message_go", TAG_EXIT = "exit", TAG_FIREBASE_MESSAGE = "fir   ebase_message",
            TAG_NEW_ACCIDENT = "new_accident", TAG_LOAD_MESSAGE="message_load", TAG_GO_MAP = "map_go", TAG_LOAD_MAP= "load_map";
    public static final String TYPE = "TYPE";
    public static final String TYPE_LOGOUT ="LOGOUT";
    public static final String TYPE_REGISTED ="REGISTED";
    public static final String ACTION = "ACTION";


    //for firebase
    public static final  String TYPE_HELPER = "helper";
    public static final  String id_PI = "id_PI";
    // for sharedPreferent bbảng User
    public static final String USER = "User";
    public static final String EMAIL_PI = "EMAIL_PI";
    public static final String ID_PI = "ID_PI";
    public static final  String ID_USER = "id_user";
    public static final String AVATAR_PI = "AVATAR_PI";
    public static final String userState = "StoreUserState";
    public static final  String NAME_PI = "NAME_PI";
    public static final  String PI = "PERSONAL_INFO";
    public static final String IS_LOGINED ="isLogined";

    public static final String DefaultAvatar = "https://firebasestorage.googleapis.com/v0/b/fir-demo-chat-spring.appspot.com/o/images%2Favatar_default%2Fprofile3.jpg?alt=media&token=19cbfd57-614a-4476-8bb3-43235f2928ca";
    public static final String LOADING_IMAGE_URL = "https://media.giphy.com/media/3oEjI6SIIHBdRxXI40/giphy.gif";
    public static String VideoUrl = "VIDEO_URL";

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