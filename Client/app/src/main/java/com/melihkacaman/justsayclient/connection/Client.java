package com.melihkacaman.justsayclient.connection;

import com.melihkacaman.entity.ACKType;
import com.melihkacaman.entity.Message;
import com.melihkacaman.entity.OperationType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private String serverIP;
    private int serverPort;

    private static Client client = null;
    private Client(String serverIP, int serverPort) throws IOException {
        this.serverIP = serverIP;
        this.serverPort = serverPort;

        this.socket = new Socket(this.serverIP, this.serverPort);
        this.output = new ObjectOutputStream(socket.getOutputStream());
        this.input = new ObjectInputStream(socket.getInputStream());
    }

    public void sendObject(Object object){
        try {
            output.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUserNameForConvenience(String username){
        AtomicBoolean result = new AtomicBoolean(false);
        Message<String> message = new Message<>(username, OperationType.CHECKUSERNAME);
        sendObject(message);

        new Thread(() -> {
            // TODO: 14.05.2021 | might be necessary while.
            try {
                Object ackType = input.readObject();
                if (ackType == ACKType.SUCCESS)
                    result.set(true);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });

        return result.get();
    }

    public static Client getInstance() throws IOException {
        if (client == null)
            client = new Client("127.0.0.1", 6000);

        return client;
    }
}
