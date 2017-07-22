package com.emc.emergency.Helper.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Admin on 13/7/2017.
 */

public class GetAllUser  extends AsyncTask<Void, Void, ArrayList<User>> {
    ArrayList<User> arrUser=new ArrayList<>();
    private ReturnDataAllUser returnDataAllUser=null;

    public GetAllUser(ReturnDataAllUser returnDataAllUser){
        this.returnDataAllUser=returnDataAllUser;
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

            URL url = new URL(SystemUtils.getServerBaseUrl() + "users");
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inStreamReader);
            StringBuilder builder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                builder.append(line);
                line = bufferedReader.readLine();
            }
            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONObject _embeddedObject = jsonObject.getJSONObject("_embedded");
            JSONArray usersJSONArray = _embeddedObject.getJSONArray("users");
//                Log.d("jsonObj", jsonObject.toString());
            for (int i = 0; i < usersJSONArray.length(); i++) {
                User user1 = new User();
                JSONObject jsonObj = usersJSONArray.getJSONObject(i);
                if (jsonObj.has("id_user"))
                    user1.setId_user(Long.parseLong((jsonObj.getString("id_user"))));
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
                if (jsonObj.has("id_user_type")) {
                    String user_type = jsonObj.getString("id_user_type");
                    User_Type user_type1 = new User_Type();
                    try {
                        JSONObject jsonObject1 = new JSONObject(user_type);
                        if (jsonObject1.has("id_user_type"))
                            user_type1.setId_user_type(jsonObject1.getLong("id_user_type"));
                        if (jsonObject1.has("name_user_type"))
                            user_type1.setName_user_type(jsonObject1.getString("name_user_type"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    user1.setUser_type(user_type1);
                }
//                    Log.d("User1", user1.toString());
                userList.add(user1);
            }
            Log.d("DSUser1", userList.toString());
        } catch (Exception ex) {
//                Log.e("LOI ", ex.toString());
        }
        return userList;
    }
}
