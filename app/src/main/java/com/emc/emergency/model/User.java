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
    private String lat;
    private String lon;
    private ArrayList<Link> links;

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public User(int id_user, String user_name, String password, String id_user_type, String avatar, String token, String lat, String lon, ArrayList<Link> links) {
        this.id_user = id_user;
        this.user_name = user_name;
        this.password = password;
        this.id_user_type = id_user_type;
        this.avatar = avatar;
        this.token = token;
        this.lat = lat;
        this.lon = lon;
        this.links = links;
    }

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }

    public User() {
    }
}
