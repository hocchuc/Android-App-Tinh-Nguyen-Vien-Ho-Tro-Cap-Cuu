package com.emc.emergency.Login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.iml.ActionProcessButton;
import com.emc.emergency.Helper.Services.IRequestListener;
import com.emc.emergency.Helper.Services.TokenService;
import com.emc.emergency.Main_Menu.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.Register.RegisterActivity;
import com.emc.emergency.Helper.Model.FlashMessage;
import com.emc.emergency.Helper.Model.Personal_Information;
import com.emc.emergency.Helper.Model.User;
import com.emc.emergency.Helper.Model.User_Type;
import com.emc.emergency.Helper.Utils.GPSTracker;
import com.emc.emergency.Helper.Utils.SystemUtils;
import com.emc.emergency.Helper.Utils.Utility;
import com.google.firebase.iid.FirebaseInstanceId;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements IRequestListener {
    ActionProcessButton btnLogin;
    Button btnSignUp;
    EditText txtUsername;
    EditText txtPassword;
    Personal_Information pi;
    MaterialDialog progressDialog;
    SharedPreferences preferences, preferences1, preferences2;
    String userState = "StoreUserState";
    String id_user = "ID_USER";
    private TokenService tokenService;
    double latitude = 0;
    double longitude = 0;
    //    private Utils utils;
    String token;
    int id = 0;
    FlashMessage flashMessage;

    private ProgressBar progressBar;
//    private FirebaseAuth auth;

    private static final int REQUEST_CAMERA_PERMISSIONS = 123;
    private static final int  ACCESS_FINE_LOCATION = 456;
    private boolean networkError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        CheckNetwork();
        progressDialog = new MaterialDialog.Builder(this)
                   .title(R.string.progress_dialog_loading)
                   .content(R.string.please_wait)
                   .progress(true, 0)
                   .show();

        addControls();
        addEvents();
    }

    private void addControls() {
        txtUsername = (EditText) findViewById(R.id.loginEmail);
        txtPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (ActionProcessButton) findViewById(R.id.btnLogin);
        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tokenService = new TokenService(this, this);

    }

    private void addEvents() {
        // Hiển thị dialog yêu cầu quyền
        RequestPermissions();
        progressDialog.dismiss();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GPSTracker gps = new GPSTracker(LoginActivity.this);
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                User user = new User(username, password);
                user.setLat_PI(latitude);
                user.setLong_PI(longitude);
                btnLogin.setProgress(1);
                sendUser(user);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences = getSharedPreferences(userState, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username", txtUsername.getText().toString());
        editor.putString("Password", txtPassword.getText().toString());
        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = "";
        String password = "";
        Boolean isLogined = false;
        Intent intent = getIntent();
        if (intent.hasExtra(SystemUtils.ACTION)) {
            if (intent.getStringExtra(SystemUtils.ACTION).equals(SystemUtils.TYPE_REGISTED)) {
                username = intent.getStringExtra("username");
                password = intent.getStringExtra("password");
                Log.d(SystemUtils.ACTION, SystemUtils.TYPE_REGISTED);

            }
            if (intent.getStringExtra(SystemUtils.ACTION).equals(SystemUtils.TYPE_LOGOUT)) {
                username = intent.getStringExtra("");
                password = intent.getStringExtra("");
                Log.d(SystemUtils.ACTION, SystemUtils.TYPE_LOGOUT);

            }
            if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
                finish();
            }

        } else {
            preferences = getSharedPreferences(SystemUtils.USER, MODE_PRIVATE);
            isLogined = preferences.getBoolean(SystemUtils.IS_LOGINED, false);
            if (isLogined&&!networkError) {

                    Intent MainMenuIntent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(MainMenuIntent);
                }



            username = preferences.getString("Username", "");
            password = preferences.getString("Password", "");
        }


        txtUsername.setText(username);
        txtPassword.setText(password);

        progressBar.setVisibility(View.GONE);
    }

    private void sendUser(final User user) {
        progressBar.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(user.getUser_name())) {
            btnLogin.setError("Enter email address");
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(user.getPassword())) {
            btnLogin.setError("Enter password!");
            
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        } else if (user.getPassword().length() < 6) {
            btnLogin.setError("Phải nhập từ 6 ký tự trở lên");
            
            Toast.makeText(this, "Phải nhập từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return;
        }else if (Utility.validate(user.getUser_name())==false) {
            btnLogin.setError("Sai cú pháp - Example@gmail.com");
            
            Toast.makeText(this, "Sai cú pháp - Example@gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }  else {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);  // <-- this is the important line!
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(SystemUtils.getServerBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build());

            Retrofit retrofit = builder.build();
            final LoginClient client = retrofit.create(LoginClient.class);
            final Call<FlashMessage> call = client.loginAccount(user);

            progressBar.setVisibility(View.GONE);
            call.enqueue(new Callback<FlashMessage>() {
                @Override
                public void onResponse(Call<FlashMessage> call, Response<FlashMessage> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                flashMessage = response.body();
    //                        Log.d("body", flashMessage.toString());

                            }
                            try {
                                if (flashMessage.getStatus().equals("SUCCESS")) {
                                    id = Integer.parseInt(flashMessage.getMessage()); // khi sai mat khau thi vang o day
                                    Log.d("ID_USER", String.valueOf(id));

                                    preferences1 = getSharedPreferences(id_user, MODE_PRIVATE);
                                    SharedPreferences.Editor editor1 = preferences1.edit();
                                    editor1.putInt("id_user", id);
                                    editor1.commit();
                                    //GetPersonalInfo();
                                    btnLogin.setProgress(0);
                                    btnLogin.setText("Done");

                                    try {
                                        token = FirebaseInstanceId.getInstance().getToken();
                                        tokenService.registerTokenInDB(token, id + "");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    OkHttpClient client = new OkHttpClient();
                                    Request request = new Request.Builder()
                                            .url(SystemUtils.getServerBaseUrl() + "users/" + id)
                                            .get()
                                            .addHeader("content-type", "application/json")
                                            .build();
                                    int SDK_INT = Build.VERSION.SDK_INT;
                                    if (SDK_INT > 8) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                .permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        try {
                                            okhttp3.Response response1 = client.newCall(request).execute();
                                            // Log.d("DSUSer",response1.body().string());
                                            User user1 = new User();
                                            User_Type user_type1 = new User_Type();

                                            String jsonUser = response1.body().string();
                                            JSONObject jsonObject = null;
                                            try {
                                                jsonObject = new JSONObject(jsonUser);
                                                if (jsonObject.has("id_user"))
                                                    user1.setId_user(Long.parseLong(jsonObject.getString("id_user")));
                                                if (jsonObject.has("username"))
                                                    user1.setUser_name(jsonObject.getString("username"));
                                                if (jsonObject.has("password"))
                                                    user1.setPassword(jsonObject.getString("password"));
                                                if (jsonObject.has("token"))
                                                    user1.setToken(jsonObject.getString("token"));

                                                if (jsonObject.has("id_user_type")) {
                                                    String user_type = jsonObject.getString("id_user_type");
    //                                            Log.d("user_type", user_type);

                                                    try {
                                                        JSONObject jsonObject1 = new JSONObject(user_type);
                                                        if (jsonObject1.has("name_user_type"))
                                                            user_type1.setName_user_type(jsonObject1.getString("name_user_type"));
                                                        if (jsonObject1.has("id_user_type"))
                                                            user_type1.setId_user_type(jsonObject1.getLong("id_user_type"));
                                                        user1.setUser_type(user_type1);
    //                                                Log.d("User_type", user_type1.toString());
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
    //                                            Log.d("User1", user1.toString());
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            preferences2 = getSharedPreferences("User", MODE_PRIVATE);
                                            SharedPreferences.Editor editor2 = preferences2.edit();
                                            editor2.putBoolean(SystemUtils.IS_LOGINED, true);
                                            editor2.putString("username", user1.getUser_name());
                                            editor2.putString("password", user1.getPassword());
                                            editor2.putString("token", user1.getToken());
                                            editor2.putString("lat_PI", String.valueOf(user1.getLat_PI()));
                                            editor2.putString("long_PI", String.valueOf(user1.getLong_PI()));
                                            editor2.putLong("id_user_type", (user1.getUser_type().getId_user_type()));
                                            editor2.putString("name_user_type", String.valueOf(user1.getUser_type().getName_user_type()));
                                            editor2.commit();


                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                    startActivity(intent);

                                } else {
                                    btnLogin.setError(getString(R.string.wrongpassword));
                                    Toast.makeText(getApplicationContext(),R.string.wrongpassword, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<FlashMessage> call, Throwable t) {
                    btnLogin.setProgress(0);
                    btnLogin.setError(getString(R.string.pleasecheckconnection));
                    Toast.makeText(getApplicationContext(),R.string.pleasecheckconnection, Toast.LENGTH_LONG).show();
                    
                }
            });
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GPSTracker gps = new GPSTracker(LoginActivity.this);
                    if (gps.canGetLocation()) {
                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                    }

                } else {
                    Toast.makeText(this, "App can't start without GPS permission, Closing", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    /**
     * xin quyền camera + location
     */
    private void RequestPermissions() {
        if (Build.VERSION.SDK_INT > 17) {
            final String[] permissions = {
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,


                                                           
            };

            final List<String> permissionsToRequest = new ArrayList<>();
            boolean flag = false;
            
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
           
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(LoginActivity.this, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CAMERA_PERMISSIONS);
            }
        }
      
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(String message) {

    }

    /**
     * Lấy user đang đăng nhập về
     */
    private void GetPersonalInfo() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(SystemUtils.getServerBaseUrl() + "users/" + id_user + "/personal_Infomation")
                .get()
                .build();
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            try {
                okhttp3.Response response = client.newCall(request).execute();
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(response.body().string());
                    pi = new Personal_Information();
                    if (jsonObj.has("work_location"))
                        pi.setWork_location(jsonObj.getString("work_location"));
                    if (jsonObj.has("birthday"))
                        pi.setBirthday(jsonObj.getString("birthday"));
                    if (jsonObj.has("phone_PI"))
                        pi.setPhone_PI(jsonObj.getString("phone_PI"));
                    if (jsonObj.has("sex__PI"))
                        pi.setSex__PI(jsonObj.getBoolean("sex__PI"));
                    if (jsonObj.has("email_PI"))
                        pi.setEmail_PI(jsonObj.getString("email_PI"));
                    if (jsonObj.has("address_PI"))
                        pi.setAddress_PI(jsonObj.getString("address_PI"));
                    if (jsonObj.has("personal_id"))
                        pi.setPersonal_id(jsonObj.getString("personal_id"));
                    if (jsonObj.has("name_PI"))
                        pi.setName_PI(jsonObj.getString("name_PI"));
                    if (jsonObj.has("avatar"))
                        pi.setAvatar(jsonObj.getString("avatar"));
                    if (jsonObj.has("id_PI")) {
                        pi.setId_PI(jsonObj.getLong("id_PI"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // luu thong tin vua load duoc vao SharedPreferences
                SharedPreferences preferences1 = getSharedPreferences(SystemUtils.PI, MODE_PRIVATE);
                SharedPreferences.Editor editor1 = preferences1.edit();
                editor1.putString(SystemUtils.NAME_PI, pi.getName_PI());
                editor1.putString(SystemUtils.EMAIL_PI, pi.getEmail_PI());
                editor1.putString(SystemUtils.AVATAR_PI, pi.getAvatar());
                editor1.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void CheckNetwork() {
            Utility.showDialog(LoginActivity.this, "Xử lý...", "Kết nối đến server, vui lòng đợi...", false);
    		Thread thrd = new Thread()
    	    {
    		    public void run()
    		    {
    		        Looper.prepare();
    		    	networkError = !checkNetwork(LoginActivity.this);

    		    	uiCheckNetworkCallback.sendEmptyMessage(0);

    		        Looper.loop();
    		    }
    	    };
    	    thrd.start();
    	}

    public static boolean checkNetwork(Activity context)
    {
        final ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        if(netInfo == null || !netInfo.isConnected())
            return false;
        return true;
    }

    private Handler uiCheckNetworkCallback = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Utility.closeDialog();
            if(networkError)
            {

                {
                    try {
                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                        ab.setTitle("Thông Báo");
                        ab.setMessage("Không thể kết nối đến server, xin hãy thử lại sau?");
                        ab.setPositiveButton("Tắt", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton)
                            {
                                finish();
                            }
                        });

                        ab.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        finish();

                    }

                }
            }
        }
    };


}