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
    private String avatar;
    private String token;
    private Double lat_PI;
    private Double long_PI;
    private long id_user;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
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

    public User(String user_name, String password, User_Type user_type, String avatar, String token, Double lat_PI, Double long_PI, long id_user) {
        this.user_name = user_name;
        this.password = password;
        this.user_type = user_type;
        this.avatar = avatar;
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
                ", avatar='" + avatar + '\'' +
                ", token='" + token + '\'' +
                ", lat_PI=" + lat_PI +
                ", long_PI=" + long_PI +
                ", id_user=" + id_user +
                '}';
    }
}
