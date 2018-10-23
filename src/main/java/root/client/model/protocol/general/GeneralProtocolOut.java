package root.client.model.protocol.general;

public class GeneralProtocolOut {

    GeneralProtocolOut() {

    }

    public String greeting(String identifier) {
        return GeneralProtocol.messagePrefix + ":" + "THIS IS " + identifier;
    }

    public String createLobby(String lobbyName, String mapName) {
        return GeneralProtocol.messagePrefix + ":" + "CREATE LOBBY " + lobbyName + "|" + mapName;
    }

    public String getLobbies() {
        return GeneralProtocol.messagePrefix + ":" + "GET LOBBIES";
    }

    public String joinLobby(String lobbyName) {
        return GeneralProtocol.messagePrefix + ":" + "JOIN LOBBY " + lobbyName;
    }

}
