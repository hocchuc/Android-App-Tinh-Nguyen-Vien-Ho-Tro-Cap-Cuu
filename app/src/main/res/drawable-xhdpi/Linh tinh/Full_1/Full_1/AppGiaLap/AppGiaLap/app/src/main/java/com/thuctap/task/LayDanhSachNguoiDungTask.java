package com.thuctap.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.thuctap.adapter.UserAdapter;
import com.thuctap.model.ThietBi;
import com.thuctap.model.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Kiệt Nhii on 3/31/2017.
 */

public class LayDanhSachNguoiDungTask extends AsyncTask<Void, Void, ArrayList<User>> {
    Activity activity;
    UserAdapter adapter;
    ArrayList<User> arrayList;

    public LayDanhSachNguoiDungTask(Activity activity, ArrayList<User> arrayList, UserAdapter userAdapter){
        this.activity = activity;
        this.arrayList = arrayList;
        this.adapter =  userAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        arrayList.clear();
    }

    @Override
    protected void onPostExecute(ArrayList<User> users) {
        super.onPostExecute(users);
        arrayList.clear();
        arrayList.addAll(users);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected ArrayList<User> doInBackground(Void... params) {
        ArrayList<User> ds =  new ArrayList<>();
        try{
            URL url =  new URL("http://10.0.3.2:8080/RestProject/Rest/taikhoanService/taikhoans");
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            InputStreamReader inStreamReader = new InputStreamReader(connect.getInputStream(), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inStreamReader);
            StringBuilder builder = new StringBuilder();
            String line  =  bufferedReader.readLine();
            while (line!=null){
                builder.append(line);
                line = bufferedReader.readLine();
            }
            JSONArray jsonArray = new JSONArray(builder.toString());
            for (int i=0; i<jsonArray.length(); i++){
                User user =  new User();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if(jsonObject.has("matk"))
                    user.setMaNguoiDung(Integer.parseInt(jsonObject.getString("matk")));
                if(jsonObject.has("tendangnhap"))
                    user.setUsername(jsonObject.getString("tendangnhap"));
                if(jsonObject.has("mk"))
                    user.setPassword(jsonObject.getString("mk"));
                if(jsonObject.has("loaitk"))
                    user.setLoaiTaiKhoan(Integer.parseInt(jsonObject.getString("loaitk")));
                ds.add(user);
            }
        }
        catch (Exception ex){
            Log.e("LOI ", ex.toString());
        }
        return ds;
    }
}
