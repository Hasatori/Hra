package root.server.main;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection implements Runnable {
    private Socket socket;
    private Client client;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), Server.ENCODING), true);// Without autoFlush method flush has to be called after every write(printl)
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Server.ENCODING));
            this.reader = reader;
            this.writer = writer;

            String line;
            while ((line = reader.readLine()) != null) {
                processMessage(line);
            }
            socket.close();
            ClientManager.getInstance().remove(client);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                ClientManager.getInstance().remove(client);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private synchronized void processMessage(String message) throws SocketException {
        System.out.println("Incomming message " + message);
        if (message.split(":")[0].equals("Hello")) {
            String clientIdentifier = message.split(":")[1];
            this.client = new Client(clientIdentifier, this);
            ClientManager.getInstance().add(client);
            writer.println("Hello " + clientIdentifier);
        } else {
            ClientManager.getInstance().sendMessageToAll(message);
        }
    }

    public void sendMessage(String message) throws IOException {
        System.out.println(message);
        writer.println(message);

    }
}
