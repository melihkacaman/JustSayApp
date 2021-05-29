package com.melihkacaman.entity;

import java.io.Serializable;

public class Message<T> implements Serializable {
    public T targetObj;
    public OperationType operationType;

    private static final long serialVersionUID = 42L;

    public Message(T targetObj, OperationType operationType) {
        this.targetObj = targetObj;
        this.operationType = operationType;
    }
}
