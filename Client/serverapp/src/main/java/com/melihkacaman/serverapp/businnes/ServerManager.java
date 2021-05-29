package com.melihkacaman.serverapp.businnes;


import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.serverapp.absoperation.OpServer;
import com.melihkacaman.serverapp.server.SClient;

import java.util.LinkedList;
import java.util.List;

public class ServerManager implements OpServer {

    private static ServerManager serverManager = null;

    private static LinkedList<SClient> users;
    private static LinkedList<Room> rooms;

    private int userCount = 0;
    private int roomCount = 0;

    private ServerManager() {
        users = new LinkedList<>();
        rooms = new LinkedList<>();
    }

    public boolean sendChatMessageUserToUser(ChatMessage message){
        for (SClient sClient : users) {
            if(message.getReceiver().getId() == sClient.getUser().getId()){
              return sClient.sendMessage(message);
            }
        }

        return false;
    }

    @Override
    public boolean checkUserNameForConvenience(final String username) {
        return findUserByUserName(username) == null;
    }

    @Override
    public Room addUserToRoom(Room room, User who) {
        for (Room item : rooms){
            if (item.getId() == room.getId()){
                if (!item.contains(who)){
                    item.addUser(who);
                    return item;
                }
            }
        }

        return null;
    }

    public int getUserCount() {
        return userCount;
    }

    public void increaseCount() {
        userCount++;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void increaseRoom() {
        roomCount++;
    }

    public void addUser(SClient user) {
        users.add(user);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    private SClient findUserByUserName(String userName) {
        SClient user = null;

        for (SClient sClient : users) {
            if (sClient.getUser() != null && sClient.getUser().getUserName().equals(userName)) {
                user = sClient;
            }
        }

        return user;
    }

    private SClient findUserById(int id) {
        SClient user = null;

        for (SClient sClient : users) {
            if (sClient.getUser() != null && sClient.getUser().getId() == id) {
                user = sClient;
            }
        }

        return user;
    }

    private Room findRoomById(int id) {
        for (Room item : rooms) {
            if (item.getId() == id) {
                return item;
            }
        }

        return null;
    }

    public User[] getUsers() {
        User[] result = new User[users.size()];
        int k = 0;
        for (SClient sClient : users) {
            if (sClient.getUser() != null) {
                result[k] = sClient.getUser();
                k++;
            }
        }

        return result;
    }

    public List<Room> getRooms(){
        return rooms;
    }

    public List<Room> getRooms(User outOfUser){
        List<Room> result = new LinkedList<>();
        for (Room room : rooms) {
            if (!room.getOwner().equals(outOfUser)){
                result.add(room);
            }
        }

        return result;
    }

    public static ServerManager getInstance() {
        if (serverManager == null) {
            serverManager = new ServerManager();
        }

        return serverManager;
    }

    public void sendRoomMessage(ChatMessage chatMessage) {
        Room room = findRoomById(chatMessage.getReceiver().getId());
        for(User item : room.getUsers()){
            if (item.getId() != chatMessage.getSender().getId()){
                findUserById(item.getId()).sendMessage(chatMessage);
            }
        }
    }
}