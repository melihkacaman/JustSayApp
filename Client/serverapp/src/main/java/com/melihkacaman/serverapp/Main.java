package com.melihkacaman.serverapp;

import com.melihkacaman.serverapp.server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(5000);
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}