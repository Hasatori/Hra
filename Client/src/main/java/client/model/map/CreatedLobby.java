package client.model.map;

/**
 * Class for already created and public lobby.
 */
public class CreatedLobby {
	
	private final static int CAPACITY = 2;
	private final static String STATUS_DELIM = "/";
	private String name;
	private String owner;
	private String status;

	/**
	 * @param name name of the lobby
	 * @param owner name of the owner of the lobby
	 * @param status status of the lobby (1/2, 2/2)
	 */
	public CreatedLobby(String name, String owner, String status) {
		this.name = name;
		this.owner = owner;
		this.status = status + STATUS_DELIM + CAPACITY;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status + STATUS_DELIM + CAPACITY;
	}
}
