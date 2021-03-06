package com.melihkacaman.entity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Room extends User {
    private String topic;

    private List<User> users;
    private User owner;

    private Date creationTime;
    private Date lastUpdatedTime;

    public Room(String name, String topic, User owner) {
        super(name);

        this.topic = topic;
        this.owner = owner;

        this.users = new LinkedList<>();
        this.creationTime = new Date(System.currentTimeMillis());
        this.lastUpdatedTime = new Date(System.currentTimeMillis());

        this.users.add(owner);
    }
    private static final long serialVersionUID = 42L;
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
        if(!contains(user)){
            users.add(user);
        }
    }

    public boolean contains (User user){
        for (User item : users) {
            if (user.getId() == item.getId()){
                return true;
            }
        }

        return false;
    }
}
