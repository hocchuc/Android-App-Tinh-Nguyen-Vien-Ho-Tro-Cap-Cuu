package com.emc.emergency.Helper.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

import com.emc.emergency.Helper.Model.Personal_Information;
import com.emc.emergency.Helper.Model.User;
import com.emc.emergency.Helper.Model.User_Type;
import com.emc.emergency.Helper.Utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Admin on 13/7/2017.
 */

public class GetAllUser extends AsyncTask<Void, Void, ArrayList<User>> {
    ArrayList<User> arrUser = new ArrayList<>();
    private ReturnDataAllUser returnDataAllUser = null;

    public GetAllUser(ReturnDataAllUser returnDataAllUser) {
        this.returnDataAllUser = returnDataAllUser;
    }

    @Override
    protected void onPostExecute(ArrayList<User> users) {
        returnDataAllUser.handleReturnDataAllUser(users);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        arrUser.clear();
    }

    @Override
    protected ArrayList<User> doInBackground(Void... params) {
        ArrayList<User> userList = new ArrayList<>();
        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(SystemUtils.getServerBaseUrl() + "GetAllUser")
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            JSONArray usersJSONArray = new JSONArray(response.body().string());
                Log.d("usersJSONArray", usersJSONArray.toString());
            for (int i = 0; i < usersJSONArray.length(); i++) {
                User user1 = new User();
                JSONObject jsonObj = usersJSONArray.getJSONObject(i);
                if (jsonObj.has("name")) {
                    Personal_Information pi = new Personal_Information();
                    pi.setName_PI(jsonObj.getString("name"));
                    user1.setUser_name(pi.getName_PI());
                }
                if (jsonObj.has("id_user"))
                    user1.setId_user(jsonObj.getLong("id_user"));
                if (jsonObj.has("avatar"))
                    user1.setAvatar_User(jsonObj.getString("avatar"));
                if (jsonObj.has("long"))
                    user1.setLong_PI(jsonObj.getDouble("long"));
                if (jsonObj.has("lat"))
                    user1.setLat_PI(jsonObj.getDouble("lat"));
                if (jsonObj.has("name_user_type")) {
                    User_Type user_type1 = new User_Type();
                    user_type1.setName_user_type(jsonObj.getString("name_user_type"));
                    user1.setUser_type(user_type1);
                }
                if (jsonObj.has("token"))
                    user1.setToken(jsonObj.getString("token"));
                    Log.d("User1", user1.toString());
                userList.add(user1);
            }
            Log.d("DSUser1", userList.toString());
        } catch (Exception ex) {
//                Log.e("LOI ", ex.toString());
        }
        return userList;
    }
}
