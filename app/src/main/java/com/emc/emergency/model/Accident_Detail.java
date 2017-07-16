package com.emc.emergency.model;

import java.util.Date;

/**
 * Created by Admin on 16/7/2017.
 */

public class Accident_Detail {
    private User user_id;
    private Accident accident_id;
    private Action_Type action_type_id;
    private Date date_create;

    public User getUser_id() {
        return user_id;
    }

    public void setUser_id(User user_id) {
        this.user_id = user_id;
    }

    public Accident getAccident_id() {
        return accident_id;
    }

    public void setAccident_id(Accident accident_id) {
        this.accident_id = accident_id;
    }

    public Action_Type getAction_type_id() {
        return action_type_id;
    }

    public void setAction_type_id(Action_Type action_type_id) {
        this.action_type_id = action_type_id;
    }

    public Date getDate_create() {
        return date_create;
    }

    public void setDate_create(Date date_create) {
        this.date_create = date_create;
    }

    @Override
    public String toString() {
        return "Accident_Detail{" +
                "user_id=" + user_id +
                ", accident_id=" + accident_id +
                ", action_type_id=" + action_type_id +
                ", date_create=" + date_create +
                '}';
    }

    public Accident_Detail(User user_id, Accident accident_id, Action_Type action_type_id, Date date_create) {
        this.user_id = user_id;
        this.accident_id = accident_id;
        this.action_type_id = action_type_id;
        this.date_create = date_create;
    }

    public Accident_Detail() {
    }
}
