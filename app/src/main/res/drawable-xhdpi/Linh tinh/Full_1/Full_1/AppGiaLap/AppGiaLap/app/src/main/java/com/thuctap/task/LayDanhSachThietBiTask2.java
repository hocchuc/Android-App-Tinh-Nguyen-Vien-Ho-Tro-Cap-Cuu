package com.thuctap.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.thuctap.adapter.QuanLyThietBiAdapter;
import com.thuctap.adapter.ThietBiAdapter;
import com.thuctap.model.ThietBi;

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
 * Created by Kiệt Nhii on 3/25/2017.
 */

public class LayDanhSachThietBiTask2 extends AsyncTask<Void, Void, ArrayList<ThietBi>> {
    Activity activity;
    ArrayList<ThietBi> arrThietBi =  new ArrayList<>();
    QuanLyThietBiAdapter thietBiAdapter;

    public LayDanhSachThietBiTask2(Activity activity, ArrayList<ThietBi> arrThietBi, QuanLyThietBiAdapter thietBiAdapter){
        this.activity = activity;
        this.arrThietBi = arrThietBi;
        this.thietBiAdapter =  thietBiAdapter;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        arrThietBi.clear();
    }

    @Override
    protected void onPostExecute(ArrayList<ThietBi> thietBis) {
        super.onPostExecute(thietBis);
        arrThietBi.clear();
        arrThietBi.addAll(thietBis);
        thietBiAdapter.notifyDataSetChanged();
    }

    @Override
    protected ArrayList<ThietBi> doInBackground(Void... params) {
        ArrayList<ThietBi> ds =  new ArrayList<>();
        try{
            URL url =  new URL("http://10.0.3.2:8080/RestProject/Rest/thietbiService/thietbis");
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
                ThietBi thietBi =  new ThietBi();
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if(jsonObject.has("id_tb"))
                    thietBi.setMaThietBi(Integer.parseInt(jsonObject.getString("id_tb")));
                if(jsonObject.has("tentb"))
                    thietBi.setTenThietBi(jsonObject.getString("tentb"));
                if(jsonObject.has("trangthai_hientai"))
                    thietBi.setTrangThaiHienTai(Integer.parseInt(jsonObject.getString("trangthai_hientai")));
                if(jsonObject.has("ngay_hientai"))
                    thietBi.setNgayHienTai(Date.valueOf(jsonObject.getString("ngay_hientai")));
                if(jsonObject.has("gio_hientai"))
                    thietBi.setGioHienTai(Time.valueOf(jsonObject.getString("gio_hientai")));
                if(jsonObject.has("hinhanh"))
                    thietBi.setHinhanh(jsonObject.getString("hinhanh"));
                ds.add(thietBi);
            }
        }
        catch (Exception ex){
            Log.e("LOI ", ex.toString());
        }
        return ds;
    }
}
