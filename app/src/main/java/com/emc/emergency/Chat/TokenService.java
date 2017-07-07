package com.emc.emergency.Chat;

import android.content.Context;

import android.os.StrictMode;
import android.util.Log;


import com.emc.emergency.model.User;
import com.emc.emergency.utils.SystemUtils;


import java.io.IOException;

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
    private OkHttpClient client;


    public TokenService(Context context, IRequestListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void registerTokenInDB(final String token, final String id_user) throws IOException {
        Response response = null;
        client = new OkHttpClient();

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n\t\"token\":\""+token+"\"}");
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl()+"users/"+id_user)
                .patch(body)
                .addHeader("content-type", "application/json;charset=utf-8")
                .build();

        Log.d("removeToken",SystemUtils.getServerBaseUrl()+"users/"+id_user);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            response = client.newCall(request).execute();
            if(response.isSuccessful()) Log.d("removeTokenResponge","SUCCESS");



        }
            Log.d("TokenServices","onRefresh");
            System.out.println(response.body().string());



    }

    public class PutToken {

//        OkHttpClient client = new OkHttpClient();

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
