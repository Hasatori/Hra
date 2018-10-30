package client.model.protocol.lobby;

public class LobbyProtocolIn {
    private final String message;
    private static final String SENT_MESSAGE = "SENT MESSAGE";

    LobbyProtocolIn(String message) {
        this.message = message;
    }

    public boolean playerHasLeft() {
        return message.equals("PLAYER HAS LEFT");
    }

    public boolean kicked() {
        return message.equals("YOU WERE KICKED OUT OF THE LOBBY");
    }

    public boolean setMap() {
        return message.matches("SET MAP \\w+");
    }

    public String getMap() {
        return message.replace("SET MAP ", "");
    }

    public boolean start() {
        return message.equals("START");
    }

    public boolean playerConnected() {
        return message.contains("CONNECTED PLAYER ");
    }

    public String getSecondPlayerName() {
        return message.replace("CONNECTED PLAYER ", "");
    }

    public boolean lobbyDeleted() {
        return message.equals("LOBBY DELETED");
    }
    
    public boolean messageSent() {
        return message.startsWith(SENT_MESSAGE);
    }
}
