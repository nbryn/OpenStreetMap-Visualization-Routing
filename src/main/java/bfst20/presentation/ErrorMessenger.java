package bfst20.presentation;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ErrorMessenger {

    public static void alertOK(Alert.AlertType type, String text){
        Alert alert = new Alert(type, text, ButtonType.OK);
        alert.showAndWait();
    }

    public static ButtonType alertYesNo(Alert.AlertType type, String text){
        Alert alert = new Alert(type, text, ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        return alert.getResult();
    }
}
