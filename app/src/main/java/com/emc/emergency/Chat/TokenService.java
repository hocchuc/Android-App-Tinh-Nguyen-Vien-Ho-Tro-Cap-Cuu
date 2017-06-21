package com.emc.emergency.Chat;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import com.android.volley.AuthFailureError;

import com.emc.emergency.ReportAccidentActivity;
import com.emc.emergency.model.User;
import com.emc.emergency.utils.SystemUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.emc.emergency.RequestRescueActivity.mediaType;

/**
 * Token service to handle the registration into the database
 */

public class TokenService {

    private static final String TAG = "TokenService";
    public static final String BACKEND_SERVER_IP = SystemUtils.getServerBaseUrl();

    private okhttp3.Response putResponse;
    private User user1 = new User();
    private Context context;
    private IRequestListener listener;
    String json;
    private final OkHttpClient client = new OkHttpClient();


    public TokenService(Context context, IRequestListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void registerTokenInDB(final String token, final String id_user) throws IOException {
        Response response = null;

            RequestBody formBody = new FormBody.Builder()
                    .add("token", token)
                    .add("id_user", id_user)
                    .build();
            Request request = new Request.Builder()
                    .url(SystemUtils.getServerBaseUrl()+"refreshToken")
                    .post(formBody)
                    .build();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
             response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            System.out.println(response.body().string());



    }

    public class PutToken {

        OkHttpClient client = new OkHttpClient();

        String put(String url, String txt) throws IOException {
            RequestBody body = RequestBody.create(mediaType, txt);
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8)
            {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    putResponse = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return putResponse.body().string();

        }
    }


}
