package com.emc.emergency.Helper.Model;



/**
 * Created by Admin on 23/7/2017.
 */

public class UserJoined {
    private Long user_id;
    private Double long_userjoined;
    private Double lat_userjoined;

    public UserJoined() {
    }

    public UserJoined(Long user_id, Double long_userjoined, Double lat_userjoined) {
        this.user_id = user_id;
        this.long_userjoined = long_userjoined;
        this.lat_userjoined = lat_userjoined;
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

    public Double getLat_userjoined() {
        return lat_userjoined;
    }

    public void setLat_userjoined(Double lat_userjoined) {
        this.lat_userjoined = lat_userjoined;
    }

    @Override
    public String toString() {
        return "UserJoined{" +
                "user_id=" + user_id +
                ", long_userjoined=" + long_userjoined +
                ", lat_userjoined=" + lat_userjoined +
                '}';
    }
}
