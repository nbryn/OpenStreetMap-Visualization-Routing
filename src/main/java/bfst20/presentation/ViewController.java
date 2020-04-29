package bfst20.presentation;


import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.Map;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import bfst20.data.AddressData;
import bfst20.data.InterestPointData;
import bfst20.logic.AppController;
import bfst20.logic.entities.Address;
import bfst20.logic.entities.InterestPoint;
import bfst20.logic.misc.Vehicle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ViewController {
    private AppController appController;
    @FXML
    private FlowPane wayPointFlowPane;
    @FXML
    private Button searchAdressButton;
    @FXML
    private Button searchRouteButton;

    @FXML
    private TextField destinationBar;
    @FXML
    private Label mouseLocationLabel;
    @FXML
    private TextField searchAddress;
    @FXML
    private AnchorPane canvasParent;
    private boolean scrollTrigger;
    @FXML
    public FlowPane displayPane;
    @FXML
    private TextField searchbar;
    @FXML
    private MenuItem openFile;
    @FXML
    private Slider zoomSlider;
    @FXML
    private ToggleGroup type;
    @FXML
    private Canvas canvas;
    @FXML
    private HBox hbox;
    private View view;





    public ViewController() {
        appController = new AppController();
    }

    Point2D lastMouse;

    @FXML
    public void initialize() {

        /*        canvas.widthProperty().bind(canvasParent.widthProperty());
        canvas.heightProperty().bind(canvasParent.heightProperty());

        System.out.println(canvas.getWidth());*/

        setupHbox();

        appController.createView(canvas, mouseLocationLabel);

        setupFileHandling();

        ClassLoader classLoader = getClass().getClassLoader();

        File file = null;

        try {
            file = new File(classLoader.getResource("samsoe.osm").getFile());
            //file = new File("D:\\Projects\\Java\\BFST20Gruppe17Data\\danmark.bin");
        } catch (NullPointerException e) {
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

        setupZoomSlider();

        setupCanvas();

        setupSearchButton();

        setupDisplayPane();

        setupRouteButton();
    }

    private void setupRouteButton() {
        searchRouteButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (searchbar.getText().equals("") || destinationBar.getText().equals("")) {
                    appController.alertOK(Alert.AlertType.WARNING, "Please specify search or target address");
                    return;
                }

                //view.searchRoute("Sølyst 3", "Vestergade 37", Vehicle.CAR);
                Vehicle vehicle = Vehicle.valueOf(type.getSelectedToggle().getUserData().toString().toUpperCase());
                view.shortestPath(searchbar.getText(), destinationBar.getText(), vehicle);
            }
        });
    }

    private void setupHbox() {
        hbox.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            canvas.setWidth((double) newVal - 400);
            view.repaint();
        });

        hbox.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            canvas.setHeight((double) newVal);
            view.repaint();
        });
    }

    private void setupDisplayPane() {
        displayPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (appController.getRouteInfoFromModel() != null) {
                    for (Map.Entry<String, Double> entry : appController.getRouteInfoFromModel().entrySet()) {
                        displayPane.getChildren().add(new Button("Follow " + entry.getKey() + " for " + entry.getValue() + " km"));
                    }
                }
            }
        });
    }

    private void setupZoomSlider() {
        zoomSlider.setMax(128);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {

                if (!scrollTrigger) {
                    double deltaValue = zoomSlider.getValue() - view.getSliderValue();
                    double factor = Math.pow(1.001, 40 * deltaValue);

                    view.zoom(factor, canvas.getWidth() / 2, canvas.getHeight() / 2, 40 * deltaValue);
                }
                view.setSliderValue(zoomSlider.getValue());
            }

        });
    }

    private void updateInterestPoints() {
        wayPointFlowPane.getChildren().clear();

        InterestPointData data = InterestPointData.getInstance();
        List<InterestPoint> interestPoints = data.getAllInterestPoints();

        int i = 0;

        for (InterestPoint interest : interestPoints) {
            Text scoreText = new Text(i + ". Intrest point");

            int s = i;

            scoreText.setFont(new Font("ARIAL", 25));
            scoreText.setStyle("-fx-font-weight: bold;");
            scoreText.setFill(Color.BLACK);

            Button button = new Button();
            button.setText("Delete");

            HBox box = new HBox(scoreText, button);

            button.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    wayPointFlowPane.getChildren().remove(s);
                    interestPoints.remove(s);
                    updateInterestPoints();
                    view.repaint();
                }
            });


            wayPointFlowPane.getChildren().add(box);


            i++;
        }
    }

    private void setupFileHandling() {
        openFile.setOnAction(e -> {
            try {
                File file = new FileChooser().showOpenDialog(Launcher.primaryStage);
                if (file != null) {
                    appController.loadFile(file);
                    view = appController.initialize();
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        });
    }

    private void setupCanvas() {
        canvas.setOnScroll(e -> {
            double factor = Math.pow(1.001, e.getDeltaY());
            view.zoom(factor, e.getX(), e.getY(), e.getDeltaY());

            scrollTrigger = true;
            zoomSlider.setValue(view.getTimesZoomed());
            scrollTrigger = false;
        });

        canvas.setOnMousePressed(e -> {
            lastMouse = new Point2D(e.getX(), e.getY());

            if (e.isControlDown()) {
                Point2D converted = view.toModelCoords(e.getX(), e.getY());

                InterestPointData interestPointData = InterestPointData.getInstance();
                interestPointData.addInterestPoint(new InterestPoint((float) converted.getY(), (float) converted.getX()));
                updateInterestPoints();
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

        searchAddress.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {

                if(s2.equals(""))return;

                AddressData addressData = AddressData.getInstance();

                Queue<Address> address = addressData.getTst().keysWithPrefix(s2);

                for(int i = 0; i< 10; i++){
                    if(address.poll() != null){
                        System.out.println(address.poll().toString());
                    }
                }
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
