package root.client.model.protocol.general;

import org.omg.PortableInterceptor.LOCATION_FORWARD;
import root.client.model.protocol.lobby.LobbyProtocol;

public class GeneralProtocolOut {

    GeneralProtocolOut() {

    }

    public String gretting(String identifier) {
        return GeneralProtocol.messagePrefix + ":" + "THIS IS " + identifier;
    }

    public String createLobby(String lobbyName) {
        return GeneralProtocol.messagePrefix + ":" + "SET MAP " + lobbyName;
    }

    public String getLobbies() {
        return GeneralProtocol.messagePrefix + ":" + "GET LOBBIES";
    }

    public String joinLobby(String lobbyName) {
        return GeneralProtocol.messagePrefix + ":" + "JOIN " + lobbyName;
    }

}
