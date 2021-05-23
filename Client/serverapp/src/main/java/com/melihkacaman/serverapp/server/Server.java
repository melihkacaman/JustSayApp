package com.melihkacaman.serverapp.server;

import com.melihkacaman.serverapp.businnes.ServerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket socket;
    private int port;
    private static int count = 0;

    private ListenThread listenThread;
    private ServerManager serverManager;

    public Server(int port) throws IOException {
        this.port = port;
        this.socket = new ServerSocket(port);

        this.listenThread = new ListenThread();
        this.serverManager = ServerManager.getInstance();
    }

    public void listen(){
        System.out.println("[Server.java] Server is listening.");
        listenThread.start();
    }

    private class ListenThread extends Thread {
        private ListenThread(){

        }

        @Override
        public void run() {
            while (!Server.this.socket.isClosed()){
                try {
                    synchronized (this){
                        Socket nClient = Server.this.socket.accept();
                        SClient sClient = new SClient(serverManager.getUserCount(), nClient);
                        new Thread(sClient).start();
                        serverManager.addUser(sClient);
                        serverManager.increaseCount();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
