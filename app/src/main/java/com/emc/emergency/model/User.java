package com.emc.emergency.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 21/5/2017.
 */

public class User implements Serializable {
    @SerializedName("username")
    @Expose
    private String user_name;
    @SerializedName("password")
    @Expose
    private String password;

    private User_Type user_type;
    private String token;
    private Double lat_PI;
    private Double long_PI;
    private Long id_user;
    private boolean id_signup_volumteer;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User_Type getUser_type() {
        return user_type;
    }

    public void setUser_type(User_Type user_type) {
        this.user_type = user_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getLat_PI() {
        return lat_PI;
    }

    public void setLat_PI(Double lat_PI) {
        this.lat_PI = lat_PI;
    }

    public Double getLong_PI() {
        return long_PI;
    }

    public void setLong_PI(Double long_PI) {
        this.long_PI = long_PI;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public boolean getId_signup_volumteer() {
        return id_signup_volumteer;
    }

    public void setId_signup_volumteer(boolean id_signup_volumteer) {
        this.id_signup_volumteer = id_signup_volumteer;
    }

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public User(String user_name, String password, Double lat_PI, Double long_PI, Long id_user) {
        this.user_name = user_name;
        this.password = password;
        this.lat_PI = lat_PI;
        this.long_PI = long_PI;
        this.id_user = id_user;
    }

    public User(Double lat_PI, Double long_PI) {
        this.lat_PI = lat_PI;
        this.long_PI = long_PI;
    }

    public User(String user_name, String password, User_Type user_type, String token, Double lat_PI, Double long_PI, long id_user) {
        this.user_name = user_name;
        this.password = password;
        this.user_type = user_type;
        this.token = token;
        this.lat_PI = lat_PI;
        this.long_PI = long_PI;
        this.id_user = id_user;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", user_type=" + user_type +
                ", token='" + token + '\'' +
                ", lat_PI=" + lat_PI +
                ", long_PI=" + long_PI +
                ", id_user=" + id_user +
                '}';
    }
}
