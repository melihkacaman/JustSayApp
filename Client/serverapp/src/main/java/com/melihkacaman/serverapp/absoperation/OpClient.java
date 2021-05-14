package com.melihkacaman.serverapp.absoperation;

import com.melihkacaman.entity.ACKType;

public interface OpClient {
    void sendMessage(String message);
    void ACK(ACKType ackType);

}
