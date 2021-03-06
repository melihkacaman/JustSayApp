package com.melihkacaman.serverapp.server;

import com.melihkacaman.entity.ACKType;
import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.FileMessage;
import com.melihkacaman.entity.Message;
import com.melihkacaman.entity.OperationType;
import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.serverapp.absoperation.OpClient;
import com.melihkacaman.serverapp.businnes.ServerManager;
import com.melihkacaman.serverapp.model.ImgMessage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SClient implements Runnable, OpClient {
    private int id;
    private Socket socket;
    private ObjectOutputStream cOutput;
    private ObjectInputStream cInput;
    private User user = null;

    private Queue<ImgMessage> imagesMessage;

    private ServerManager serverManager;

    public SClient(int id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;

        cOutput = new ObjectOutputStream(this.socket.getOutputStream());
        cInput = new ObjectInputStream(this.socket.getInputStream());

        System.out.println("[SClient.java] A Client is connected.");

        serverManager = ServerManager.getInstance();

        this.imagesMessage = new LinkedList<>();
    }

    public void addImage(ImgMessage message){
        imagesMessage.add(message);
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Object message = cInput.readObject();
                if (message instanceof Message) {
                    switch (((Message) message).operationType) {
                        case CHECKUSERNAME:
                            if (serverManager.checkUserNameForConvenience(((Message) message).targetObj.toString())) {
                                String userName = ((Message) message).targetObj.toString();
                                user = new User(userName);
                                cOutput.writeObject(user);
                            } else {
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
                            Room room = new Room(respondRoom.getUserName(), respondRoom.getTopic(), respondRoom.getOwner());
                            serverManager.increaseRoom();
                            serverManager.addRoom(room);
                            cOutput.writeObject(new Message<Room>(room, OperationType.CREATEROOM));
                            break;
                        case SENDROOMSLIST:
                            User userTarget = (User) ((Message) message).targetObj;
                            List<Room> rooms = serverManager.getRooms(userTarget);
                            cOutput.writeObject(new Message<List<Room>>(rooms, OperationType.SENDROOMSLIST));
                            break;
                        case SENDCHATMESSAGE:
                            ChatMessage chatMessage = (ChatMessage) ((Message) message).targetObj;
                            boolean ack = serverManager.sendChatMessageUserToUser(chatMessage);
                            if (!ack) {
                                // TODO: 22.05.2021 migth send ack, repeat the message again.
                            }
                            break;
                        case JOINROOM:
                            Room targetRoom = (Room) ((Message) message).targetObj;
                            targetRoom = serverManager.addUserToRoom(targetRoom, user);

                            if (targetRoom != null) {
                                cOutput.writeObject(new Message<Room>(targetRoom, OperationType.JOINROOM));
                            } else {
                                // nack
                                cOutput.writeObject(new Message<Room>(null, OperationType.JOINROOM));
                            }
                            break;
                        case SENDROOMMESSAGE:
                            ChatMessage roomMessage = (ChatMessage) ((Message) message).targetObj;
                            serverManager.sendRoomMessage(roomMessage);
                            break;
                        case SENDIMAGE:
                            cOutput.writeObject(new Message<Void>(null, OperationType.SENDIMAGE));
                            DataInputStream dIn = new DataInputStream(socket.getInputStream());
                            int length = dIn.readInt();                    // read length of incoming message
                            if (length > 0) {
                                byte[] img = new byte[length];
                                dIn.readFully(img, 0, img.length); // read the message
                                cOutput.writeObject(new Message<Void>(null, OperationType.IMAGEINFO));
                                Object ob = cInput.readObject();
                                if (ob instanceof Message && ((Message) ob).operationType == OperationType.IMAGEINFO) {
                                    ChatMessage fileMessage = (ChatMessage) ((Message) ob).targetObj;
                                    if (fileMessage.getReceiver() instanceof Room) {
                                        // This is a room.
                                        int roomId = fileMessage.getReceiver().getId();
                                        serverManager.sendFileToRoom(roomId, new ImgMessage(fileMessage, img));
                                    } else {
                                        // This is a user.
                                        SClient receiver = serverManager.findUserById(fileMessage.getReceiver().getId());
                                        serverManager.sendFile(receiver,new ImgMessage(fileMessage, img));
                                    }
                                }
                            }
                            break;
                        case RECEIVEIMAGE:
                            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                            ImgMessage imgMessage = imagesMessage.poll();
                            dOut.writeInt(imgMessage.img.length); // write length of the message
                            dOut.write(imgMessage.img);

                            Object o2 = cInput.readObject();
                            if (o2 instanceof Message && ((Message) o2).operationType == OperationType.IMAGEINFO) {
                                cOutput.writeObject(new Message<ChatMessage>(imgMessage.chatMessage, OperationType.IMAGEINFO));
                            }
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
    public boolean sendMessage(ChatMessage message) {
        try {
            cOutput.writeObject(new Message<ChatMessage>(message, OperationType.SENDCHATMESSAGE));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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

    @Override
    public void sendMessage(Object object) {
        try {
            cOutput.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return this.user;
    }
}

