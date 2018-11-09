package client.model.map;

public class CreatedLobby {
	
	private final static int CAPACITY = 2;
	private final static String STATUS_DELIM = "/";
	private String name;
	private String owner;
	private String status;
	
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
