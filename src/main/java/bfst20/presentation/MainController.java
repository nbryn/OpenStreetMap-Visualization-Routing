package bfst20.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.scene.control.Menu;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.io.File;
import java.io.IOException;


public class MainController {

    Model model;

    @FXML
    private MenuItem openFile;

    @FXML
    private VBox vbox;

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

    @FXML
    public void initialize() {

        try{
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("samsoe.osm").getFile());
            Parser.parseOSMFile(file);
        }catch(Exception err){}

        MapCanvas mapCanvas = new MapCanvas(new Dimension(200, 200));
        vbox.getChildren().add(new MapCanvas(new Dimension(200, 200)));
    }
}