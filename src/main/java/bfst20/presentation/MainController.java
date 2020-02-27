package bfst20.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.scene.control.Menu;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;


public class MainController {

    Model model;

    @FXML
    private MenuItem openFile;

    public MainController() {
        this.model = new Model();
    }


    public static void main(String[] args) {
        Launcher.main(args);
    }


    public void load(ActionEvent actionEvent) throws IOException, XMLStreamException, FactoryConfigurationError {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(openFile.getParentPopup().getScene().getWindow());

        model.load(file);

    }
}