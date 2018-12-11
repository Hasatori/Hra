package server.protocol.general;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * Handles output from GeneralProtocol messages.
 */
public class GeneralProtocolOut {

    GeneralProtocolOut() {

    }

    public String loginOk() {
        return GeneralProtocol.MSG_PREFIX + ":" + "CONNECTED";
    }

    public String sendLobbies(List<String> lobbies) {
        Type type = new TypeToken<LinkedList<String>>() {
        }.getType();
        return GeneralProtocol.MSG_PREFIX + ":" + new Gson().toJson(lobbies, type);
    }

    public String duplicateUserName() {
        return GeneralProtocol.MSG_PREFIX + ":DUPLICATE NAME";
    }

    public String lobbyIsFull() {
        return GeneralProtocol.MSG_PREFIX + ":LOBBY IS FULL";
    }

    public String duplicateLobbyName() {
        return GeneralProtocol.MSG_PREFIX + ":DUPLICATE LOBBY NAME";
    }

    public String lobbyCreated() {
        return GeneralProtocol.MSG_PREFIX + ":LOBBY CREATED";
    }

    public String connectedToLobby(String lobbyName, String ownerName, String mapName) {
        return String.format("%s:CONNECTED TO %s|%s|%s", GeneralProtocol.MSG_PREFIX, lobbyName, ownerName, mapName);
    }

    public String lobbyDoesNotExist(){
        return GeneralProtocol.MSG_PREFIX + ":LOBBY DOES NOT EXIST";
    }
}
