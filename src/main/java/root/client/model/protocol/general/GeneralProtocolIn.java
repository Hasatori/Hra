package root.client.model.protocol.general;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;

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

    public String[] connectedToLobby() {
        return message.replace("CONNECTED TO ", "").split(" ");
    }

    public boolean duplicateName() {
        return message.equals("DUPLICATE NAME");
    }
}
