package com.melihkacaman.justsayclient.connection;

import com.github.javafaker.Faker;
import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.Room;
import com.melihkacaman.entity.User;
import com.melihkacaman.justsayclient.model.Chat;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class ClientInfo {
    public static User me;
    private static Stack<Chat> chats = new Stack<>();

    public static List<Room> getMyRooms() {
        List<Room> rooms = new LinkedList<>();
        for (Chat chat : chats){
            if (chat.getWho() instanceof Room){
                rooms.add((Room) chat.getWho());
            }
        }

        return rooms;
    }

    public static void addChat(Chat chat) {
        chats.push(chat);
    }

    public static Chat getChatById(int id) {
        for (Chat chat : chats) {
            if (chat.getId() == id)
                return chat;
        }

        return null;
    }

    private static Chat getRoomChatById(int id) {
        for (Chat chat : chats) {
            if (chat.getWho() instanceof Room && chat.getWho().getId() == id)
                return chat;
        }

        return null;
    }

    public static Chat checkPreviousChat(User user){
        for (Chat chat : chats) {
            if (chat.getWho().getId() == user.getId()){
                return chat;
            }
        }

        return null;
    }

    public static Stack<Chat> getChats() {
        return chats;
    }

    public static void generateFakeData(){
        Faker fakeData = new Faker();
        Random rnd = new Random();
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            users.add(new User(fakeData.name().firstName()));
        }

        List<Room> rooms = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            Room room = new Room(fakeData.address().city(), fakeData.address().fullAddress(),
                    users.get(rnd.nextInt(9)));
            room.addUser(users.get(1));
            room.addUser(users.get(2));
            room.addUser(users.get(3));
            room.addUser(users.get(4));
            room.addUser(ClientInfo.me);
            rooms.add(room);
        }

        for (int i = 0; i < 10; i++) {
            Chat chat = new Chat(i % 2 == 0 ? users.get(rnd.nextInt(9)) : rooms.get(rnd.nextInt(4)));
            for (int j = 0; j < 50; j++) {
                if (chat.getWho() instanceof Room){
                    User sender = users.get(rnd.nextInt(9));
                    chat.addMessage(new ChatMessage(sender,chat.getWho(), fakeData.address().fullAddress()));

                }else {
                    chat.addMessage(new ChatMessage(j % 2 == 0 ? chat.getWho() : ClientInfo.me,
                            j % 2 == 0 ? ClientInfo.me : chat.getWho(), fakeData.address().fullAddress()));
                }
            }

            chats.add(chat);
        }

        System.out.println("Static Fake data is passed.");
    }

    public static void insertMessage(ChatMessage chatMessage) {
        if (chatMessage.getReceiver() instanceof Room){
            Chat roomChat = getRoomChatById(chatMessage.getReceiver().getId());
            roomChat.addMessage(chatMessage);
        }else {
            Chat prev = checkPreviousChat(chatMessage.getSender());
            if (prev == null){
                Chat brandChat = new Chat(chatMessage.getSender());
                brandChat.addMessage(chatMessage);
                chats.push(brandChat);
            }else {
                int id = findById(prev.getId());
                if (id != -1){
                    chats.remove(id);
                    prev.addMessage(chatMessage);
                    chats.push(prev);
                }
            }
        }
    }

    private static int findById(int chatId){
        int i = 0;
        for (Chat chat : chats) {
            if (chat.getId() == chatId){
                return i;
            }
            i++;
        }

        return -1;
    }
}
