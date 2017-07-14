package com.emc.emergency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.emc.emergency.Adapter.CustomGrid;
import com.emc.emergency.model.Accident;
import com.emc.emergency.utils.GPSTracker;
import com.emc.emergency.utils.SystemUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportAccidentActivity extends AppCompatActivity {
    Toolbar toolbar2;
    ActionProcessButton btnCamera, btnSubmit;
    Accident accident;
    String id_user = "ID_USER";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final MediaType mediaType = MediaType.parse("text/uri-list");

    Response postResponse, putResponse;

    SharedPreferences sharedPreferences,sharedPreferences1;
    String namePI;
    Double longitude, latitude;
    private String response;
    DatabaseReference mDatabase1;
    public static final String ACCIDENTS_CHILD = "accidents";
    Accident accident2;
    EditText txtRP_NamePI,txtRB_Details;

    GridView grid;
    String AccidentKey="";
    int id;
    String[] DSTainan = {"Gãy chân", "Gãy tay", "Bị bỏng", "Chó cắn", "Đụng xe", "Gãy cổ", "Gãy vai", "Té cầu thang", "Té xe"};
    int[] DSHinhTainan = {
            R.drawable.accident1,
            R.drawable.accident2,
            R.drawable.accident3,
            R.drawable.accident4,
            R.drawable.accident5,
            R.drawable.accident6,
            R.drawable.accident7,
            R.drawable.accident8,
            R.drawable.accident9,
    };
    int vt = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_accident);
        /**
         * Lấy tọa độ hiện tại
         */
        GPSTracker gps = new GPSTracker(ReportAccidentActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

        onControls();
        onEvents();
    }

    private void onEvents() {
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
// Get access to the custom title view
        TextView mTitle = (TextView) toolbar2.findViewById(R.id.toolbar_title);

        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final CustomGrid adapter = new CustomGrid(ReportAccidentActivity.this, DSTainan, DSHinhTainan);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedPosition(position);
                vt = position;
                adapter.notifyDataSetChanged();
            }
        });

        /**
         *  Gửi accident lên server
         */
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase1  = FirebaseDatabase.getInstance().getReference(ACCIDENTS_CHILD);
                AccidentKey= mDatabase1.child(ACCIDENTS_CHILD).push().getKey();

                sendData();

                mDatabase1.child(AccidentKey).setValue(accident2);


                btnSubmit.setProgress(1);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(ReportAccidentActivity.this,MainMenuActivity.class);
        startActivity(i);
    }

    private void sendData() {
        accident = new Accident();
        if(vt<0){
            accident.setDescription_AC(txtRB_Details.getText().toString());
        }else accident.setDescription_AC(DSTainan[vt]+" - "+txtRB_Details.getText().toString());

          //TODO thêm locate sau này, sử dụng giờ hệ thống
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        String currentDateandTime = sdf.format(new Date());
        accident.setDate_AC(currentDateandTime);

        accident.setStatus_AC("Pending");

        accident.setLat_AC(latitude);
        accident.setLong_AC(longitude);
        accident.setFirebaseKey(AccidentKey);

        // convert object to json
        Gson gson = new Gson();
        String json = gson.toJson(accident);

        PostAccident example = new PostAccident();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                response = example.post(SystemUtils.getServerBaseUrl() + "accidents", json);
//                        Log.d("response",response);
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
                    accident2.setLat_AC(jsonObject.getDouble("lat_AC"));
                if (jsonObject.has("status_AC"))
                    accident2.setStatus_AC(jsonObject.getString("status_AC"));
                if (jsonObject.has("adress"))
                    accident2.setAddress(jsonObject.getString("adress"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("accident2", accident2.toString());

        /**
         * tiếp tục gởi put lên  server để xác định user nào tạo tai nạn
         */
        PutRelation putRel = new PutRelation();

        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                String response2 = putRel.put(SystemUtils.getServerBaseUrl() + "accidents/" + accident2.getId_AC() + "/id_user",
                        SystemUtils.getServerBaseUrl() + "users/" + id);
//                        Log.d("response2",response2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onControls() {
        toolbar2= (Toolbar) findViewById(R.id.toolbar2);
        txtRP_NamePI= (EditText) findViewById(R.id.txtRB_NamePI);
        txtRB_Details= (EditText) findViewById(R.id.txtRB_Details);
        grid = (GridView) findViewById(R.id.grid1);
        btnSubmit = (ActionProcessButton) findViewById(R.id.btnSubmit);

        sharedPreferences1 = getApplicationContext().getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
        namePI=sharedPreferences1.getString(SystemUtils.NAME_PI,"");
        txtRP_NamePI.setText(namePI);
        txtRP_NamePI.setEnabled(false);

        sharedPreferences = getSharedPreferences(id_user, Context.MODE_PRIVATE);
        id = sharedPreferences.getInt("id_user", -1);
    }

    /**
     * Gửi accident lên server
     */
    public class PostAccident  {

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
     * Gởi put lên server
     */
    public class PutRelation {

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
