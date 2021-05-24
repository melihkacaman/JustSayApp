package com.melihkacaman.justsayclient.model;

import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.connection.Client;
import com.melihkacaman.justsayclient.connection.ClientInfo;

import java.util.LinkedList;
import java.util.List;

public class Chat {
    private int id;
    private User who;
    private List<ChatMessage> messages;
    private String lastMessage;

    public Chat(User who) {
        this.who = who;
        this.id = getID();
        this.messages = new LinkedList<>();
        this.lastMessage = "";
    }

    public int getId() {
        return id;
    }

    public User getWho() {
        return who;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void addMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
        this.lastMessage = chatMessage.getSender().getUserName() + ": " + chatMessage.getMessage();
    }

    public String getLastMessage() {
        return lastMessage;
    }

    private static int idCounter = 0;
    private static synchronized int getID(){
        return idCounter++;
    }
}
