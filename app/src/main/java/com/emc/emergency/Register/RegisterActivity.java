package com.emc.emergency.Register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.emc.emergency.Login.LoginActivity;
import com.emc.emergency.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.model.FlashMessage;
import com.emc.emergency.model.Personal_Infomation;
import com.emc.emergency.model.User;

import com.emc.emergency.utils.SystemUtils;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;
    EditText txtRegitersUsername;
    EditText txtRegisterPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    double lat = 0;
    double lng = 0;

    FlashMessage flashMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        Firebase.setAndroidContext(this);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
    }

    private void addControls() {
        txtRegitersUsername = (EditText) findViewById(R.id.registerEmail);
        txtRegisterPassword = (EditText) findViewById(R.id.registerPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        btnLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
    }

    private void addEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xulyDangKy();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void xulyDangKy() {
        final String username = txtRegitersUsername.getText().toString();
        final String pass = txtRegisterPassword.getText().toString();


        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pass.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        //JacksonConverter converter = JacksonConverter(new ObjectMapper());

        httpClient.addInterceptor(logging);  // <-- this is the important line!
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(SystemUtils.getServerBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();

        RegisterClient client = retrofit.create(RegisterClient.class);
        final Call<FlashMessage> call = client.registerAccount(new User(username, pass));

        //create user
        auth.createUserWithEmailAndPassword(username, pass)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            final Personal_Infomation pi = new Personal_Infomation();
                            call.enqueue(new Callback<FlashMessage>() {
                                @Override
                                public void onResponse(Call<FlashMessage> call, Response<FlashMessage> response) {
                                    if (response.isSuccessful()) {
                                        if (response.body() != null) {
                                            flashMessage = response.body();
                                            Log.d("bodyRegis", flashMessage.toString());

                                        }
                                        try {
                                            if (flashMessage.getStatus().equals("SUCCESS")) {

                                                Long id_user = Long.parseLong(flashMessage.getMessage());
                                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                                String userId = auth.getCurrentUser().getUid();

                                                SharedPreferences preferences = getSharedPreferences("UID", MODE_PRIVATE);
                                                SharedPreferences.Editor editor1 = preferences.edit();
                                                editor1.putString("iduser_uid",userId);
                                                editor1.commit();

                                                // pushing user to 'users' node using the userId
                                                mDatabase.child(userId).setValue(new User(username, pass, lat, lng, id_user));

                                                pi.setEmail_PI(username);
                                                pi.setSex__PI(true);
//                                                pi.setPersonal_id(null);
                                                Gson gson = new Gson();
                                                String json = gson.toJson(pi);

                                                //tao va gui PI len server
                                                OkHttpClient client = new OkHttpClient();
                                                MediaType mediaType = MediaType.parse("application/json");
                                                RequestBody body = RequestBody.create(mediaType, json);
                                                Request request = new Request.Builder()
                                                        .url(SystemUtils.getServerBaseUrl() + "personal_Infomations")
                                                        .post(body)
                                                        .addHeader("content-type", "application/json")
                                                        .build();
                                                //-------------------------------------------------------------------
                                                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                                                if (SDK_INT > 8) {
                                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                                                            .permitAll().build();
                                                    StrictMode.setThreadPolicy(policy);
                                                    try {
                                                        okhttp3.Response response2 = client.newCall(request).execute();

                                                        Personal_Infomation pi = new Personal_Infomation();
                                                        String jsonPI = response2.body().string();

                                                        JSONObject jsonObject = null;
                                                        try {
                                                            jsonObject = new JSONObject(jsonPI);
                                                            for (int i = 0; i < jsonObject.length(); i++) {
                                                                if (jsonObject == null) continue;
                                                                if (jsonObject.has("id_PI"))
                                                                    pi.setId_PI(jsonObject.getLong("id_PI"));
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
//                                                        Log.d("ID_PI",pi.toString());

                                                        //Lien ket moi quan he
                                                        OkHttpClient client1 = new OkHttpClient();

                                                        String link_id_user = SystemUtils.getServerBaseUrl() + "users/" + id_user+"\n";

                                                        MediaType mediaType1 = MediaType.parse("text/uri-list");
                                                        RequestBody body1 = RequestBody.create(mediaType1, link_id_user);
                                                        Request request1 = new Request.Builder()
                                                                .url(SystemUtils.getServerBaseUrl() + "personal_Infomations/" + pi.getId_PI() + "/id_user")
                                                                .put(body1)
                                                                .addHeader("content-type", "text/uri-list")
                                                                .build();
                                                        okhttp3.Response response3 = client1.newCall(request1).execute();

                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }

                                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                intent.putExtra("action","registed");
                                                intent.putExtra("username",username);
                                                intent.putExtra("password",pass);
                                                startActivity(intent);

                                                finish();
                                            } else {
                                                Toast.makeText(getApplication(), "Email đã tồn tại!!!", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<FlashMessage> call, Throwable t) {

                                }
                            });
                        }
                    }
                });
    }
}