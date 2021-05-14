package com.melihkacaman.entity;

public class Message<T> {
    public T targetObj;
    public OperationType operationType;

    public Message(T targetObj, OperationType operationType) {
        this.targetObj = targetObj;
        this.operationType = operationType;
    }
}
