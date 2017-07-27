package com.emc.emergency.Register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;


import com.emc.emergency.Login.LoginActivity;
import com.emc.emergency.R;
import com.emc.emergency.Helper.Model.FlashMessage;
import com.emc.emergency.Helper.Model.Personal_Information;
import com.emc.emergency.Helper.Model.User;

import com.emc.emergency.Helper.Utils.SystemUtils;
import com.emc.emergency.Helper.Utils.Utility;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity Đăng ký tài khoản
 */
public class RegisterActivity extends AppCompatActivity {
    Button btnRegister, btnLogin, btnDate;
    EditText txtRegitersUsername, txtRegisterEmai, txtRegisterPassword, txtIn_Date, txtRegisterPhone;
    RadioButton radMale, radFalse;
    private ProgressBar progressBar;
    double lat = 0;
    double lng = 0;
    private int mYear, mMonth, mDay;
    String date;
    public Long id_user;
    Personal_Information pi;


    FlashMessage flashMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        addControls();
        addEvents();
    }

    private void addControls() {
        txtRegitersUsername = (EditText) findViewById(R.id.txtRegisName);
        txtRegisterEmai = (EditText) findViewById(R.id.txtRegisEmail);
        txtRegisterPassword = (EditText) findViewById(R.id.txtRegisPassword);
        txtIn_Date = (EditText) findViewById(R.id.txtIn_date);
        txtRegisterPhone = (EditText) findViewById(R.id.txtRegisPhoneNumer);
        radFalse = (RadioButton) findViewById(R.id.radMaleRegist);
        radMale = (RadioButton) findViewById(R.id.radMaleRegist);
        btnDate = (Button) findViewById(R.id.btnDate);

        pi = new Personal_Information();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        txtIn_Date.setText(dayOfMonth + "-" + (month + 1) + "-" + year);
                        date = year + "-" + (month + 1) + "-" + dayOfMonth;
                        txtIn_Date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        btnRegister = (Button) findViewById(R.id.btnRegist);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        btnLogin = (Button) findViewById(R.id.btnRegistLogin);
    }

    private void addEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserOnServer();
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

    private void createUserOnServer() {
        progressBar.setVisibility(View.VISIBLE);

        final String email = txtRegisterEmai.getText().toString();
        final String pass = txtRegisterPassword.getText().toString();
        final String dateRegis = txtIn_Date.getText().toString();
        final String MobilePhone = txtRegisterPhone.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(dateRegis)) {
            Toast.makeText(getApplicationContext(), "Enter date!", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(MobilePhone)) {
            Toast.makeText(getApplicationContext(), "Enter mobile phone number!", Toast.LENGTH_SHORT).show();
            return;
        } else if (pass.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }else if (Utility.validate(txtRegisterEmai.getText().toString())==false) {
            Toast.makeText(this, "Sai cú pháp - Example@gmail.com", Toast.LENGTH_SHORT).show();
            return;
        }  else {
            final HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        

            httpClient.addInterceptor(logging);  // <-- this is the important line!
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(SystemUtils.getServerBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build());

            Retrofit retrofit = builder.build();

            RegisterClient client = retrofit.create(RegisterClient.class);
            final Call<FlashMessage> call = client.registerAccount(new User(email, pass));

            call.enqueue(new Callback<FlashMessage>() {
                @Override
                public void onResponse(Call<FlashMessage> call, Response<FlashMessage> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            flashMessage = response.body();
//                        Log.d("bodyRegis", flashMessage.toString());

                        }
                        try {
                            if (flashMessage.getStatus().equals("SUCCESS")) {

                                id_user = Long.parseLong(flashMessage.getMessage());
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

                                // pushing user to 'users' node using the userId
                                mDatabase.push().setValue(new User(email, pass, lat, lng, id_user));

                                createPIOnServer();

                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra(SystemUtils.ACTION, SystemUtils.TYPE_REGISTED);
                                intent.putExtra("username", email);
                                intent.putExtra("password", pass);
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

                private void createPIOnServer() {
                    pi.setEmail_PI(email);
                    pi.setName_PI(txtRegitersUsername.getText().toString());
                    pi.setPhone_PI(txtRegisterPhone.getText().toString());
//                                                String date=txtRegisterYear.getText().toString()+"-"+selected+"-"+txtRegisterDay.getText().toString();
                    pi.setBirthday(date);
                    if (radMale.isChecked()) pi.setSex__PI(true);
                    else pi.setSex__PI(false);
                    Gson gson = new Gson();
                    String json = gson.toJson(pi);
//                            Log.d("Json",json);

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
                            putRelationUserPI();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                private void putRelationUserPI() throws IOException {
                    //Lien ket moi quan he
                    OkHttpClient client1 = new OkHttpClient();
                    Log.d("id_userPutRel", id_user.toString());

                    String link_id_user = SystemUtils.getServerBaseUrl() + "users/" + id_user;

                    MediaType mediaType = MediaType.parse("text/uri-list");
                    RequestBody body = RequestBody.create(mediaType, link_id_user);
                    Request request = new Request.Builder()
                            .url(SystemUtils.getServerBaseUrl() + "personal_Infomations/" + pi.getId_PI() + "/id_user")
                            .put(body)
                            .addHeader("content-type", "text/uri-list")
                            .build();

                    okhttp3.Response response = client1.newCall(request).execute();
                }

                @Override
                public void onFailure(Call<FlashMessage> call, Throwable t) {

                }
            });
        }
    }
}
//        //create user
//        auth.createUserWithEmailAndPassword(email, pass)
//                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.GONE);
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//
//                    }
//                });

