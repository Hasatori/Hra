package root.client.model.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {
    private static final String ENCODING = "UTF-8";
    private final Logger LOGGER = LoggerFactory.getLogger(ServerConnection.class);
    private Socket socket;

    private IncomingMessageProcessor incommingMessageProccessor;

    public IncomingMessageProcessor getIncommingMessageProccessor() {
        return incommingMessageProccessor;
    }

    public OutgoingMessageProcessor getOutgoingMessageProccessor() {
        return outgoingMessageProccessor;
    }

    private OutgoingMessageProcessor outgoingMessageProccessor;

    public ServerConnection() {
        connect();
    }

    private void connect() {
        try {
            final InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, 8002);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), ENCODING));

            this.incommingMessageProccessor = new IncomingMessageProcessor(reader);
            this.outgoingMessageProccessor = new OutgoingMessageProcessor(writer);

        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e.fillInStackTrace());
        }
    }

    public boolean isConnected() {
        if (socket == null) {
            return false;
        }
        return socket.isConnected();
    }

    public void disconnect() {

    }
}
