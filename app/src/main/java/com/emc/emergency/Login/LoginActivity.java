package com.emc.emergency.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dd.processbutton.iml.ActionProcessButton;
import com.emc.emergency.Chat.IRequestListener;
import com.emc.emergency.Chat.TokenService;
import com.emc.emergency.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.model.FlashMessage;
import com.emc.emergency.model.User;
import com.emc.emergency.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements IRequestListener {
    ActionProcessButton btnLogin;
    EditText txtUsername;
    EditText txtPassword;
    SharedPreferences sharedPreferences;
    String userState = "StoreUserState";
    private TokenService tokenService;
    private Utils utils;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtUsername = (EditText) findViewById(R.id.loginEmail);
        txtPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (ActionProcessButton) findViewById(R.id.btnLogin);
        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);

    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                User user = new User(username, password);
                btnLogin.setProgress(1);
                xulyDangNhap(user);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getSharedPreferences(userState, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Username", txtUsername.getText().toString());
        editor.putString("Password", txtPassword.getText().toString());
        //editor.commit();
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(userState, MODE_PRIVATE);
        String username = preferences.getString("Username", "");
        String password = preferences.getString("Password", "");
        txtUsername.setText(username);
        txtPassword.setText(password);
    }

    private void xulyDangNhap(User user) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);  // <-- this is the important line!
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://app-tnv-ho-tro-cap-cuu.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        LoginClient client = retrofit.create(LoginClient.class);
        Call<FlashMessage> call = client.loginAccount(user);
        call.enqueue(new Callback<FlashMessage>() {
            @Override
            public void onResponse(Call<FlashMessage> call, Response<FlashMessage> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        FlashMessage flashMessage = response.body();
                        Log.d("body", flashMessage.toString());
                    }
                    btnLogin.setProgress(0);
                    btnLogin.setText("Done");
                    FirebaseMessaging.getInstance().subscribeToTopic("test");
                    token = FirebaseInstanceId.getInstance().getToken();
                    Log.d("LoginToken", "Token: " + token);
                    //Call the token service to save the token in the database

                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<FlashMessage> call, Throwable t) {

            }
        });
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(String message) {

    }
}