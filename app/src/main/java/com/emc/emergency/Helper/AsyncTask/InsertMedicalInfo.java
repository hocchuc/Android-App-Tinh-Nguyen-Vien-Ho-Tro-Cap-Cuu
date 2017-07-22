package com.emc.emergency.Helper.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.emc.emergency.Helper.Model.Medical_Information;
import com.emc.emergency.Helper.Utils.SystemUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by hocan on 09-Jul-17.
 */

public class InsertMedicalInfo {
    String name_MI;
    String type_MI;
    String description;
    SharedPreferences sharedPreferences;
    Activity activity;
    long id_user = 0l;
    long id_pi=0l;
    long id_mi = 0l;
    RecyclerView.Adapter adapter;
    public InsertMedicalInfo(Activity activity, String name_MI, String type_MI, String description, RecyclerView.Adapter adapter) {
        this.activity = activity;
        this.name_MI = name_MI;
        this.type_MI = type_MI;
        this.description = description;
        this.adapter = adapter;

//        sharedPreferences = activity.getSharedPreferences(SystemUtils.ID_PI, Context.MODE_PRIVATE);
//        id_user = sharedPreferences.getLong(SystemUtils.id_PI, -1);
        SharedPreferences sharedPreferences1 = activity.getSharedPreferences(SystemUtils.PI, Context.MODE_PRIVATE);
        id_pi = sharedPreferences1.getLong(SystemUtils.ID_PI,-1);

    }
    // Thuc hiện gởi thông tin lên server
    public void excuteInsert() {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"name_MI\":\""+name_MI+"\",\n  \"type_MI\":"+type_MI+",\n  \"description\":\""+description+"\"\n}");
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl()+"medical_Infoes")
                .post(body)
                .addHeader("content-type", "application/json;charset=utf-8")
                .build();
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

                 client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);

                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response.body().string());
                            if (jsonObject.has("id_MI"))
                                id_mi = jsonObject.getLong("id_MI");
                            Log.d("onResponse id_mi:",id_mi+"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        putRelation();

                    }
                });
            }
        }

        private void putRelation() {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("text/uri-list");
            RequestBody body2 = RequestBody.create(mediaType, SystemUtils.getServerBaseUrl()+"personal_Infomations/"+id_pi);
            Log.d("putRelationBody",SystemUtils.getServerBaseUrl()+"personal_Infomations/"+id_pi);
            Request request = new Request.Builder()
                    .url(SystemUtils.getServerBaseUrl()+"medical_Infoes/"+id_mi+"/id_PI")
                    .put(body2)
                    .addHeader("content-type", "text/uri-list")
                    .build();
            Log.d("putRelation",SystemUtils.getServerBaseUrl()+"medical_Infoes/"+id_mi+"/id_PI");
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    Response response = client.newCall(request).execute();
                    if(response.isSuccessful())Log.d("response",response.toString());
                    ArrayList<Medical_Information> medical_informations = new ArrayList<>();
                    GetMedicalInfo getMedicalInfo = new GetMedicalInfo
                            (activity, medical_informations, Integer.parseInt(type_MI),adapter);
                    getMedicalInfo.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
