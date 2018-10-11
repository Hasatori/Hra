package root.server.main.protocol.general;

public class GeneralProtocolIn {

    private final String message;

    GeneralProtocolIn(String message) {
        this.message = message;
    }

    public boolean newUser() {
        return message.matches("THIS IS \\w+");
    }

    public String getNewUser() {
        return message.replace("THIS IS ", "");
    }

    public boolean wantLobbies() {
        return message.equals("GET LOBBIES") ? true : false;
    }

    public boolean wannaCreateLobby() {
        return message.matches("CREATE LOBBY \\w+ \\w+");
    }

    public boolean wannaJoinLobby() {
        return message.matches("JOIN LOBBY \\w+");
    }

    public String getLobbyName() {
            return message.replace("JOIN LOBBY ", "");
    }

    public String[] getLobbyAndMapName(){
        return message.replace("CREATE LOBBY ", "").split(" ");
    }
}
