package com.emc.emergency.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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

    private int id_user_type;

    private String image_link;

    private String token;

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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

    public int getId_user_type() {
        return id_user_type;
    }

    public void setId_user_type(int id_user_type) {
        this.id_user_type = id_user_type;
    }

    public User() {
    }

    public User(String user_name, String password) {
        this.user_name = user_name;
        this.password = password;
    }
}
