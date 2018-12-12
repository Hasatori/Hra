package view;

import controller.MainController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class TestView extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainController mainController = new MainController(primaryStage);
        mainController.loadView();
        new Thread(() -> {
            Field f = null; //NoSuchFieldException
            try {
              //  Thread.sleep(1000);

                f = mainController.getClass().getDeclaredField("view");
                f.setAccessible(true);
                MainView view = (MainView) f.get(mainController);
                Field f2 = view.getClass().getDeclaredField("start");
                f2.setAccessible(true);
                Button start = (Button) f2.get(view);

                Field f3 = view.getClass().getDeclaredField("port");
                f3.setAccessible(true);
                TextField port = (TextField) f3.get(view);

                Platform.runLater(() -> {
                    port.setText("8002");
                    start.fire();
                });

            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }).start();

    }
}
