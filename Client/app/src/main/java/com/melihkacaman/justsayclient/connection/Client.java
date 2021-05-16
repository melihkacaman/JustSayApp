package com.melihkacaman.justsayclient.connection;

import android.os.AsyncTask;

import com.melihkacaman.entity.ACKType;
import com.melihkacaman.entity.Message;
import com.melihkacaman.entity.OperationType;
import com.melihkacaman.entity.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private String serverIP;
    private int serverPort;

    private Queue<UserListener> listenersOfUser;
    private ListenServer listenServer;

    private static Client client = null;
    private Client(String serverIP, int serverPort) throws IOException {
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        this.socket = new Socket(this.serverIP, this.serverPort);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());

        this.listenServer = new ListenServer();

        this.listenersOfUser = new LinkedList<>();
    }

    public void sendObject(Object object){
        new ForwardServer(object).start();
    }
    public void sendRequestForUserList(){
        new ForwardServer(new Message<Void>(null, OperationType.SENDUSERNAMES)).start();
    }
    public boolean checkUserNameForConvenience(String username) {
        AtomicBoolean result = new AtomicBoolean(false);
        Message<String> message = new Message<>(username, OperationType.CHECKUSERNAME);
        sendObject(message);

        Thread thread = new Thread(() -> {
            Object ackType = null;
            try {
                ackType = input.readObject();
                if (ackType == ACKType.SUCCESS)
                {
                    result.set(true);
                    listenServer.start();
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

        });
        try {
            thread.start();
            thread.join();// Todo : This locks main threads, change it with loading
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result.get();
    }

    public void addListener(UserListener listener){
        this.listenersOfUser.add(listener);
    }

    public static Client getInstance() {
        if (client == null)
        {
            new Thread(() -> {

                do {
                    try {
                        client = new Client("192.168.1.246", 5000);
                        Thread.sleep(1000);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (client == null);
            }).start();
        }

        return client;
    }

    private class ForwardServer extends Thread {
        Object message;
        ForwardServer(Object message) {
            this.message = message;
        }

        @Override
        public void run() {
            try {
                output.writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ListenServer extends Thread {
        @Override
        public void run() {
            while (!socket.isClosed()){
                try {
                    Object message = input.readObject();
                    if (message instanceof Message){
                        switch (((Message) message).operationType){
                            case SENDUSERNAMES:
                                User[] result = (User[]) ((Message) message).targetObj;
                                UserListener listener = listenersOfUser.peek();
                                if (listener != null){
                                    listener.getUsersInfo(result);
                                }else{
                                    // Send Nack
                                }
                                break;
                        }
                    }
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
