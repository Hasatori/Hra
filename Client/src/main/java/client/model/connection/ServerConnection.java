package client.model.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import client.model.protocol.general.GeneralProtocol;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Connection class for Server.
 */
public class ServerConnection {

    private static final String ENCODING = "UTF-8";
    private final int PORT;
    private final Logger LOGGER = LoggerFactory.getLogger(ServerConnection.class);
    private final String identifier;
    private Socket socket;
    private OutputWriter outgoingMessageProcessor;
    private InputReader incomingMessageProcessor;

    /**
     * @param identifier server connection indentifier
     * @param port server's port
     */
    public ServerConnection(String identifier, int port) {
        this.identifier = identifier;
        PORT = port;
        connect();
    }

    /**
     * Getter for incoming message processor
     * @return incoming message processor
     */
    public InputReader getIncomingMessageProcessor() {
        return incomingMessageProcessor;
    }

    /**
     * Getter for outgoing message processor
     * @return outgoing message processor
     */
    public OutputWriter getOutgoingMessageProcessor() {
        return outgoingMessageProcessor;
    }

    /**
     * Method for connecting to server.
     */
    private void connect() {
        try {
            final InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, PORT);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), ENCODING));

            this.incomingMessageProcessor = new InputReader(reader);
            this.outgoingMessageProcessor = new OutputWriter(writer);
            outgoingMessageProcessor.sendMessage(new GeneralProtocol().send().greeting(this.identifier));

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e.fillInStackTrace());
        }
    }

    /**
     * Checks if connection with server exists.
     * @return true=connection exists
     */
    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    /**
     * Disconnects from the server and closes the socket.
     */
    public void disconnect() {
        try {
            this.socket.close();
            socket=null;
        } catch (IOException e) {
            LOGGER.error("Error while closing socket {}",e);
        }
    }
}
