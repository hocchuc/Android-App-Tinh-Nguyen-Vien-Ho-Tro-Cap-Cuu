package com.emc.emergency.Register;


import com.emc.emergency.Helper.Model.FlashMessage;
import com.emc.emergency.Helper.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Admin on 23/5/2017.
 */

public interface RegisterClient {
    @POST("register")
    Call<FlashMessage> registerAccount(@Body User user);
}
