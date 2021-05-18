package com.melihkacaman.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Room extends BaseEntity {
    private int id;
    private String name;
    private String topic;

    private List<User> users;
    private User owner;

    private Date creationTime;
    private Date lastUpdatedTime;

    // List<Message> // Todo: When the Message Structure is ready.


    public Room(int id, String name, String topic, User owner) {
        this.id = id;
        this.name = name;
        this.topic = topic;
        this.owner = owner;

        this.users = new LinkedList<>();
        this.creationTime = new Date(System.currentTimeMillis());
        this.lastUpdatedTime = new Date(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getOwner() {
        return owner;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void addUser(User user){
        users.add(user);
    }
}
