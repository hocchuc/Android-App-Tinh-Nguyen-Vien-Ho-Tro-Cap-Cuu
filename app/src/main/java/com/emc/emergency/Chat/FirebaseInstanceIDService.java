package com.emc.emergency.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

/**
 * FirebaseInstanceIDService to register token in the database
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService implements IRequestListener {

    private static final String TAG = "FCMInstanceIDService";
    private TokenService tokenService;
    SharedPreferences sharedPreferences;
    String id_user = "ID_USER";
    @Override
    public void onTokenRefresh() {

        Log.d(TAG, "Token refresh logic");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);

        //Call the token service to save the token in the database
        tokenService = new TokenService(this, this);
        sharedPreferences = getSharedPreferences(String.valueOf(id_user), Context.MODE_PRIVATE);
        final int id = sharedPreferences.getInt("id_user", -1);
        try {
            tokenService.registerTokenInDB(token, String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onComplete() {
        Log.d(TAG, "Token registered successfully in the DB");

    }

    @Override
    public void onError(String message) {
        Log.d(TAG, "Error trying to register the token in the DB: " + message);
    }

}
