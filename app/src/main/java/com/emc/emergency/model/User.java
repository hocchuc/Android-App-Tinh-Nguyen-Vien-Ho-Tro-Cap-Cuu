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
    private Double lat;
    private Double lon;

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

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public User() {
    }

    public User(int id_user, String user_name, String password, String id_user_type, String avatar, String token, Double lat, Double lon) {
        this.id_user = id_user;
        this.user_name = user_name;
        this.password = password;
        this.id_user_type = id_user_type;
        this.avatar = avatar;
        this.token = token;
        this.lat = lat;
        this.lon = lon;
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
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
