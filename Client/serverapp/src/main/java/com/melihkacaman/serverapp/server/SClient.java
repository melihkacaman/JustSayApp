package com.melihkacaman.serverapp.server;


import com.melihkacaman.entity.ACKType;
import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.Message;
import com.melihkacaman.entity.OperationType;
import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.serverapp.absoperation.OpClient;
import com.melihkacaman.serverapp.businnes.ServerManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class SClient implements Runnable, OpClient {
    private int id;
    private Socket socket;
    private ObjectOutputStream cOutput;
    private ObjectInputStream cInput;
    private String userName;

    private ServerManager serverManager;

    public SClient(int id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;

        cOutput = new ObjectOutputStream(this.socket.getOutputStream());
        cInput = new ObjectInputStream(this.socket.getInputStream());

        System.out.println("[SClient.java] A Client is connected.");

        serverManager = ServerManager.getInstance();
    }

    @Override
    public void run() {
        while (!socket.isClosed()){
            try {
                Object message = cInput.readObject();
                if (message instanceof Message){
                    switch (((Message) message).operationType){
                        case CHECKUSERNAME:
                            if (serverManager.checkUserNameForConvenience(((Message) message).targetObj.toString())){
                                userName = ((Message) message).targetObj.toString();
                                cOutput.writeObject(new User(userName, id));
                            }else {
                                ACK(ACKType.FAILURE);
                            }
                            break;
                        case SENDUSERNAMES:
                            User[] userNames = serverManager.getUsers();
                            Message<User[]> result = new Message<>(userNames, OperationType.SENDUSERNAMES);
                            cOutput.writeObject(result);
                            // Todo: you migth use the readObj again to check ack from client.
                            break;
                        case CREATEROOM:
                            Room respondRoom = (Room) ((Message) message).targetObj;
                            Room room = new Room(serverManager.getRoomCount(), respondRoom.getName(), respondRoom.getTopic(), respondRoom.getOwner());
                            serverManager.increaseRoom();
                            serverManager.addRoom(room);
                            cOutput.writeObject(new Message<Room>(room, OperationType.CREATEROOM));
                            break;
                        case SENDROOMSLIST:
                            User user = (User) ((Message) message).targetObj;
                            List<Room> rooms = serverManager.getRooms(user);
                            cOutput.writeObject(new Message<List<Room>>(rooms, OperationType.SENDROOMSLIST));
                            break;
                        case SENDCHATMESSAGE:
                            ChatMessage chatMessage = (ChatMessage) ((Message) message).targetObj;

                            break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void ACK(ACKType ackType) {
        try {
            cOutput.writeObject(ackType);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }
}

