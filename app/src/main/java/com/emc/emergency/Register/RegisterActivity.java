package com.emc.emergency.Register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.emc.emergency.Login.LoginActivity;
import com.emc.emergency.R;
import com.emc.emergency.model.FlashMessage;
import com.emc.emergency.model.User;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister,btnLogin;
    EditText txtRegitersUsername;
    EditText txtRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtRegitersUsername = (EditText) findViewById(R.id.registerEmail);
        txtRegisterPassword = (EditText) findViewById(R.id.registerPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
    }

    private void addEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user=new   User(
                        txtRegitersUsername.getText().toString(),
                        txtRegisterPassword.getText().toString()
                );
                xulyDangKy(user);
             }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void xulyDangKy(User user) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …
// add logging as last interceptor
        //JacksonConverter converter = JacksonConverter(new ObjectMapper());

        httpClient.addInterceptor(logging);  // <-- this is the important line!
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.3.2:8080/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();

        RegisterClient client = retrofit.create(RegisterClient.class);
        Call<FlashMessage> call = client.registerAccount(user);
        call.enqueue(new Callback<FlashMessage>() {
            @Override
            public void onResponse(Call<FlashMessage> call, Response<FlashMessage> response) {
               if(response.isSuccessful()) {
                   if (response.body()!=null) {
                       FlashMessage flashMessage= response.body();
                       Log.d("body",flashMessage.toString());
                       Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                       startActivity(intent);
                   }

               }
               else { // if response.isSuccessful == false
                   ResponseBody body = response.errorBody();
                       try {
                           Log.d("body", body.string());
                           Toast.makeText(getApplication(),"Email đã tồn tại!!!",Toast.LENGTH_SHORT).show();
                       } catch (IOException e) {
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