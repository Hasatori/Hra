package client.model.protocol.general;

import java.lang.reflect.Type;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GeneralProtocolIn {

    private final String message;

    GeneralProtocolIn(String message) {
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
        return message.equals("LOBBY IS FULL") ? true : false;
    }

    public boolean connectedToLobby(){
        return message.matches("CONNECTED TO \\w+ \\w+ \\w+");
    }
    public String[] getLobbyCredentials() {
        return message.replace("CONNECTED TO ", "").split(" ");
    }

    public boolean duplicateUserName() {
        return message.equals("DUPLICATE NAME");
    }
    public boolean duplicateLobbyName() {
        return message.equals("DUPLICATE LOBBY NAME");
    }
    public boolean lobbyCreated() {
        return message.equals("LOBBY CREATED");
    }
}
