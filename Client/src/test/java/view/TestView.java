package view;

import client.controller.multiplayer.LobbyOwnerController;
import client.controller.multiplayer.LobbySecondPlayerController;
import client.model.connection.OutputWriter;
import client.model.connection.ServerConnection;
import client.model.protocol.general.GeneralProtocol;
import client.model.protocol.lobby.LobbyProtocol;
import client.util.ResourceLoader;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class TestView extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Stage secondaryStage = new Stage();

        String name1 = "petr";
        String name2 = "pavel";

        ServerConnection serverConnection1 = new ServerConnection(name1, 8002);
        ServerConnection serverConnection2 = new ServerConnection(name2, 8002);
        serverConnection1.getIncomingMessageProcessor().getMessage();
        serverConnection2.getIncomingMessageProcessor().getMessage();


        String lobbyName = "lobby";


        GeneralProtocol protocol = new GeneralProtocol();
        OutputWriter outputWriter1 = serverConnection1.getOutgoingMessageProcessor();
        OutputWriter outputWriter2 = serverConnection2.getOutgoingMessageProcessor();

        outputWriter1.sendMessage(protocol.send().createLobby(lobbyName, ResourceLoader.getMultiplayerMaps().get(0)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        outputWriter2.sendMessage(protocol.send().joinLobby(lobbyName));

        serverConnection1.getIncomingMessageProcessor().getMessage();
        serverConnection1.getIncomingMessageProcessor().getMessage();
        serverConnection2.getIncomingMessageProcessor().getMessage();

        LobbyProtocol lobbyProtocol = new LobbyProtocol();
        String mapName = "level1";
        new LobbySecondPlayerController(secondaryStage, name1, name2, mapName, serverConnection2).loadView();
        LobbyOwnerController lobbyOwnerController = new LobbyOwnerController(primaryStage, name1, serverConnection1, mapName);
        lobbyOwnerController.loadView();
        lobbyOwnerController.setSecondPlayerName(name2);
        lobbyOwnerController.startGame(mapName);


    }

}