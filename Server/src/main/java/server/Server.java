package server;

import controller.MainController;
import server.clientservices.ClientConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    public static final String ENCODING = "UTF-8";
    private static final Server INSTANCE = new Server();
    private ServerSocket serverSocket;
    private MainController mainController;
    private Integer port;

    private Server() {

    }

    public static Server getInstance() {
        return INSTANCE;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    public void stopServer() {
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

    }

    @Override
    public void run() {
        if (this.port == null ) {
            throw new IllegalStateException("Port is not set");
        } else {
            try {
                System.out.println("Starting the server");
                serverSocket = new ServerSocket(port);
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Waiting for socket...");
                    final Socket socket;
                    socket = serverSocket.accept();
                    new Thread(new ClientConnection(socket)).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
