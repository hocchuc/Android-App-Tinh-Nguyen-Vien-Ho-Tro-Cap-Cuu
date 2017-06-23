package com.thuctap.task;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.thuctap.appgialap.QuanLyThietBiActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kiệt Nhii on 3/31/2017.
 */

public class ThemThietBiTask extends AsyncTask<String, Integer, String> {
    Activity activity;
    String tenThietBi;
    String ngayHienTai;
    String gioHienTai;
    String hinhThietBi;
    Dialog dialog;

    public ThemThietBiTask(Activity activity, String tenThietBi, String ngayHienTai, String gioHienTai,String hinhThietBi, Dialog dialog){
        this.activity=activity;
        this.tenThietBi=tenThietBi;
        this.ngayHienTai=ngayHienTai;
        this.gioHienTai=gioHienTai;
        this.dialog=dialog;
        this.hinhThietBi=hinhThietBi;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(activity, "Đã thêm thiết bị.", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        Intent intent =  new Intent(activity, QuanLyThietBiActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected String doInBackground(String... params) {
        return makePostRequest(params[0]);
    }
    private String makePostRequest(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        // URL của trang web nhận request
        HttpPost httpPost = new HttpPost(url);
        // Các tham số truyền
        List nameValuePair = new ArrayList(3);
        nameValuePair.add(new BasicNameValuePair("tenthietbi", tenThietBi));
        nameValuePair.add(new BasicNameValuePair("ngay_hientai", ngayHienTai));
        nameValuePair.add(new BasicNameValuePair("gio_hientai", gioHienTai));
        //Encoding POST data
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String kq = "";
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            kq = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kq;
    }
}
