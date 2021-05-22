package com.melihkacaman.entity;

import java.util.Date;

public class ChatMessage extends  BaseEntity {
    private int id;
    private User sender;
    private User receiver;
    private long createdAt;
    private String Message;

    private static int idCounter = 0;
    private static synchronized int getID(){
        return idCounter++;
    }

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
}
