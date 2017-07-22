package com.emc.emergency.Login;



import com.emc.emergency.Helper.Model.FlashMessage;
import com.emc.emergency.Helper.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Admin on 23/5/2017.
 */

public interface LoginClient {
    @POST("login")
    Call<FlashMessage> loginAccount(@Body User user);
    //public void getFeed(@Body User user, Callback<User> response);
}
