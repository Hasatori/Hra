package root.server.main;

import com.sun.xml.internal.ws.api.model.MEP;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ClientManager {
    private List<Client> clients = new LinkedList<>();

    private static ClientManager INSTANCE = new ClientManager();

    private ClientManager() {
    }

    public synchronized void add(Client client) {
        System.out.println("Client " + client.IDENTIFIER + " came");
        clients.add(client);
        System.out.println(this.toString());
    }

    public synchronized void remove(Client client) {
        System.out.println("Client " + client.IDENTIFIER + " tryMoveLeft");
        clients.remove(client);
        System.out.println(this.toString());
    }

    public synchronized static ClientManager getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized String toString() {
        return "\n Number of client: " + clients.size();
    }

    public void sendMessageToAll(String message) {
        if (clients.size() > 1) {
            clients.forEach(client -> {
                try {
                    if (client.IDENTIFIER.equals("OLDRICH")) {

                        client.CLIENT_CONNETION.sendMessage(message);

                    } else {
                        client.CLIENT_CONNETION.sendMessage("ok");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
