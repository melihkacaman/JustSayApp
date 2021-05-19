package com.melihkacaman.justsayclient.connection;

import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;

import java.util.LinkedList;
import java.util.List;

public class ClientInfo {
    public static User me;
    private static List<Room> myRooms = new LinkedList<>();

    public static List<Room> getMyRooms() {
        return myRooms;
    }

    public static void addRoom(Room room) {
        myRooms.add(room);
    }

    public static Room getRoomById(int id) {
        for (Room room : myRooms) {
            if (room.getId() == id)
                return room;
        }

        return null;
    }
}
