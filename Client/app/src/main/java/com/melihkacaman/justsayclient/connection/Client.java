package com.melihkacaman.justsayclient.connection;

import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.FileMessage;
import com.melihkacaman.entity.Message;
import com.melihkacaman.entity.OperationType;
import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.model.ConvenienceUser;
import com.melihkacaman.justsayclient.model.ImgMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class Client {
    private static Thread connection;

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private String serverIP;
    private int serverPort;

    private Queue<UserListener> listenersOfUser;
    private Queue<RoomListener> listenersOfRoom;
    private Queue<ImgMessage> images;
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
        this.listenersOfRoom = new LinkedList<>();
        this.images = new LinkedList<>();
    }

    public void sendObject(Object object) {
        new ForwardServer(object).start();
    }

    public void sendRequestForUserList() {
        new ForwardServer(new Message<Void>(null, OperationType.SENDUSERNAMES)).start();
    }

    public void sendRequestForCreateRoom(Room room, RoomListener listener) {
        new ForwardServer(new Message<Room>(room, OperationType.CREATEROOM)).start();
        this.listenersOfRoom.add(listener);
    }

    public void sendRequestForRoomList() {
        new ForwardServer(new Message<User>(ClientInfo.me, OperationType.SENDROOMSLIST)).start();
    }

    public void sendRequestForJoiningRoom(Room room) {
        new ForwardServer(new Message<User>(room, OperationType.JOINROOM)).start();
    }

    public void sendChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getReceiver() instanceof Room) {
            new ForwardServer(new Message<ChatMessage>(chatMessage, OperationType.SENDROOMMESSAGE)).start();
        } else {
            new ForwardServer(new Message<ChatMessage>(chatMessage, OperationType.SENDCHATMESSAGE)).start();
        }
    }

    public void sendFileMessage(FileMessage fileMessage) {
        if (fileMessage.getFileType() == FileMessage.FileType.IMAGE) {
            ImgMessage imgMessage = new ImgMessage((byte[]) fileMessage.getFile(),
                    new FileMessage(fileMessage.getSender(), fileMessage.getReceiver(), null, FileMessage.FileType.IMAGE));
            new ForwardServer(new Message<Void>(null, OperationType.SENDIMAGE)).start();
            images.add(imgMessage);
        }
    }

    public void checkUserNameForConvenience(String username) {
        Message<String> message = new Message<>(username, OperationType.CHECKUSERNAME);
        sendObject(message);

        Thread thread = new Thread(() -> {
            Object ack = null;
            try {
                ack = input.readObject();
                if (ack instanceof User) {
                    ClientInfo.me = (User) ack;
                    EventBus.getDefault().post(new ConvenienceUser(true, ClientInfo.me));
                    listenServer.start();
                } else {
                    EventBus.getDefault().post(new ConvenienceUser(false, null));
                    throw new Exception("Client NACK!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        thread.start();
    }

    public void addUserListener(UserListener listener) {
        this.listenersOfUser.add(listener);
    }

    public static Client getInstance() {
        if (client == null) {
            if (connection == null) {
                connection = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        do {
                            try {
                                client = new Client("192.168.1.246", 5000);
                                Thread.sleep(1000);
                            } catch (IOException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        } while (client == null);
                    }
                });
                connection.start();
            }
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
            while (!socket.isClosed()) {
                try {
                    Object message = input.readObject();
                    if (message instanceof Message) {
                        switch (((Message) message).operationType) {
                            case SENDUSERNAMES:
                                User[] result = (User[]) ((Message) message).targetObj;
                                EventBus.getDefault().post(result);
                                break;
                            case CREATEROOM:
                                Room room = (Room) ((Message) message).targetObj;
                                RoomListener roomListener = listenersOfRoom.poll();
                                if (roomListener != null) {
                                    roomListener.getRoomInfo(room);
                                }
                                break;
                            case SENDROOMSLIST:
                                List<Room> rooms = (List<Room>) ((Message) message).targetObj;
                                EventBus.getDefault().post(rooms);
                                break;
                            case SENDCHATMESSAGE:
                                ChatMessage chatMessage = (ChatMessage) ((Message) message).targetObj;
                                ClientInfo.insertMessage(chatMessage);
                                EventBus.getDefault().post(chatMessage);
                                // TODO: 23.05.2021 notification will be added.
                                break;
                            case JOINROOM:
                                Room joinedRoom = (Room) ((Message) message).targetObj;
                                EventBus.getDefault().post(joinedRoom);
                                break;
                            case SENDIMAGE:
                                ImgMessage imgMessage = images.poll();
                                byte[] img = imgMessage.getImg();
                                if (img != null && img.length > 0) {
                                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                                    dOut.writeInt(img.length); // write length of the message
                                    dOut.write(img);
                                    Object ob = input.readObject();
                                    if (ob instanceof Message && ((Message) ob).operationType == OperationType.IMAGEINFO){
                                        ChatMessage chatMessage1 = new ChatMessage(imgMessage.getFileMessage().getSender(),
                                                imgMessage.getFileMessage().getReceiver(), "IMAGE");
                                        sendObject(new Message<ChatMessage>(chatMessage1, OperationType.IMAGEINFO));
                                    }
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
