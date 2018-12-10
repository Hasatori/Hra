package server.protocol.general;

/**
 * Handles input from GeneralProtocol messages.
 */
public class GeneralProtocolIn {

    private static final String THIS_IS = "THIS IS ";
    private static final String GET_LOBBIES = "GET LOBBIES";
    private static final String CREATE_LOBBY = "CREATE LOBBY ";
    private static final String JOIN_LOBBY = "JOIN LOBBY ";
    private final String message;

    GeneralProtocolIn(String message) {
        this.message = message;
    }

    public boolean newUser() {
        return message.matches( THIS_IS + ".+");
    }

    public String getNewUser() {
        return message.replace(THIS_IS, "");
    }

    public boolean wantLobbies() {
        return message.equals(GET_LOBBIES);
    }

    public boolean wannaCreateLobby() {
        return message.matches(CREATE_LOBBY + ".*\\|.*");
    }

    public boolean wannaJoinLobby() {
        return message.matches(JOIN_LOBBY + ".*");
    }

    public String getLobbyName() {
            return message.replace(JOIN_LOBBY, "");
    }

    public String[] getLobbyAndMapName(){
        return message.replace(CREATE_LOBBY, "").split("\\|");
    }
}
