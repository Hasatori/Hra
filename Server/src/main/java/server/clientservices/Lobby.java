package server.clientservices;

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
            this.otherPlayer.getClientConnection().sendMessage("LOBBY:SETMAP " + mapName);
        }
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public void start() {
        if (this.otherPlayer != null) {
            otherPlayer.getClientConnection().sendMessage("LOBBY:START " + mapName);
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
        return this.otherPlayer != null;
    }

    public synchronized void removeOtherPlayer() {
        this.getOtherPlayer().deleteLobby();
        this.otherPlayer = null;
        this.playerCount--;
    }
}
