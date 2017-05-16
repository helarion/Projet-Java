package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox root =null;
        try {
            root= (VBox) FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
        } catch (IOException e) {
            System.out.println("test");
            e.printStackTrace();
        }

        primaryStage.setTitle("Projet Java");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
