package bfst20.presentation;

import bfst20.logic.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader MainLoader = new FXMLLoader(getClass().getResource("/Main.fxml"));
        Scene sceneMain = new Scene(MainLoader.load());


        primaryStage.setResizable(false);
        primaryStage.setTitle("Map");
        primaryStage.setScene(sceneMain);
        primaryStage.show();
    }

	public static void main(String[] args) {
        launch(args);
	}
}