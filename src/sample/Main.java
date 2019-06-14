package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

    public static Service service = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
        primaryStage.setTitle("Cooperation DTP");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setFullScreen(true);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}

