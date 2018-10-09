package root.client.model.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import root.client.model.protocol.general.GeneralProtocol;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {
    private static final String ENCODING = "UTF-8";
    private final Logger LOGGER = LoggerFactory.getLogger(ServerConnection.class);
    private final String identifier;
    private Socket socket;

    private InputReader incommingMessageProccessor;

    public InputReader getIncommingMessageProccessor() {
        return incommingMessageProccessor;
    }

    public OutputWritter getOutgoingMessageProccessor() {
        return outgoingMessageProccessor;
    }

    private OutputWritter outgoingMessageProccessor;

    public ServerConnection(String identifier) {
        this.identifier = identifier;
        connect();

    }

    private void connect() {
        try {
            final InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, 8002);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), ENCODING));

            this.incommingMessageProccessor = new InputReader(reader);
            this.outgoingMessageProccessor = new OutputWritter(writer);
            outgoingMessageProccessor.sendMessage(new GeneralProtocol().send().gretting(this.identifier));

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
        try {
            this.socket.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
