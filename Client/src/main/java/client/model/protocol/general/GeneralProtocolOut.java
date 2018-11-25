package client.model.protocol.general;

public class GeneralProtocolOut {

    GeneralProtocolOut() {

    }

    public String greeting(String identifier) {
        return GeneralProtocol.MSG_PREFIX + ":" + "THIS IS " + identifier;
    }

    public String createLobby(String lobbyName, String mapName) {
        return GeneralProtocol.MSG_PREFIX + ":" + "CREATE LOBBY " + lobbyName + "|" + mapName;
    }

    public String getLobbies() {
        return GeneralProtocol.MSG_PREFIX + ":" + "GET LOBBIES";
    }

    public String joinLobby(String lobbyName) {
        return GeneralProtocol.MSG_PREFIX + ":" + "JOIN LOBBY " + lobbyName;
    }

    public String disconnect() {
        return GeneralProtocol.MSG_PREFIX + ":" + "DISCONNECT";
    }

}
