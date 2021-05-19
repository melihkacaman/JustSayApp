package com.melihkacaman.serverapp.businnes;


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

    @Override
    public boolean checkUserNameForConvenience(final String username) {
        return findUserByUserName(username) == null;
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
            if (sClient.getUserName() != null && sClient.getUserName().equals(userName)) {
                user = sClient;
            }
        }

        return user;
    }

    public User[] getUsers() {
        User[] result = new User[users.size()];
        int k = 0;
        for (SClient sClient : users) {
            if (!sClient.getUserName().isEmpty()) {
                result[k] = new User(sClient.getUserName(), sClient.getId());
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
}