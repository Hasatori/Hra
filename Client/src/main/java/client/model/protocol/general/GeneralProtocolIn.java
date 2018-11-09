package client.model.protocol.general;

import java.lang.reflect.Type;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GeneralProtocolIn {

    private static final String LOBBY_FULL = "LOBBY IS FULL";
    private static final String LOBBY_CONNECTED_TO = "CONNECTED TO ";
    private static final String LOBBY_DUPLICATE_NAME = "DUPLICATE NAME";
    private static final String LOBBY_DUPLICATE_LOBBY = "DUPLICATE LOBBY NAME";
    private static final String LOBBY_CREATED = "LOBBY CREATED";
    private final String message;

    public GeneralProtocolIn(String message) {
        this.message = message;
    }

    public boolean wasLoginOk() {
        return message.equals("CONNECTED") ;
    }

    public LinkedList<String> getLobbies() {
        Type type = new TypeToken<LinkedList<String>>() {
        }.getType();
        return new Gson().fromJson(message, type);
    }

    public boolean lobbyFull() {
        return message.equals(LOBBY_FULL);
    }

    public boolean connectedToLobby(){
        return message.matches(LOBBY_CONNECTED_TO + "\\w+ \\w+ \\w+");
    }
    public String[] getLobbyCredentials() {
        return message.replace(LOBBY_CONNECTED_TO, "").split(" ");
    }

    public boolean duplicateUserName() {
        return message.equals(LOBBY_DUPLICATE_NAME);
    }
    public boolean duplicateLobbyName() {
        return message.equals(LOBBY_DUPLICATE_LOBBY);
    }
    public boolean lobbyCreated() {
        return message.equals(LOBBY_CREATED);
    }
}
