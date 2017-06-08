package com.emc.emergency.model;

import java.io.Serializable;

/**
 * Created by Admin on 8/6/2017.
 */

public class Link implements Serializable {
    private String user;
    private String user_type;
    private String accident;
    private String personal_Infomation;
    private String chat;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getAccident() {
        return accident;
    }

    public void setAccident(String accident) {
        this.accident = accident;
    }

    public String getPersonal_Infomation() {
        return personal_Infomation;
    }

    public void setPersonal_Infomation(String personal_Infomation) {
        this.personal_Infomation = personal_Infomation;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public Link() {
    }

    public Link(String user, String user_type, String accident, String personal_Infomation, String chat) {
        this.user = user;
        this.user_type = user_type;
        this.accident = accident;
        this.personal_Infomation = personal_Infomation;
        this.chat = chat;
    }

    @Override
    public String toString() {
        return "Link{" +
                "user='" + user + '\'' +
                ", user_type='" + user_type + '\'' +
                ", accident='" + accident + '\'' +
                ", personal_Infomation='" + personal_Infomation + '\'' +
                ", chat='" + chat + '\'' +
                '}';
    }
}
