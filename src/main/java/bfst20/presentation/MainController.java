package bfst20.presentation;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;


public class MainController {

    FileLoader fileLoader;

    @FXML
    private MenuItem openFile;

    @FXML
    private VBox vbox;

    @FXML
    private VBox addressVBox;

    @FXML
    private VBox routeVBox;

    @FXML
    private Button toAddressButton;

    @FXML
    private Button toRouteButton;


    public MainController() {
        this.fileLoader = new FileLoader();
    }

    Point2D lastMouse;


    @FXML
    public void initialize() {
        
        Canvas canvas = new Canvas(1270, 720);
        
        View view = new View(canvas);

        vbox.getChildren().add(canvas);

        toRouteButton.setOnAction(e -> {
            addressVBox.setVisible(false);
            routeVBox.setVisible(true);
        });

        toAddressButton.setOnAction(e -> {
            addressVBox.setVisible(true);
            routeVBox.setVisible(false);
        });

        try {
            ClassLoader classLoader = getClass().getClassLoader();
            //File file = new File(classLoader.getResource("samsoe.osm").getFile());
            File file = new File("F:\\bornholm.osm");
            //File file = new File("F:\\denmark.osm");

            Parser parser = Parser.getInstance();
            parser.parseOSMFile(file);
            view.initializeData();
        } catch (Exception err) {
        }

        canvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            view.zoom(factor, e.getX(), e.getY());
        });


        canvas.setOnMousePressed(e -> {
            lastMouse = new Point2D(e.getX(), e.getY());
        });

        canvas.setOnMouseDragged(e -> {
            view.pan(e.getX() - lastMouse.getX(), e.getY() - lastMouse.getY());
            lastMouse = new Point2D(e.getX(), e.getY());
        });

        canvas.setOnMouseMoved(e -> {
            view.repaint();
        });
    }

    public static void main(String[] args) {
        Launcher.main(args);

    }

    public void load(ActionEvent actionEvent) throws IOException, XMLStreamException, FactoryConfigurationError {
    }
}