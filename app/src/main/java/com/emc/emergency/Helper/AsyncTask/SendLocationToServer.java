package com.emc.emergency.Helper.AsyncTask;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.emc.emergency.Helper.Utils.SystemUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by hocan on 24-Jul-17.
 */

public class SendLocationToServer {
    private Context context;
    private long id_user;
    private Double longtitude;
    private Double latitude;

    public SendLocationToServer(Context context, long id_user, Double longtitude, Double latitude) {
        this.context = context;
        this.id_user = id_user;
        this.longtitude = longtitude;
        this.latitude = latitude;
    }
    public void excute() {
        OkHttpClient client = new OkHttpClient();
        Log.d("SendLocationToServer",SystemUtils.getServerBaseUrl()+"users/"+id_user);

        MediaType mediaType = MediaType.parse("application/json");
        Log.d("SendLocationToServer","{\n      \"long_PI\" : "+longtitude+",\n      \"lat_PI\" : "+latitude+"\n}");
        RequestBody body = RequestBody.create(mediaType, "{\n      \"long_PI\" : "+longtitude+",\n      \"lat_PI\" : "+latitude+"\n}");
        Request request = new Request.Builder()
          .url(SystemUtils.getServerBaseUrl()+"users/"+id_user)
          .patch(body)
          .addHeader("content-type", "application/json")
          .build();

         client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 try {

                 } catch (Exception e1) {
                     e1.printStackTrace();
                 }
             }

             @Override
             public void onResponse(Call call, Response response) throws IOException {
                 Log.d("SendLocationToServer","Sucess");

             }
         }) ;
    }
}
