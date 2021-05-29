package com.melihkacaman.entity;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Serializable {
    private int id;
    private User sender;
    private User receiver;
    private long createdAt;
    private String Message;

    private static final long serialVersionUID = 42L;

    public ChatMessage(User sender, User receiver , String message) {
        this.id = getID();
        this.sender = sender;
        this.receiver = receiver;
        this.createdAt = new Date().getTime();
        Message = message;
    }

    public int getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getMessage() {
        return Message;
    }


    private static int idCounter = 10;
    private static synchronized int getID(){
        return idCounter++;
    }
}
