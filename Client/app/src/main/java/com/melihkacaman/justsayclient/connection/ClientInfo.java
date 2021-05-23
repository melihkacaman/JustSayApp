package com.melihkacaman.justsayclient.connection;

import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.model.Chat;

import java.util.LinkedList;
import java.util.List;

public class ClientInfo {
    public static User me;
    private static List<Chat> chats = new LinkedList<>();

    public static List<Room> getMyRooms() {
        List<Room> rooms = new LinkedList<>();
        for (Chat chat : chats){
            if (chat.getWho() instanceof Room){
                rooms.add((Room) chat.getWho());
            }
        }

        return rooms;
    }

    public static void addChat(Chat chat) {
        chats.add(chat);
    }

    public static Chat getChatById(int id) {
        for (Chat chat : chats) {
            if (chat.getId() == id)
                return chat;
        }

        return null;
    }

    public static Chat checkPreviousChat(User user){
        for (Chat chat : chats) {
            if (chat.getWho().getId() == user.getId()){
                return chat;
            }
        }

        return null;
    }

}
