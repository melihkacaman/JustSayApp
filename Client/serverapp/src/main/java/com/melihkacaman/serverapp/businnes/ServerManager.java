package com.melihkacaman.serverapp.businnes;


import com.melihkacaman.serverapp.absoperation.OpServer;

public class ServerManager implements OpServer {

    private static ServerManager serverManager = null;
    private ServerManager(){

    }

    @Override
    public boolean checkUserName(String username) {
        return false;
    }


    public static ServerManager getInstance(){
        if (serverManager == null){
            serverManager = new ServerManager();
        }

        return serverManager;
    }
}