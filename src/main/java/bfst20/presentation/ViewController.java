package bfst20.presentation;


import java.io.File;
import java.io.IOException;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import bfst20.data.InterestPointData;
import bfst20.logic.AppController;
import bfst20.logic.entities.InterestPoint;
import com.sun.glass.ui.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

public class ViewController {

    private AppController appController;

    private View view;

    @FXML
    private MenuItem openFile;

    @FXML
    private Label mouseLocationLabel;

    @FXML
    private TextField searchAddress;
    @FXML
    private Button searchAdressButton;

    @FXML
    private Canvas canvas;

    @FXML private TextField searchbar;
    @FXML private TextField yesbar;

    @FXML private Button bikeButton;
    @FXML private Button carButton;
    
    @FXML private Button toAddressButton;

    public ViewController() {
        appController = new AppController();

    }

    Point2D lastMouse;

    @FXML
    public void initialize() {

        appController.createView(canvas, mouseLocationLabel);


        setupFileHandling();

        ClassLoader classLoader = getClass().getClassLoader();

        File file = null;

        try{
            //file = new File(classLoader.getResource("samsoe.osm").getFile());
            file = new File("D:\\Projects\\Java\\BFST20Gruppe17\\danmark.bin");
        }catch (NullPointerException e){
            appController.alertOK(Alert.AlertType.ERROR, "Error loading startup file, exiting.");
            System.exit(1);
        }

        appController.loadFile(file);
        try {
            view = appController.initialize();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        setupCanvas();

        setupSearchButton();

        setupRouteButtons();

        toAddressButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                view.searchRoute();

            }
        });
    }

    private void setupFileHandling() {
        openFile.setOnAction(e -> {
            try{
                File file = new FileChooser().showOpenDialog(Launcher.primaryStage);
                if (file != null) {
                    appController.loadFile(file);
                    view = appController.initialize();
                }
            }catch(Exception err){
                err.printStackTrace();
            }
        });
    }

    private void setupCanvas() {
        canvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            view.zoom(factor, e.getX(), e.getY());
        });

        canvas.setOnMousePressed(e -> {
            lastMouse = new Point2D(e.getX(), e.getY());

            if(e.isControlDown()){
                Point2D converted = view.toModelCoords(e.getX(), e.getY());

                InterestPointData interestPointData = InterestPointData.getInstance();
                interestPointData.addIntrestPoint(new InterestPoint((float) converted.getY(),(float) converted.getX()));
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
    }

    private void setupSearchButton() {
        searchAdressButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String searchText = searchAddress.getText();

                view.setSearchString(searchText);

            }
        });
    }

    private void setupRouteButtons(){
        bikeButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                //System.out.println("HEY");
                //TODO: Bike not implemented yet.
            }
        });

        carButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                view.setAddress(searchbar.getText(), yesbar.getText());
                view.repaint();
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