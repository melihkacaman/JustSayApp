package com.melihkacaman.serverapp.businnes;


import com.melihkacaman.serverapp.absoperation.OpServer;
import com.melihkacaman.serverapp.server.SClient;

import java.util.LinkedList;
import java.util.function.Consumer;

public class ServerManager implements OpServer {

    private static ServerManager serverManager = null;

    private static LinkedList<SClient> users;

    private int count = 0;

    private ServerManager(){
        users = new LinkedList<>();
    }

    @Override
    public boolean checkUserNameForConvenience(final String username) {
        return findUserByUserName(username) == null;
    }

    public int getCount() {
        return count;
    }

    public void increaseCount() {
        count++;
    }

    public void addUser(SClient user){
        users.add(user);
    }


    private SClient findUserByUserName(String userName){
        SClient user = null;

        for (SClient sClient : users) {
            if (sClient.getUserName() != null && sClient.getUserName().equals(userName)){
                user = sClient;
            }
        }

        return user;
    }

    public static ServerManager getInstance(){
        if (serverManager == null){
            serverManager = new ServerManager();
        }

        return serverManager;
    }
}