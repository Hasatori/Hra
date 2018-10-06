package root.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

class ServerConnection implements Runnable {
    private static final String ENCODING = "UTF-8";
    private final Logger LOGGER = LoggerFactory.getLogger(ServerConnection.class);
    public static final String IDENTIFIER = "OLDRICH";
    private Socket socket;
    private SingleplayerMapController controller;
    private PrintWriter writer;
    private BufferedReader reader;

    ServerConnection(SingleplayerMapController controller) {

        this.controller = controller;

    }
    @Override
    public void run() {
        try {
            connect();
            Thread.sleep(200);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), ENCODING));
            this.reader = reader;
            this.writer = writer;
            sendMessage("Hello:" + IDENTIFIER);
            this.reader.readLine();
            String line;
            writer.println("ok");
            while ((line = this.reader.readLine()) != null) {
                System.out.println(line);

            }
        } catch (UnknownHostException | InterruptedException e) {
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
        }
    }


    public String sendMessage(String message) {
        System.out.println("Sending message " + message);
        writer.println(message);//Sends message to server
        try {
            String reply = reader.readLine();
            System.out.println(reply);
            return reply;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
        }
        return null;
    }


    private void connect() {
        try {
            final InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, 8002);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e.fillInStackTrace());
            try {
                Thread.sleep(5000);
                //  connect();
            } catch (InterruptedException e1) {
                LOGGER.error(e.getMessage(),e.fillInStackTrace());
            }
        }
    }
}
