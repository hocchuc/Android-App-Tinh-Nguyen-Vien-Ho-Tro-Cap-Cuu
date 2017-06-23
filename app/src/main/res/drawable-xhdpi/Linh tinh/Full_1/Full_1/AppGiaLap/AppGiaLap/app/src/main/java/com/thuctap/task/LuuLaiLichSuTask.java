package com.thuctap.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.thuctap.model.ThietBi;

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
 * Created by Kiệt Nhii on 3/25/2017.
 */

public class LuuLaiLichSuTask extends AsyncTask<String, Integer, String> {
    Activity activity;
    ThietBi thietBi;
    ProgressDialog progressDialog;
    public LuuLaiLichSuTask(Activity activity, ThietBi thietBi, ProgressDialog progressDialog){
        this.activity = activity;
        this.thietBi=thietBi;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog =  new ProgressDialog(activity);
        progressDialog.setMessage("Đang thực hiện");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
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
        List nameValuePair = new ArrayList(5);
        nameValuePair.add(new BasicNameValuePair("id_thietbi", thietBi.getMaThietBi()+""));
        nameValuePair.add(new BasicNameValuePair("tenthietbi", thietBi.getTenThietBi()));
        nameValuePair.add(new BasicNameValuePair("trangthai_hientai", thietBi.getTrangThaiHienTai()+""));
        nameValuePair.add(new BasicNameValuePair("ngay_hientai", thietBi.getNgayHienTai()+""));
        nameValuePair.add(new BasicNameValuePair("gio_hientai", thietBi.getGioHienTai()+""));
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
