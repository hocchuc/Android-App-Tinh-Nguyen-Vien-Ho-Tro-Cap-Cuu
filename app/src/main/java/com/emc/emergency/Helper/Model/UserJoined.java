package com.emc.emergency.Helper.Model;


import java.sql.Date;

/**
 * Created by Admin on 23/7/2017.
 */

public class UserJoined {
    private Long user_id;
    private Double long_userjoined;
    private Double lat_userjoined;
    private String date;
    private String avatar;
    private String name;

    public UserJoined() {
    }

    public UserJoined(Long user_id, Double long_userjoined, Double lat_userjoined, String date, String avatar, String name) {
        this.user_id = user_id;
        this.long_userjoined = long_userjoined;
        this.lat_userjoined = lat_userjoined;
        this.date = date;
        this.avatar = avatar;
        this.name = name;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Double getLong_userjoined() {
        return long_userjoined;
    }

    public void setLong_userjoined(Double long_userjoined) {
        this.long_userjoined = long_userjoined;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Double getLat_userjoined() {
        return lat_userjoined;
    }

    public void setLat_userjoined(Double lat_userjoined) {
        this.lat_userjoined = lat_userjoined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserJoined{" +
                "user_id=" + user_id +
                ", long_userjoined=" + long_userjoined +
                ", lat_userjoined=" + lat_userjoined +
                ", date=" + date +
                ", avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
