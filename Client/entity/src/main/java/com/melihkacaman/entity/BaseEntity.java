package com.melihkacaman.entity;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    private static int idCounter = 0;
    public static synchronized int getID(){
        return idCounter++;
    }
}
