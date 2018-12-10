package server;

import server.clientservices.ClientConnection;
import server.clientservices.ClientManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Singleton class of Server.
 * This class is the core of server side.
 */
public class Server implements Runnable {
    public static final String ENCODING = "UTF-8";
    private static final Server INSTANCE = new Server();
    private ServerSocket serverSocket;
    private Integer port;

    private Server() {

    }

    public static Server getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the port number for server to start on.
     * @param port number
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * Stops the server.
     */
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
                serverSocket = new ServerSocket(port);
                ClientManager.getInstance().getController().updateMessages("Server started listening on port "+port);
                while (!Thread.currentThread().isInterrupted()) {
                    ClientManager.getInstance().getController().updateMessages("Waiting for client");
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
