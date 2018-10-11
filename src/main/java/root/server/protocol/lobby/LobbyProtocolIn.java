package root.server.protocol.lobby;

public class LobbyProtocolIn {

  private final String message;

  LobbyProtocolIn(String message) {
   this.message = message;
  }


  public boolean setMap(){
      return message.matches("SET MAP \\w+");
  }
  public String getMapName(){
      return message.replace("SET MAP ","");
  }
  public boolean startGame(){
      return message.matches("START GAME");
  }
  public boolean destroyLobby(){
      return message.matches("DESTROY LOBBY");
  }
    public boolean leaveLobby(){
        return message.matches("LEAVE LOBBY");
    }
}
