package com.melihkacaman.serverapp.absoperation;

import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;

public interface OpServer {
    boolean checkUserNameForConvenience(String username);
    Room addUserToRoom(Room room, User who);
}