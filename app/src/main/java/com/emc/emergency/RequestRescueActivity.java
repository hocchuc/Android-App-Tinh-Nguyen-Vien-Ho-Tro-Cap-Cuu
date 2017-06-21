package com.emc.emergency;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.emc.emergency.Chat.TokenService;
import com.emc.emergency.Fragment.fragment_map_page;
import com.emc.emergency.R;
import com.emc.emergency.model.Accident;
import com.emc.emergency.model.User;
import com.emc.emergency.utils.GPSTracker;
import com.emc.emergency.utils.SystemUtils;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestRescueActivity extends AppCompatActivity
        implements fragment_map_page.onFragmentMapInteraction {


    String id_user="ID_USER";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType mediaType = MediaType.parse("text/uri-list");

    Response postResponse,putResponse;

    SharedPreferences sharedPreferences;
    Accident accident;
    Accident accident2;
    Double longitude,latitude;
    DatabaseReference mFirebaseDatabaseReference;
//    FirebaseAnalytics mFirebaseAnalytics;
 //   private static final String MESSAGE_SENT_EVENT = "message_sent";
    public static final String ACCIDENTS_CHILD = "accidents";
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_rescue);

        GPSTracker gps = new GPSTracker(RequestRescueActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        BuildFragment();
        onControls();
        onEvents();
    }
    private void BuildFragment() {
//        FragmentManager managerTop = getSupportFragmentManager();
        fragment_map_page fragment_map_page = new fragment_map_page();
        getSupportFragmentManager().beginTransaction().add(R.id.contentRequest2,fragment_map_page).commit();

    }
    private void onEvents() {
        SendData();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child(ACCIDENTS_CHILD).push().setValue(accident2);
//        mFirebaseAnalytics.logEvent(MESSAGE_SENT_EVENT, null);
    }
    // TODO đổi tên hàm này
    private void SendData() {
        sharedPreferences = getSharedPreferences(id_user, Context.MODE_PRIVATE);
        final int id = sharedPreferences.getInt("id_user", -1);

        accident = new Accident();
        accident.setDescription_AC("Tai nạn");

        //TODO thêm locate sau này, sử dụng giờ hệ thống
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String currentDateandTime = sdf.format(new Date());
        accident.setDate_AC(currentDateandTime);

        accident.setStatus_AC("Active");

        accident.setLat_AC(latitude);
        accident.setLong_AC(longitude);

        // convert object to json
        Gson gson = new Gson();
        String json = gson.toJson(accident);

        RequestRescueActivity.PostAccident example = new RequestRescueActivity.PostAccident();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                response = example.post(SystemUtils.getServerBaseUrl() + "accidents", json);
                Log.d("response",response);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        accident2 = new Accident();

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);


            for (int i = 0; i < jsonObject.length(); i++) {
                if (jsonObject == null) continue;
                if (jsonObject.has("id_AC"))
                    accident2.setId_AC(Long.parseLong(jsonObject.getString("id_AC")));
                if (jsonObject.has("description_AC"))
                    accident2.setDescription_AC(jsonObject.getString("description_AC"));
                if (jsonObject.has("date_AC"))
                    accident2.setDate_AC(jsonObject.getString("date_AC"));
                if (jsonObject.has("long_AC"))
                    accident2.setLong_AC(jsonObject.getDouble("long_AC"));
                if (jsonObject.has("lat_AC"))
                    accident2.setLat_AC( jsonObject.getDouble("lat_AC"));
                if (jsonObject.has("status_AC"))
                    accident2.setStatus_AC(jsonObject.getString("status_AC"));
                if (jsonObject.has("adress"))
                    accident2.setAddress(jsonObject.getString("adress"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("accident2",accident2.toString());

        /**
         * tiếp tục gởi put lên  server để xác định user nào tạo tai nào
         */
        RequestRescueActivity.PutRelation putRel = new RequestRescueActivity.PutRelation();

        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                String response2 = putRel.put(SystemUtils.getServerBaseUrl()+"accidents/"+accident2.getId_AC()+"/id_user",
                        SystemUtils.getServerBaseUrl()+"users/"+id);
                Log.d("response2",response2);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void onControls() {
    }

    @Override
    public void onFragmentMapInteraction(Uri uri) {

    }

    /**
     * Gửi accident lên server
     */
    public class PostAccident{

        OkHttpClient client = new OkHttpClient();

        String post(String url, String json) throws IOException {
            RequestBody body = RequestBody.create(JSON, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            try {
                postResponse = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return postResponse.body().string();

        }
    }

    /**
     * Gởi put lên server để tạo relation giữa user và accident
     */
    public class PutRelation{

        OkHttpClient client = new OkHttpClient();

        String put(String url, String txt) throws IOException {
            RequestBody body = RequestBody.create(mediaType, txt);
            Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .addHeader("content-type", "text/uri-list")
                    .addHeader("cache-control", "no-cache")
                    .build();
            try {
                putResponse = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return putResponse.body().string();

        }
    }
}
