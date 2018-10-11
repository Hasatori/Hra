package root.client.model.protocol.lobby;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;

public class LobbyProtocolIn {
    private final String message;

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
}
