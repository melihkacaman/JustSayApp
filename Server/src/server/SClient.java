package server;

import absoperation.OpClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SClient implements Runnable, OpClient {
    private int id;
    private Socket socket;
    private ObjectOutputStream cOutput;
    private ObjectInputStream cInput;
    private String userName;

    public SClient(int id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;

        cOutput = new ObjectOutputStream(this.socket.getOutputStream());
        cInput = new ObjectInputStream(this.socket.getInputStream());

        System.out.println("[SClient.java] A Client is connected.");
    }

    @Override
    public void run() {
        while (!socket.isClosed()){
            try {
                Object message = cInput.readObject();
                // TODO: Message Listening
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendMessage(String message) {

    }
}
