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

    class GetUsers extends AsyncTask<Void, String, User> {
        Context context;
        User user;
        String id_user;
        String token;

        public GetUsers(Context context, User user, String id_user, String token) {
            this.context = context;
            this.user = user;
            this.id_user=id_user;
            this.token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(User users) {
            super.onPostExecute(users);

            users.setToken(token);
            Log.d("user after: ",users.toString());
            Gson gson = new Gson();
            json = gson.toJson(users);
            Log.d("jsonToken",json);
           // user1 = users;
            PutToken putToke = new PutToken();
            try {
                putToke.put(BACKEND_SERVER_IP + "users/" + id_user, json);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected User doInBackground(Void... params) {
            User user1=new User();
            try {
                URL url = new URL(SystemUtils.getServerBaseUrl()+"users/"+id_user);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inStreamReader);
                StringBuilder builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                JSONObject jsonObj = new JSONObject(builder.toString());
                Log.d("jsonObj",jsonObj.toString());
//                    User user = new User();
                    if (jsonObj.has("id_user"))
                        user1.setId_user(Integer.parseInt((jsonObj.getString("id_user"))));
                    if (jsonObj.has("username"))
                        user1.setUser_name(jsonObj.getString("username"));
                    if (jsonObj.has("token"))
                        user1.setToken(jsonObj.getString("token"));
                    if (jsonObj.has("password"))
                        user1.setPassword(jsonObj.getString("password"));
                    if (jsonObj.has("long_PI"))
                        user1.setLong_PI(jsonObj.getDouble("long_PI"));
                    if (jsonObj.has("lat_PI"))
                        user1.setLat_PI(jsonObj.getDouble("lat_PI"));
                    if (jsonObj.has("id_user_type"))
                        user1.setId_user_type(jsonObj.getString("id_user_type"));
                    if (jsonObj.has("avatar"))
                        user1.setAvatar(jsonObj.getString("avatar"));
                Log.d("User1",user1.toString());
            } catch (Exception ex) {
                Log.e("LOI ", ex.toString());
            }
            return user1;
        }
    }
}
