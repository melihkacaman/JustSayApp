package com.melihkacaman.serverapp.absoperation;

import com.melihkacaman.entity.ACKType;
import com.melihkacaman.entity.ChatMessage;

public interface OpClient {
    boolean sendMessage(ChatMessage message);
    void sendMessage(String message);
    void ACK(ACKType ackType);
    void sendMessage(Object object);
}
