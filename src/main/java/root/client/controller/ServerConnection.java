package root.client.controller;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;

class ServerConnection implements Runnable {
    private static final String ENCODING = "UTF-8";
    public static final String IDENTIFIER="OLDRICH";

    private Socket socket;

    private Controller controller;

    private PrintWriter writer;
    private BufferedReader reader;

    ServerConnection(Controller controller) {

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
            sendMessage("Hello:"+IDENTIFIER);
            this.reader.readLine();
            String line;
            writer.println("ok");
            while ((line = this.reader.readLine()) != null) {
                System.out.println(line);
                controller.processMessage(line);
            }
        } catch (UnknownHostException | InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return null;
    }


    private void connect() {
        try {
            final InetAddress address = InetAddress.getLocalHost();
            socket = new Socket(address, 8002);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Thread.sleep(5000);
                //  connect();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    }
}
