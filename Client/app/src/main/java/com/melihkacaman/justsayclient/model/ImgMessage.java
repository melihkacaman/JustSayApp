package com.melihkacaman.justsayclient.model;

import com.melihkacaman.entity.ChatMessage;
import com.melihkacaman.entity.FileMessage;

public class ImgMessage {
    private byte[] img;
    private FileMessage fileMessage;

    public ImgMessage(byte[] img, FileMessage fileMessage) {
        this.img = img;
        this.fileMessage = fileMessage;
    }

    public byte[] getImg() {
        return img;
    }

    public FileMessage getFileMessage() {
        return fileMessage;
    }
}
