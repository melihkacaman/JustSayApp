package com.melihkacaman.entity;

public class FileMessage extends ChatMessage {
    private Object file;
    private FileType fileType;
    private static final long serialVersionUID = 42L;

    public FileMessage(User sender, User receiver, Object file ,FileType type) {
        super(sender, receiver, "IMAGE");
        this.file = file;
        this.fileType = type;
    }

    public enum FileType{
        IMAGE,
        PNG,
    }

    public Object getFile() {
        return file;
    }

    public FileType getFileType() {
        return fileType;
    }
}
