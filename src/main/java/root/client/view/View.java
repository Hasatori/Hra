package root.client.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import root.client.controller.Controller;

public  abstract class View extends Scene {

    private final Controller controller;

    public View(Parent parent, Controller controller){
        super(parent);
        this.controller=controller;
    }


    public abstract void reload();


}
