package root.server.clientservices;

import java.io.IOException;

public class Lobby {
	private int playerCount;
	private String mapName;
	private String name;
	private final Client owner;
	private Client otherPlayer;
	
	public Lobby(Client owner, String name, String mapName) {
        this.owner = owner;
        this.name = name;
        this.mapName = mapName;
        this.playerCount = 1;
    }

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

    public int getPlayerCount() {
        return this.playerCount;
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

    public String getName() {
        return name;
    }

    public Client getOwner() {
        return owner;
    }

    public Client getOtherPlayer() {
        return otherPlayer;
    }

    public synchronized void addPlayer(Client client) {
        if (this.otherPlayer == null) {
            this.otherPlayer = client;
            otherPlayer.setLobby(this);
            this.playerCount++;
        }
    }

    public boolean isFull() {
        if (this.otherPlayer != null) {
            return true;
        }
        return false;
    }

    public synchronized void kickOtherPlayer() {
        this.getOtherPlayer().deleteLobby();
        this.otherPlayer = null;
        this.playerCount--;
    }
}
