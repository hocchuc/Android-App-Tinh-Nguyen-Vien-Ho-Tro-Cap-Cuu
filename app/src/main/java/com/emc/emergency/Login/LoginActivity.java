package com.emc.emergency.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.emc.emergency.Chat.IRequestListener;
import com.emc.emergency.Chat.TokenService;
import com.emc.emergency.Fragment.fragment_personal_info_page;
import com.emc.emergency.MainMenuActivity;
import com.emc.emergency.R;
import com.emc.emergency.Register.RegisterActivity;
import com.emc.emergency.model.FlashMessage;
import com.emc.emergency.model.Personal_Infomation;
import com.emc.emergency.model.User;
import com.emc.emergency.utils.GPSTracker;
import com.emc.emergency.utils.SystemUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements IRequestListener,fragment_personal_info_page.OnListFragmentInteractionListener {
    ActionProcessButton btnLogin;
    Button btnSignUp;
    EditText txtUsername;
    EditText txtPassword;
    SharedPreferences preferences,preferences1;
    String userState = "StoreUserState";
    String id_user="ID_USER";
    private TokenService tokenService;
     double latitude = 0;
    double longitude = 0;
//    private Utils utils;
    String token;
    int id = 0;
    FlashMessage flashMessage;

    private ProgressBar progressBar;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        addControls();
        addEvents();
    }

    private void addControls() {
        txtUsername = (EditText) findViewById(R.id.loginEmail);
        txtPassword = (EditText) findViewById(R.id.loginPassword);
        btnLogin = (ActionProcessButton) findViewById(R.id.btnLogin);
        btnLogin.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignUp= (Button) findViewById(R.id.btnSignUp);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tokenService=new TokenService(this,this);

    }

    private void addEvents() {
        GPSTracker gps = new GPSTracker(LoginActivity.this);
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                User user = new User(username, password);
                user.setLat_PI(latitude);
                user.setLong_PI(longitude);
                btnLogin.setProgress(1);
                xulyDangNhap(user);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
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
        //editor.commit();
        editor.apply();

    }

    @Override
    protected void onResume() {
        super.onResume();
        String username = "";
        String password = "";
        Intent intent = getIntent();
        if(intent.hasExtra("action")) {
            if(intent.getStringExtra("action").equals("registed")) {
                username = intent.getStringExtra("username");
                password = intent.getStringExtra("password");
            }

        }
        else {
            preferences = getSharedPreferences(userState, MODE_PRIVATE);
            username = preferences.getString("Username", "");
            password = preferences.getString("Password", "");
        }


        txtUsername.setText(username);
        txtPassword.setText(password);

        progressBar.setVisibility(View.GONE);
    }

    private void xulyDangNhap(final User user) {
        if (TextUtils.isEmpty(user.getUser_name())) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(user.getPassword())) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
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
                    if (response.isSuccessful()) {
                        if (response.body() != null )
                        {
                            flashMessage = response.body();
//                        Log.d("body", flashMessage.toString());

                        }
                        try {
                            if(flashMessage.getStatus().equals("SUCCESS")){
                                id= Integer.parseInt(flashMessage.getMessage()); // khi sai mat khau thi vang o day
                                Log.d("ID_USER", String.valueOf(id));
                                preferences1 = getSharedPreferences(id_user, MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = preferences1.edit();
                                editor1.putInt("id_user",id);
                                editor1.commit();
                                btnLogin.setProgress(0);
                                btnLogin.setText("Done");

                                token = FirebaseInstanceId.getInstance().getToken();
                                tokenService.registerTokenInDB(token,id+"");

                                //authenticate user
                                auth.signInWithEmailAndPassword(user.getUser_name(), user.getPassword())
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                // the auth state listener will be notified and logic to handle the
                                                // signed in user can be handled in the listener.
                                                if (!task.isSuccessful()) {
                                                    // there was an error
                                                    if (txtPassword.length() < 6) {
                                                        txtPassword.setError("Mật khẩu phải từ 6 ký tự trở lên. ");
                                                    } else {
                                                        Toast.makeText(LoginActivity.this,"auth bị lỗi", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            }
                                        });

//                                                Log.d("editor1",editor1.toString());
                                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(),"Đăng nhập không thành công!",Toast.LENGTH_LONG).show();
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

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(String message) {

    }
    @Override
    public void onListFragmentInteraction(Personal_Infomation mItem) {

    }
}