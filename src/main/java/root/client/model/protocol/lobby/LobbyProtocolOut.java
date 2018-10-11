package root.client.model.protocol.lobby;

import root.client.model.protocol.general.GeneralProtocol;

public class LobbyProtocolOut {

 LobbyProtocolOut(){

}
    public String setMap(String mapName) {
        return LobbyProtocol.messagePrefix + ":" + "SET MAP " + mapName;
    }

    public String kickOtherPlayer() {
        return LobbyProtocol.messagePrefix + ":" + "KICK OTHER PLAYER";
    }

    public String startGame() {
        return LobbyProtocol.messagePrefix + ":" + "START GAME";
    }

    public String leaveLobby() {
        return LobbyProtocol.messagePrefix + ":" + "LEAVE LOBBY";
    }

    public String destroyLobby() {
        return LobbyProtocol.messagePrefix + ":" + "DESTROY LOBBY";
    }
}


