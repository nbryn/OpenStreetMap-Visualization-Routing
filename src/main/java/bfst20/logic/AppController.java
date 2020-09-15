package bfst20.logic;

import java.io.*;
import java.util.List;
import java.util.Map;

import bfst20.data.*;
import bfst20.logic.controllers.*;
import bfst20.logic.controllers.interfaces.AddressAPI;
import bfst20.logic.controllers.interfaces.KDTreeAPI;
import bfst20.logic.controllers.interfaces.LinePathAPI;
import bfst20.logic.controllers.interfaces.OSMElementAPI;
import bfst20.logic.entities.*;
import bfst20.logic.entities.LinePath;
import bfst20.logic.misc.Vehicle;
import bfst20.logic.routing.Edge;
import bfst20.logic.routing.Graph;
import bfst20.logic.routing.RoutingController;
import bfst20.logic.services.LinePathService;
import bfst20.presentation.AlertHandler;
import bfst20.presentation.View;
import javafx.scene.control.Alert;

import javax.xml.stream.XMLStreamException;

public class AppController {

    private RoutingController routingController;
    private LinePathService linePathService;
    private OSMElementData osmElementData;
    private LinePathData linePathData;
    private boolean isBinary = false;
    private FileHandler fileHandler;
    private RoutingData routingData;
    private AddressData addressData;
    private KDTreeData kdTreeData;
    private Parser parser;
    private OSMElementAPI osmElementController;
    private AddressAPI addressController;
    private KDTreeAPI kdTreeController;
    private LinePathAPI linePathController;
    private View view;

    public AppController() {
        osmElementData = OSMElementData.getInstance();
        linePathData = LinePathData.getInstance();
        routingData = RoutingData.getInstance();
        addressData = AddressData.getInstance();
        kdTreeData = KDTreeData.getInstance();

        addressController = new AddressController(addressData);
        osmElementController = new OSMElementController(osmElementData);
        kdTreeController = new KDTreeController(kdTreeData);

        linePathService = LinePathService.getInstance(linePathData);
        linePathController = new LinePathController(linePathData, linePathService);

        parser = new Parser(osmElementController, addressController);

        fileHandler = new FileHandler.Builder(parser)
                .withKDTreeAPI(kdTreeController)
                .withAddressAPI(addressController)
                .withLinePathAPI(linePathController)
                .withOSMElementAPI(osmElementController)
                .build();

    }

    public void initialize(View view, File file) {
        this.view = view;
        loadFile(file);
        routingController = new RoutingController(this);
        if (!isBinary) {
            linePathController.init(osmElementController.fetchAllWays(), osmElementController.fetchAllNodes(),
                    osmElementController.fetchAllRelations());

            clearNodeData();

            routingController.buildRoutingGraph();
            kdTreeController.generateKDTrees(linePathController.fetchLinePathData(), osmElementController.fetchBoundsData());

        }

        view.initialize(isBinary);
        System.gc();
    }

    public void loadFile(File file) {
        clearExistingData();
        try {
            isBinary = file.getName().endsWith(".bin") ? true : false;

            fileHandler.load(file, this);
        } catch (IOException ioException) {
            alertOK(Alert.AlertType.ERROR, "Invalid xml data, exiting.", true);
            System.exit(1);
        } catch (XMLStreamException xmlStreamException) {
            alertOK(Alert.AlertType.ERROR, "Invalid xml data, exiting.", true);
            System.exit(1);
        } catch (NullPointerException exception) {
            alertOK(Alert.AlertType.ERROR, "Error finding file, exiting.", true);
            System.exit(1);
        }
    }

    private void clearExistingData() {
        linePathData.clearMotorways();
        linePathData.clearCoastlines();
        kdTreeData.clearData();

        clearLinePathData();
        addressData.clearData();

        InterestPointData interestPointData = InterestPointData.getInstance();
        interestPointData.clearData();

        clearNodeData();
        clearLinePathData();

    }

    public List<LinePath> fetchHighways() {
        return linePathData.getHighways();
    }

    public double initializeRouting(String sourceQuery, String targetQuery, Vehicle vehicle) {
        routingController = new RoutingController(new AppController());

        Address source = addressData.findAddress(sourceQuery);
        Address target = addressData.findAddress(targetQuery);

        Graph graph = fetchGraphData();
        List<Edge> edges = graph.getEdges();

        return routingController.calculateShortestRoute(graph, edges, source, target, vehicle);
    }

    public Graph fetchGraphData() {
        return routingData.getGraph();
    }

    public void saveGraphData(Graph graph) {
        routingData.saveGraph(graph);
    }

    public List<Edge> fetchRouteData() {
        return routingData.getRoute();
    }

    public void saveRouteData(List<Edge> route) {
        routingData.saveRoute(route);
    }

    public void saveRouteDirections(Map<String, Double> routeInfo) {
        routingData.saveRouteDirections(routeInfo);
    }

    public Map<String, Double> fetchRouteDirections() {
        return routingData.getRouteDirections();
    }

    public void clearRouteInfoData() {
        routingData.clearData();
    }

    public void setSearchString(Address address) {
        view.setSearchAddress(address);
    }


    public void parseString(String string) throws XMLStreamException {
        parser.parseString(string);
    }


    public void clearNodeData() {
        OSMElementData.getInstance().clearNodeData();
    }

    public void clearLinePathData() {
        LinePathService.getInstance(linePathData).clearData();
        linePathData.clearData();
    }

    public void alertOK(Alert.AlertType type, String text, boolean wait) {
        AlertHandler.alertOK(type, text, wait);
    }

    public void generateBinary() throws IOException {
        clearAllNonBinData();
        try {
            fileHandler.generateBinary();
        } catch (Exception e) {
            alertOK(Alert.AlertType.ERROR, "Error generating binary, please retry.", false);
        }
    }

    public void clearAllNonBinData() {
        clearNodeData();
        clearLinePathData();
        routingData.clearData();
    }


}