package root.server.main.processors;


import root.server.main.Client;
import root.server.main.ClientConnection;
import root.server.main.ClientManager;
import root.server.main.protocol.general.GeneralProtocol;
import root.server.main.protocol.general.GeneralProtocolIn;

import java.io.IOException;
import java.net.Socket;

public class GeneralMessageProcessor extends MessageProcessor {
    private final GeneralProtocol protocol;

    public GeneralMessageProcessor(ClientConnection connection) {
        super(connection);
        this.protocol = new GeneralProtocol();

    }

    @Override
    public void processMessage(String message) throws IOException {
        GeneralProtocolIn in = this.protocol.get(message);
        if (in.newUser()) {
            if (!ClientManager.getInstance().isUserNameUnique(in.getNewUser())) {
                clientConnection.sendMessage(protocol.send().duplicateName());
            } else {
                Client client = new Client(in.getNewUser(), clientConnection);
                clientConnection.setClient(client);
                ClientManager.getInstance().add(client);
                clientConnection.sendMessage(protocol.send().loginOk());
            }
        }
        if (in.wantLobbies()) {
            this.clientConnection.sendMessage(protocol.send().sendLobbies(ClientManager.getInstance().getLobbies()));
        }
    }
}
