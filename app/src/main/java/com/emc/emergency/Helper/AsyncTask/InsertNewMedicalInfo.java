package com.emc.emergency.Helper.AsyncTask;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;

import com.emc.emergency.Helper.Utils.SystemUtils;

import java.io.IOException;

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

public class InsertNewMedicalInfo {
    String name_MI;
    String type_MI;
    String description;
    SharedPreferences sharedPreferences;
    long id_user;
    public InsertNewMedicalInfo(Activity activity, String name_MI, String type_MI, String description) {
        this.name_MI = name_MI;
        this.type_MI = type_MI;
        this.description = description;

        sharedPreferences = activity.getSharedPreferences(SystemUtils.ID_PI, Context.MODE_PRIVATE);
        id_user = sharedPreferences.getLong(SystemUtils.id_PI, -1);


    }

    private void excuteInsert() {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "{\n  \"name_MI\":\"Panadol\",\n  \"type_MI\":1,\n  \"description\":\"Thuốc nhức đầu\"\n}");
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/medical_Infoes")
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

                        response.body().string();
                    }
                });
            }
        }

        private void putRelation(String body) {


        }
    }
