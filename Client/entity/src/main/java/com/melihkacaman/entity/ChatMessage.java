package com.melihkacaman.entity;

import java.util.Date;

public class ChatMessage extends  BaseEntity {
    private int id;
    private User sender;
    private User receiver;
    private long createdAt;
    private String Message;

    public ChatMessage(int id, User sender, User receiver, long createdAt, String message) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.createdAt = createdAt;
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
