package client.model.protocol.lobby;

public class LobbyProtocolOut {

	LobbyProtocolOut() {}	
	
    public String setMap(String mapName) {
        return LobbyProtocol.MSG_PREFIX + ":" + "SET MAP " + mapName;
    }

    public String kickOtherPlayer() {
        return LobbyProtocol.MSG_PREFIX + ":" + "KICK OTHER PLAYER";
    }

    public String startGame() {
        return LobbyProtocol.MSG_PREFIX + ":" + "START GAME";
    }

    public String leaveLobby() {
        return LobbyProtocol.MSG_PREFIX + ":" + "LEAVE LOBBY";
    }

    public String destroyLobby() {
        return LobbyProtocol.MSG_PREFIX + ":" + "DESTROY LOBBY";
    }
    
    public String sendLobbyMessage(String msg) {
        return LobbyProtocol.MSG_PREFIX + ":" + "SENT MESSAGE" + msg;
    }
}
