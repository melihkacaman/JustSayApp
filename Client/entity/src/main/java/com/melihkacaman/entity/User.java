package com.melihkacaman.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private int id;

    public User(String userName, int id) {
        this.userName = userName;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }
}
