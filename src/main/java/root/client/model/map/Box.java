package root.client.model.map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import root.client.util.ResourceLoader;

import java.io.IOException;

 public class Box extends MapPart implements Movable{
     private Overlaid overlaid;

     public Box(Position position, Overlaid overlaid) {
        super(position);        this.overlaid=overlaid;
    }

    @Override
    public Node getSource() {
        try {
            return FXMLLoader.load(ResourceLoader.gerResourceURL("fxml/parts/box.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


     @Override
     public void tryMoveLeft() {

     }

     @Override
     public void tryMoveRight() {

     }

     @Override
     public void tryMoveup() {

     }

     @Override
     public void tryMoveDown() {

     }

 }
