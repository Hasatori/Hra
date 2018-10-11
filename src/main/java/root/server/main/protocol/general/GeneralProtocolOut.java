package root.server.main.protocol.general;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import root.server.main.protocol.lobby.LobbyProtocol;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class GeneralProtocolOut {

    GeneralProtocolOut() {

    }

    public String loginOk() {
        return GeneralProtocol.messagePrefix + ":" + "CONNECTED";
    }

    public String sendLobbies(List<String> lobbies) {
        Type type = new TypeToken<LinkedList<String>>() {
        }.getType();
        return GeneralProtocol.messagePrefix + ":" + new Gson().toJson(lobbies, type);
    }

    public String duplicateUserName() {
        return GeneralProtocol.messagePrefix + ":DUPLICATE NAME";
    }

    public String lobbyIsFull() {
        return GeneralProtocol.messagePrefix + ":LOBBY IS FULL";
    }

    public String duplicateLobbyName() {
        return GeneralProtocol.messagePrefix + ":DUPLICATE LOBBY NAME";
    }

    public String lobbyCreated() {
        return GeneralProtocol.messagePrefix + ":LOBBY CREATED";
    }

    public String connectedToLobby(String lobbyName, String ownerName, String mapName) {
        return String.format("%s:CONNECTED TO %s %s %s", GeneralProtocol.messagePrefix, lobbyName, ownerName, mapName);
    }
}
