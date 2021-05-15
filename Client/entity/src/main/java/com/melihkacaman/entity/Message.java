package com.melihkacaman.entity;

import java.io.Serializable;

public class Message<T> implements Serializable {
    public T targetObj;
    public OperationType operationType;

    public Message(T targetObj, OperationType operationType) {
        this.targetObj = targetObj;
        this.operationType = operationType;
    }
}
