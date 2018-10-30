package root.server.clientservices;

/**
 * entity for tableview
 * @author david
 *
 */
public class CreatedLobby {
	
	private final static int CAPACITY = 2;
	private String name;
	private String owner;
	private String status;
	
	public CreatedLobby(String name, String owner, String status) {
		this.name = name;
		this.owner = owner;
		this.status = status + "/" + CAPACITY;
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
		this.status = status + "/" + CAPACITY;
	}
}
