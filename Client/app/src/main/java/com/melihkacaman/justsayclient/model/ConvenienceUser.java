package com.melihkacaman.justsayclient.model;

import com.melihkacaman.entity.User;

public class ConvenienceUser {
    private boolean isConvenience;
    private User acceptedUser;

    public ConvenienceUser(boolean isConvenience, User acceptedUser) {
        this.isConvenience = isConvenience;
        this.acceptedUser = acceptedUser;
    }

    public boolean isConvenience() {
        return isConvenience;
    }

    public User getAcceptedUser() {
        return acceptedUser;
    }
}
