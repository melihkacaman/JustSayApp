package com.melihkacaman.justsayclient.connection;

import com.melihkacaman.entity.Room;

import java.util.List;

public interface RoomListener {
    void getRoomInfo(Room room);
    void getRoomList(List<Room> rooms);
}