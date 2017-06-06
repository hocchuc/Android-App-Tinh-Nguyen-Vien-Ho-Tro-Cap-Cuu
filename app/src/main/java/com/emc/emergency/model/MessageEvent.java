package com.emc.emergency.model;

public class MessageEvent {
    public final String type;
    public final String message;
 
    public MessageEvent(String type, String message) {
        this.type = type;
        this.message = message;
    }
}