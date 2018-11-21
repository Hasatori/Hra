package client.model.connection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ServerConnectionTest {
    private ServerConnection serverConnection;
    private static final int PORT = 8002;

    @BeforeEach
    void setUp() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(PORT);
                Socket socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        serverConnection = new ServerConnection("test", PORT);
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void isConnected() {
        assertTrue(serverConnection.isConnected());
    }

    @Test
    void disconnect() {
        serverConnection.disconnect();
        assertFalse(serverConnection.isConnected());
    }

}