package bfst20.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import bfst20.data.*;
import bfst20.logic.entities.*;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.entities.LinePath;
import bfst20.logic.misc.OSMType;
import bfst20.logic.misc.Vehicle;
import bfst20.logic.routing.Edge;
import bfst20.logic.routing.Graph;
import bfst20.logic.routing.RoutingController;
import bfst20.presentation.View;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

import javax.xml.stream.XMLStreamException;

public class AppController {
    private RoutingController routingController;
    private LinePathGenerator linePathGenerator;
    private OSMElementData OSMElementData;
    private LinePathData linePathData;
    private boolean isBinary = false;
    private RoutingData routingData;
    private AddressData addressData;
    private KDTreeData kdTreeData;
    private Parser parser;
    private View view;

    public AppController() {
        OSMElementData = OSMElementData.getInstance();
        linePathData = LinePathData.getInstance();
        routingData = RoutingData.getInstance();
        addressData = AddressData.getInstance();
        kdTreeData = KDTreeData.getInstance();
        parser = Parser.getInstance();

    }

    public View initialize() throws IOException {
        routingController = routingController.getInstance();
        if (!isBinary) {
            createLinePaths();
            //generateBinary();
            clearNodeData();
        }
        System.out.println("Generate Highways");
        generateHighways();
        System.out.println("Done, building graph");
        routingController.buildRoutingGraph();
        System.out.println("Done");
        view.initialize();
        OSMElementData = null;
        linePathData = null;
        linePathGenerator = null;
        System.gc();

        return view;
    }

    public void createLinePaths() {
        linePathGenerator = LinePathGenerator.getInstance();
        linePathGenerator.createLinePaths();
    }

    public void generateHighways() {
        Map<OSMType, List<LinePath>> linePaths = linePathData.getLinePaths();
        List<LinePath> highWays = new ArrayList<>();
        highWays.addAll(linePaths.get(OSMType.HIGHWAY));
        highWays.addAll(linePaths.get(OSMType.TERTIARY));
        highWays.addAll(linePaths.get(OSMType.UNCLASSIFIED_HIGHWAY));
        highWays.addAll(linePaths.get(OSMType.RESIDENTIAL_HIGHWAY));
        highWays.addAll(linePaths.get(OSMType.PATH));
        highWays.addAll(linePaths.get(OSMType.FOOTWAY));
        highWays.addAll(linePaths.get(OSMType.TRACK));

        if (linePaths.get(OSMType.MOTORWAY) != null) highWays.addAll(linePaths.get(OSMType.MOTORWAY));

        linePathData.saveHighways(highWays);
    }

    public List<LinePath> getHighwaysFromModel() {
        return linePathData.getHighWays();
    }


    public void addHighwaysToModel(List<LinePath> highways) {
        linePathData.saveHighways(highways);
    }

    public double initializeRouting(String sourceQuery, String targetQuery, Vehicle vehicle) {
        routingController = routingController.getInstance();

        Address source = findAddress(sourceQuery);
        Address target = findAddress(targetQuery);

        Graph graph = getGraphFromModel();
        List<Edge> edges = graph.getEdges();
        edges.sort(Comparator.comparing(Edge::getName));

        return routingController.calculateShortestRoute(graph, edges, source, target, vehicle);
    }

    public Graph getGraphFromModel() {
        return routingData.getGraph();
    }

    public void addToModel(Graph graph) {
        routingData.saveGraph(graph);
    }

    public List<Edge> getRouteFromModel() {
        return routingData.getRoute();
    }

    public void addToModel(List<Edge> route) {
        routingData.saveRoute(route);
    }

    public void addRouteInfoToModel(Map<String, Double> routeInfo) {
        routingData.saveRouteInfo(routeInfo);
    }

    public Map<String, Double> getRouteInfoFromModel() {
        return routingData.getRouteInfo();
    }

    public void loadFile(File file) {
        try {
            FileHandler fileHandler = FileHandler.getInstance();
            if (file.getName().endsWith(".bin")) isBinary = true;
            fileHandler.load(file);
        } catch (IOException ioException) {
            alertOK(Alert.AlertType.ERROR, "Invalid xml data, exiting.");
            System.exit(1);
        } catch (XMLStreamException xmlStreamException) {
            alertOK(Alert.AlertType.ERROR, "Invalid xml data, exiting.");
            System.exit(1);
        }
    }

    public void parseOSM(File file) throws IOException, XMLStreamException {
        parser.parseOSMFile(file);
    }

    public void parseString(String string) throws XMLStreamException {
        parser.parseString(string);
    }



    public void addAddressesToModel(Map<Long, Address> addresses) {
        addressData.saveAddresses(addresses);
    }

    public void addToModel(long id, Address address) {
        addressData.addAddress(id, address);
    }

    public Address findAddress(String query) {
        Address address = addressData.search(query);

        return address;
    }

    public void addToModel(Relation relation) {
        OSMElementData.addRelation(relation);
    }

    public List<Relation> getRelationsFromModel() {
        return OSMElementData.getRelations();
    }

    public void addToModel(Bounds bounds) {
        OSMElementData.setBounds(bounds);
    }

    public Bounds getBoundsFromModel() {
        return OSMElementData.getBounds();
    }

    public void addToModel(long id, Node node) {
        OSMElementData.addToNodeMap(id, node);
    }

    public void addToModel(Way way) {
        OSMElementData.addWay(way);
    }

    public List<Way> getWaysFromModel() {
        return OSMElementData.getOSMWays();
    }

    public Node getNodeFromModel(long id) {
        return OSMElementData.getNode(id);
    }

    public Map<Long, Node> getNodesFromModel() {
        return OSMElementData.getNodes();
    }


    public void clearNodeData() {
        OSMElementData.clearNodeData();
    }

    public Map<OSMType, List<LinePath>> getLinePathsFromModel() {
        return linePathData.getLinePaths();
    }

    public Map<Long, Address> getAddressesFromModel() {
        AddressData addressData = AddressData.getInstance();
        return addressData.getAddresses();
    }

    public Way removeWayFromNodeTo(OSMType type, Node node) {
        return linePathData.removeWayFromNodeTo(type, node);
    }

    public void addToModel(OSMType type, Node node, Way way) {
        linePathData.addNodeTo(type, node, way);
    }

    public Map<Node, Way> getNodeTo(OSMType type) {
        return linePathData.getNodeTo(type);
    }

    public void addToModel(OSMType type, LinePath linePath) {
        linePathData.addLinePath(type, linePath);
    }

    public void addToModel(OSMType type) {
        linePathData.addType(type);
    }

    public void addToModel(Map<OSMType, List<LinePath>> linePaths) {
        linePathData.saveLinePaths(linePaths);
    }

    public void clearLinePathData() {
        LinePathGenerator.getInstance().clearData();
        linePathData.clearData();
    }

    public void setupRect() {
        Bounds bounds = getBoundsFromModel();

        kdTreeData.setValuesOnRect(bounds.getMinLat(), bounds.getMaxLat(), bounds.getMinLon(), bounds.getMaxLon());
    }

    public Rect getRectFromModel() {
        return kdTreeData.getRect();
    }

    public void addKDTreeToModel(OSMType OSMType, List<LinePath> linePaths) {
        kdTreeData.addKDTree(OSMType, new KDTree(linePaths, getRectFromModel()));
    }

    public KDTree getKDTreeFromModel(OSMType OSMType) {
        return kdTreeData.getKDTree(OSMType);
    }

    public void createView(Canvas canvas, Label mouseLocationLabel) {
        view = new View(canvas);
        view.setMouseLocationView(mouseLocationLabel);
    }

    public void alertOK(Alert.AlertType type, String text) {
        view.displayError(type, text);
    }

    //TODO: NOT BEING USED?
    public void generateBinary() throws IOException {
        try {
            FileHandler fileHandler = FileHandler.getInstance();
            fileHandler.generateBinary();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}