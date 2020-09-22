package bfst20.presentation;


import java.io.File;
import java.util.*;

import javax.xml.parsers.FactoryConfigurationError;

import bfst20.Launcher;
import bfst20.data.*;
import bfst20.logic.filehandling.FileHandler;
import bfst20.logic.controllers.*;
import bfst20.logic.controllers.interfaces.AddressAPI;
import bfst20.logic.controllers.interfaces.RoutingAPI;
import bfst20.logic.entities.Address;
import bfst20.logic.entities.InterestPoint;
import bfst20.logic.misc.Vehicle;
import bfst20.logic.services.AddressService;
import bfst20.logic.services.LinePathService;
import bfst20.logic.services.RoutingService;
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
    private StartupController startupController;
    private AddressService addressService;
    private RoutingService routingService;
    private AddressData addressData;
    private FileHandler fileHandler;
    private AddressAPI addressAPI;
    private RoutingAPI routingAPI;

    @FXML
    private FlowPane wayPointFlowPane;
    @FXML
    private Button searchAddressButton;
    @FXML
    private Button searchRouteButton;
    @FXML
    private TextField destinationBar;
    @FXML
    private Label mouseLocationLabel;
    @FXML
    private TextField searchAddress;
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

    private boolean scrollTrigger;
    private Point2D lastMouse;
    private View view;


    public ViewController() {
        fileHandler = new FileHandler.Builder().build();
        addressData = AddressData.getInstance();
        routingService = new RoutingService(RoutingData.getInstance());
        startupController = new StartupController();
        addressService = new AddressService(addressData);

        addressAPI = new AddressController(addressService);
        routingAPI = new RoutingController(routingService, addressService);
    }

    @FXML
    public void initialize() {
        view = buildView();

        loadDefault();

        setupSuggestionHandlers();
        setupHbox();
        setupFileHandling();
        setupZoomSlider();

        setupCanvas();
        setupSearchButton();
        setupRouteButton();
    }

    private View buildView() {
        return new View.Builder(canvas)
                .withLinePathAPI(new LinePathController(LinePathService.getInstance(LinePathData.getInstance())))
                .withOSMElementAPI(new OSMElementController())
                .withKDTreeAPI(new KDTreeController())
                .withRoutingAPI(new RoutingController(routingService, addressService))
                .withMouseLocationLabel(mouseLocationLabel)
                .build();
    }

    private void setupSuggestionHandlers() {
        new SuggestionHandler(addressAPI, view, searchAddress, SuggestionHandler.SuggestionEvent.SEARCH);
        new SuggestionHandler(addressAPI, view, searchbar, SuggestionHandler.SuggestionEvent.ADDRESS);
        new SuggestionHandler(addressAPI, view, destinationBar, SuggestionHandler.SuggestionEvent.DESTINATION);
    }

    private void loadDefault() {
        AlertHandler.alertOK(Alert.AlertType.INFORMATION, "Starting program, press OK to continue!", true);

        try {
            startupController.initialize(view, fileHandler.getFile());
        } catch (Exception e) {
            e.printStackTrace();
            AlertHandler.alertOK(Alert.AlertType.ERROR, "Error initializing application, exiting.", true);
            System.exit(1);
        }
    }

    private void setupHbox() {
        hbox.widthProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setWidth((double) newVal - 400);
            view.repaint();
        });

        hbox.heightProperty().addListener((obs, oldVal, newVal) -> {
            canvas.setHeight((double) newVal);
            view.repaint();
        });
    }

    private void setupZoomSlider() {
        zoomSlider.setMax(126);
        zoomSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (!scrollTrigger) {
                    double deltaValue = zoomSlider.getValue() - view.getSliderValue();
                    double factor = Math.pow(1.001, 20 * deltaValue);

                    view.zoom(factor, canvas.getWidth() / 2, canvas.getHeight() / 2, 20 * deltaValue);
                }
                view.setSliderValue(zoomSlider.getValue());
            }

        });
    }

    private void setupRouteButton() {
        searchRouteButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                if (searchbar.getText().equals("") || destinationBar.getText().equals("")) {
                    AlertHandler.alertOK(Alert.AlertType.WARNING, "Please specify search or target address", true);
                    return;
                }

                try {
                    Vehicle vehicle = Vehicle.valueOf(type.getSelectedToggle().getUserData().toString().toUpperCase());
                    view.shortestPath(searchbar.getText(), destinationBar.getText(), vehicle);

                    if (routingAPI.fetchRouteDirections() != null) {
                        displayPane.getChildren().clear();

                        Map<String, Double> routeDirections = routingAPI.fetchRouteDirections();
                        List<String> streetsOnRoute = new ArrayList<>(routeDirections.keySet());
                        Collections.reverse(streetsOnRoute);
                        if (routingAPI.fetchRouteDirections().size() > 0) {
                            for (String street : streetsOnRoute) {
                                String text = street.equals("ååååå") ? "Unknown Street" : street;
                                Button route = new Button("Follow " + text + " for " + routeDirections.get(street) + " km");
                                route.setPrefWidth(375);

                                route.setPrefHeight(60);
                                route.setMouseTransparent(true);
                                route.setFocusTraversable(false);

                                displayPane.getChildren().add(new Separator());
                                displayPane.getChildren().add(route);
                            }
                            routingAPI.clearRouteInfoData();
                        } else {
                            displayPane.getChildren().clear();
                            AlertHandler.alertOK(Alert.AlertType.INFORMATION, "No route(s) found!", true);
                        }

                    } else {
                        displayPane.getChildren().clear();
                        AlertHandler.alertOK(Alert.AlertType.INFORMATION, "No route(s) found!", true);
                    }
                } catch (NullPointerException e) {
                    AlertHandler.alertOK(Alert.AlertType.INFORMATION, "No route(s) found!", true);
                }
            }
        });
    }

    private void updateInterestPoints() {
        wayPointFlowPane.getChildren().clear();

        InterestPointData data = InterestPointData.getInstance();
        List<InterestPoint> interestPoints = data.getAllInterestPoints();

        for (int i = 0; i < interestPoints.size(); i++) {
            Text scoreText = new Text(i + ". Interest point");

            int s = i;

            scoreText.setFont(new Font("ARIAL", 25));
            scoreText.setStyle("-fx-font-weight: bold;");
            scoreText.setFill(Color.BLACK);

            Button button = new Button();
            button.setText("Delete");

            HBox box = new HBox(scoreText, button);
            box.setMinWidth(400);
            box.setPrefWidth(400);

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
        }
    }

    private void setupFileHandling() {
        openFile.setOnAction(e -> {
            try {
                File file = new FileChooser().showOpenDialog(Launcher.primaryStage);
                if (file != null) {
                    wayPointFlowPane.getChildren().clear();

                    view = buildView();

                    startupController.initialize(view, file);
                }
            } catch (Exception err) {
                AlertHandler.alertOK(Alert.AlertType.ERROR, "Error loading selected file, please retry with a new one.", false);
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
                Point2D converted = view.convertCoordinates(e.getX(), e.getY());
                InterestPointData interestPointData = InterestPointData.getInstance();

                interestPointData.saveInterestPoint(new InterestPoint((float) converted.getY(), (float) converted.getX()));
                updateInterestPoints();
            }

            view.repaint();
        });

        canvas.setOnMouseDragged(e -> {
            view.pan(e.getX() - lastMouse.getX(), e.getY() - lastMouse.getY());
            lastMouse = new Point2D(e.getX(), e.getY());
        });

        canvas.setOnMouseMoved(e -> {
            view.setMousePosition(new Point2D(e.getX(), e.getY()));
            view.repaint();
        });
    }

    private void setupSearchButton() {
        searchAddressButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                String searchText = searchAddress.getText();
                Address address = addressAPI.findAddress(searchText);

                if (address != null) {
                    view.setSearchAddress(address);
                } else {
                    AlertHandler.alertOK(Alert.AlertType.INFORMATION, "Typed address not found!", true);
                }
            }
        });

    }

    public void save(ActionEvent actionEvent) throws FactoryConfigurationError {
        startupController.generateBinary();
    }

    public void normalColorButton(ActionEvent actionEvent) {
        view.changeToColorBlindMode(false);
    }

    public void colorBlindButton(ActionEvent actionEvent) {
        view.changeToColorBlindMode(true);
    }

    public void load(ActionEvent actionEvent) throws FactoryConfigurationError {

    }
}
