package root.server.main;

import java.io.IOException;

public class Lobby {

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
        if (this.otherPlayer != null) {
            try {
                this.otherPlayer.getClientConnection().sendMessage("LOBBY:SETMAP " + mapName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String capacity;

    public String getCapacity() {
        return this.capacity;
    }

    public void start() {
        if (this.otherPlayer != null) {
            try {
                otherPlayer.getClientConnection().sendMessage("LOBBY:START " + mapName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String mapName;

    public String getName() {
        return name;
    }

    private String name;

    private final Client owner;

    public Client getOwner() {
        return owner;
    }

    public Client getOtherPlayer() {
        return otherPlayer;
    }

    private Client otherPlayer;

    public Lobby(Client owner, String name, String mapName) {
        this.owner = owner;
        this.name = name;
        this.mapName = mapName;
        this.capacity = "Opened";
    }

    public void addPlayer(Client client) {
        if (this.otherPlayer == null) {
            this.otherPlayer = client;
            otherPlayer.setLobby(this);
            this.capacity = "Full";
        }
    }

    public boolean isFull() {
        if (this.otherPlayer != null) {
            return true;
        }
        return false;
    }

    public void kickOtherPlayer() {
        this.getOtherPlayer().deleteLobby();
        this.otherPlayer = null;
        this.capacity = "Opened";
    }
}
