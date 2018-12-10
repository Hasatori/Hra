package server.protocol.lobby;

/**
 * Handles input from LobbyProtocol messages.
 */
public class LobbyProtocolIn {

    private final String message;
    private static final String SENT_MESSAGE = "SENT MESSAGE";
    private static final String SET_MAP = "SET MAP ";
    private static final String START_GAME = "START GAME";
    private static final String DESTROY_LOBBY = "DESTROY LOBBY";
    private static final String LEAVE_LOBBY = "LEAVE LOBBY";

    LobbyProtocolIn(String message) {
        this.message = message;
    }

    public boolean setMap(){
        return message.matches(SET_MAP + "\\w+");
    }

    public String getMapName(){
        return message.replace(SET_MAP,"");
    }

    public boolean startGame(){
        return message.matches(START_GAME);
    }

    public boolean destroyLobby(){
        return message.matches(DESTROY_LOBBY);
    }

    public boolean leaveLobby(){
        return message.matches(LEAVE_LOBBY);
    }

    public boolean sendLobbyMessage(){
        return message.startsWith(SENT_MESSAGE);
    }
}
