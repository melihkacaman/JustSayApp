package com.melihkacaman.entity;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private int id;

    // TODO: 23.05.2021 List<Room> rooms;
    
    public User(String userName) {
        this.userName = userName;
        this.id = getID();
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }



    private static int idCounter = 500;
    private static synchronized int getID(){
        return idCounter++;
    }
}
