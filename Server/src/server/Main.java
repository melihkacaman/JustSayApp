package server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = new Server(6000);
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
