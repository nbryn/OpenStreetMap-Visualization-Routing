package bfst20.presentation;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import bfst20.data.IntrestPointData;
import bfst20.logic.AppController;
import bfst20.logic.entities.IntrestPoint;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ViewController {

    private AppController appController;

    private View view;

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

    @FXML
    private Label mouseLocationLabel;

    @FXML
    private TextField searchAddress;
    @FXML
    private Button searchAdressButton;

    @FXML
    private Canvas canvas;

    public ViewController() {
        appController = new AppController();

    }

    Point2D lastMouse;

    @FXML
    public void initialize() {

        appController.createView(canvas, mouseLocationLabel);

        toRouteButton.setOnAction(e -> {
            addressVBox.setVisible(false);
            routeVBox.setVisible(true);
        });

        toAddressButton.setOnAction(e -> {
            addressVBox.setVisible(true);
            routeVBox.setVisible(false);
        });

        openFile.setOnAction(e -> {
            File file = new FileChooser().showOpenDialog(Launcher.primaryStage);
            if (file != null) {
                try {
                    // Might change our project...
                    appController.loadFile(file);
                    view = appController.initialize();
                } catch (IOException | XMLStreamException | FactoryConfigurationError e1) {
                    System.out.println("Error loading file");
                }
            }
        });

        try {
            ClassLoader classLoader = getClass().getClassLoader();

            File file = new File(classLoader.getResource("samsoe.osm").getFile());

            // File file = new File(classLoader.getResource("samsoe.bin").getFile());

             //File file = new File("F:\\lolland.osm");

            appController.loadFile(file);
            view = appController.initialize();

        } catch (Exception err) {
            // err.printStackTrace();
        }
        // colorBlindButton.setOnAction(e ->{
        // view.changeToColorBlindMode(true);
        // });

        // normalColorButton.setOnAction(e ->{
        // view.changeToColorBlindMode(false);
        // });

        canvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            view.zoom(factor, e.getX(), e.getY());
        });

        canvas.setOnMousePressed(e -> {
            lastMouse = new Point2D(e.getX(), e.getY());

            if(e.isControlDown()){
                Point2D converted = view.toModelCoords(e.getX(), e.getY());

                IntrestPointData intrestPointData = IntrestPointData.getInstance();
                intrestPointData.addIntrestPoint(new IntrestPoint((float) converted.getY(),(float) converted.getX()));
            }

            view.repaint();
        });

        canvas.setOnMouseDragged(e -> {
            view.pan(e.getX() - lastMouse.getX(), e.getY() - lastMouse.getY());
            lastMouse = new Point2D(e.getX(), e.getY());
        });

        canvas.setOnMouseMoved(e -> {
            view.setMousePos(new Point2D(e.getX(), e.getY()));
            view.repaint();
        });

        searchAdressButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String searchText = searchAddress.getText();

                view.setSearchString(searchText);

            }
        });
    }



    public static void main(String[] args) {
        Launcher.main(args);
    }

    public void load(ActionEvent actionEvent) throws IOException, XMLStreamException, FactoryConfigurationError {
    }

    public void normalColorButton(ActionEvent actionEvent) {
        view.changeToColorBlindMode(false);
    }

    public void colorBlindButton(ActionEvent actionEvent) {
        view.changeToColorBlindMode(true);
    }
}