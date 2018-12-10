package server.clientservices;

/**
 * Entity class for lobby.
 */
public class Lobby {

	private int playerCount;
	private String mapName;
	private String name;
	private final Client owner;
	private Client otherPlayer;

    /**
     * @param owner owner of the lobby
     * @param name name of the lobby
     * @param mapName map name
     */
	public Lobby(Client owner, String name, String mapName) {
        this.owner = owner;
        this.name = name;
        this.mapName = mapName;
        this.playerCount = 1;
    }

    /**
     * Getter for map name.
     * @return map name
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Setter for map name, also sends message to second player.
     * @param mapName map name
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
        if (this.otherPlayer != null) {
            this.otherPlayer.getClientConnection().sendMessage("LOBBY:SETMAP " + mapName);
        }
    }

    /**
     * Returns number of player in this lobby.
     * @return player count
     */
    public int getPlayerCount() {
        return this.playerCount;
    }

    /**
     * Starts the game by sending a message to second player.
     */
    public void start() {
        if (this.otherPlayer != null) {
            otherPlayer.getClientConnection().sendMessage("LOBBY:START " + mapName);
        }
    }

    /**
     * Returns name of the lobby.
     * @return lobby name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns owner of the lobby.
     * @return client/lobby owner
     */
    public Client getOwner() {
        return owner;
    }

    /**
     * Returns second player (not the owner).
     * @return client/second player
     */
    public Client getOtherPlayer() {
        return otherPlayer;
    }

    /**
     * Adds player to this lobby.
     * @param client
     */
    public synchronized void addPlayer(Client client) {
        if (this.otherPlayer == null) {
            this.otherPlayer = client;
            otherPlayer.setLobby(this);
            this.playerCount++;
        }
    }

    /**
     * Checks whether lobby is full and returns the result.
     * @return true=the lobby is full
     */
    public boolean isFull() {
        return this.otherPlayer != null;
    }

    /**
     * Removes second player from this lobby.
     */
    public synchronized void removeOtherPlayer() {
        this.getOtherPlayer().deleteLobby();
        this.otherPlayer = null;
        this.playerCount--;
    }
}
