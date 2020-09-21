package bfst20;

import bfst20.logic.filehandling.FileHandler;
import bfst20.presentation.AlertHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class Launcher extends Application {
    public static Window primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        setup();
        FXMLLoader MainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Scene sceneMain = new Scene(MainLoader.load());

        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
        primaryStage.setResizable(true);

        primaryStage.setTitle("Map");
        primaryStage.setScene(sceneMain);
        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setup() {
        try {
            FileHandler fileHandler = new FileHandler.Builder().build();
            File file = fileHandler.getResourceAsFile("Samsø.osm");

            fileHandler.setFile(file);
        } catch (NullPointerException e) {
            AlertHandler.alertOK(Alert.AlertType.ERROR, "Error loading startup file, exiting.", true);
            System.exit(1);
        }

    }
}