package com.melihkacaman.serverapp.model;

import com.melihkacaman.entity.ChatMessage;

public class ImgMessage {
    public ChatMessage chatMessage;
    public byte[] img;

    public ImgMessage(ChatMessage chatMessage, byte[] img) {
        this.chatMessage = chatMessage;
        this.img = img;
    }
}
