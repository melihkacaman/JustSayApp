package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket socket;
    private int port;
    private static int count = 0;

    private ListenThread listenThread;

    public Server(int port) throws IOException {
        this.port = port;
        this.socket = new ServerSocket(port);

        this.listenThread = new ListenThread();
    }

    public void listen(){

    }

    private class ListenThread extends Thread {
        private ListenThread(){

        }

        @Override
        public void run() {
            while (!Server.this.socket.isClosed()){
                System.out.println("[Server.java] Accepting state");
                try {
                    Socket nClient = Server.this.socket.accept();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
