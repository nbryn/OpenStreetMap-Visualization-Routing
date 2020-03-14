package bfst20.presentation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class MainController {

    Model model;

    @FXML
    private MenuItem openFile;

    @FXML
    private VBox vbox;

    private MapCanvas mapCanvas;

    public MainController() {
        this.model = new Model();
    }

    @FXML
    public void initialize() {
        
        mapCanvas = new MapCanvas(new Dimension(800, 400));

        vbox.getChildren().add(mapCanvas);

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("samsoe.osm").getFile());
            mapCanvas.initializeData(Parser.parseOSMFile(file));
        } catch (Exception err) {
        }

        mapCanvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            mapCanvas.zoom(factor, e.getX(), e.getY());
        });
    }

    public static void main(String[] args) {
        Launcher.main(args);

    }

    public void load(ActionEvent actionEvent) throws IOException, XMLStreamException, FactoryConfigurationError, URISyntaxException {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        File file = chooser.showOpenDialog(openFile.getParentPopup().getScene().getWindow());
        model.load(file);
    }

    public void updateMapCanvas() {
        mapCanvas.update();
    }
}