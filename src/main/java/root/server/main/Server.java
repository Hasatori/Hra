package root.server.main;

import root.server.clientservices.ClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static final String ENCODING = "UTF-8";

    public static void main(String[] args) {
        try {
            System.out.println("Starting the server");
            ServerSocket serverSocket = new ServerSocket(8002);

            while (true) {

                System.out.println("Waiting for socket...");
                final Socket socket;
                socket = serverSocket.accept();
                new Thread(new ClientConnection(socket)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}
