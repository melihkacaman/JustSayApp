package com.melihkacaman.entity;

public class FileMessage extends ChatMessage {
    private Object file;
    private FileType fileType;

    public FileMessage(User sender, User receiver, Object file ,FileType type ,String message) {
        super(sender, receiver, message);
        this.file = file;
        this.fileType = type;
    }

    public enum FileType{
        IMAGE,
        PNG,
    }
}
