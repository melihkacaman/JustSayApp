package com.melihkacaman.serverapp.absoperation;

import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.serverapp.model.ImgMessage;
import com.melihkacaman.serverapp.server.SClient;

public interface OpServer {
    boolean checkUserNameForConvenience(String username);
    Room addUserToRoom(Room room, User who);

    void sendFile(SClient receiver, ImgMessage message);

    void sendFileToRoom(int roomId, ImgMessage message);
}