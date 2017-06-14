package com.emc.emergency.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 21/5/2017.
 */

public class User implements Serializable {
    private int id_user;

    @SerializedName("username")
    @Expose
    private String user_name;
    @SerializedName("password")
    @Expose
    private String password;
    private String id_user_type;
    private String avatar;
    private String token;
    private Double lat_PI;
    private Double long_PI;

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

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

    public String getId_user_type() {
        return id_user_type;
    }

    public void setId_user_type(String id_user_type) {
        this.id_user_type = id_user_type;
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

    public User() {
    }


    public User(int id_user, String user_name, String password, String id_user_type, String avatar, String token, Double lat_PI, Double long_PI) {
        this.id_user = id_user;
        this.user_name = user_name;
        this.password = password;
        this.id_user_type = id_user_type;
        this.avatar = avatar;
        this.token = token;
        this.lat_PI = lat_PI;
        this.long_PI = long_PI;
    }

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id_user=" + id_user +
                ", user_name='" + user_name + '\'' +
                ", password='" + password + '\'' +
                ", id_user_type='" + id_user_type + '\'' +
                ", avatar='" + avatar + '\'' +
                ", token='" + token + '\'' +
                ", lat_PI=" + lat_PI +
                ", long_PI=" + long_PI +
                '}';
    }
}
