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

    public View initialize() {
        routingController = routingController.getInstance();
        if (!isBinary) {
            createLinePaths();
            //generateBinary();
        }
        generateHighways();
        routingController.buildRoutingGraph();
        //clearNodeData();
        view.initialize();

        return view;
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

    public double initializeRouting(Node source, Node target, Vehicle vehicle) {
        routingController = routingController.getInstance();

        return routingController.calculateShortestRoute(getGraphFromModel(), source, target, vehicle);
    }

    public void setRouteOnModel(List<LinePath> route) {
        routingData.saveRoute(route);

    }

    public void startStringParsing(String string) throws XMLStreamException {
        parser.parseString(string);
    }

    public Node[] getNodesFromSearchQuery(String sourceQuery, String targetQuery) {
        Graph graph = getGraphFromModel();
        List<Edge> edges = graph.getEdges();
        edges.sort(Comparator.comparing(Edge::getName));

        Address source = findAddress(sourceQuery);
        Address target = findAddress(targetQuery);

        Node srcNode = routingController.getInstance().findClosestNode(source, edges);
        Node trgNode = routingController.getInstance().findClosestNode(target, edges);

        return new Node[] {srcNode, trgNode};
    }

    public List<LinePath> getRouteFromModel() {
        return routingData.getRoute();
    }


    public void addToModel(Graph graph) {
        routingData.saveGraph(graph);
    }

    public Graph getGraphFromModel() {
        return routingData.getGraph();
    }


    public void createView(Canvas canvas, Label mouseLocationLabel) {
        view = new View(canvas);
        view.setMouseLocationView(mouseLocationLabel);
    }


    public List<LinePath> getHighwaysFromModel() {
        return linePathData.getHighWays();
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

    public void addToModel(long id, Address address) {
        addressData.putAddress(id, address);
    }

    public Address findAddress(String query) {
        Address address = addressData.search(query);

        return address;
    }

    public void addToModel(Relation relation) {
        OSMElementData.addRelation(relation);
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

    public Map<Long, Node> getNodesFromModel() {
        return OSMElementData.getOSMNodes();
    }

    public List<Relation> getRelationsFromModel() {
        return OSMElementData.getOSMRelations();
    }

    public void clearOSMData() {
        OSMElementData.clearData();
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

    public Way removeWayFromNodeTo(OSMType OSMType, Node node) {
        return linePathData.removeWayFromNodeTo(OSMType, node);
    }

    public void addToModel(OSMType OSMType, Node node, Way way) {
        linePathData.addNodeTo(OSMType, node, way);
    }

    public Map<Node, Way> getNodeTo(OSMType osmType){
        return linePathData.getNodeTo(osmType);
    }

    /*public Way removeWayFromNodeTo(OSMType OSMType, Node node) {
        Way way = null;
        if (OSMType == OSMType.COASTLINE) way = linePathData.removeWayFromNodeToCoastline(node);
        else if (OSMType == OSMType.FARMLAND) way = linePathData.removeWayFromNodeToFarmland(node);
        else if (OSMType == OSMType.FOREST) way = linePathData.removeWayFromNodeToForest(node);
        else if (OSMType == OSMType.BUILDING) way = linePathData.removewayfromNodeToBuilding(node);
        else if (OSMType == OSMType.MEADOW) way = linePathData.removeWayFromNodeToMeadow(node);
        return way;
    }

    public void addToModel(OSMType OSMType, Node node, Way way) {
        if (OSMType == OSMType.COASTLINE) linePathData.addToNodeToCoastline(node, way);
        else if (OSMType == OSMType.FARMLAND) linePathData.addToNodeToFarmland(node, way);
        else if (OSMType == OSMType.FOREST) linePathData.addNodeToForest(node, way);
        else if (OSMType == OSMType.BUILDING) linePathData.addNodeToBuilding(node, way);
        else if (OSMType == OSMType.MEADOW) linePathData.addToNodeToMeadow(node, way);
    }

    public Map<Node, Way> getNodeTo(OSMType OSMType) {
        Map<Node, Way> nodeTo = null;
        if (OSMType == OSMType.COASTLINE) nodeTo = linePathData.getNodeToCoastline();
        else if (OSMType == OSMType.FARMLAND) nodeTo = linePathData.getNodeToFarmland();
        else if (OSMType == OSMType.FOREST) nodeTo = linePathData.getNodeToForest();
        else if (OSMType == OSMType.BUILDING) nodeTo = linePathData.getNodeToBuilding();
        else if (OSMType == OSMType.MEADOW) nodeTo = linePathData.getNodeToMeadow();
        return nodeTo;
    }*/

    public void addToModel(OSMType OSMType, LinePath linePath) {
        linePathData.addLinePath(OSMType, linePath);
    }

    public void addToModel(OSMType OSMType) {
        linePathData.addType(OSMType);
    }

    public void createLinePaths() {
        linePathGenerator = LinePathGenerator.getInstance();
        linePathGenerator.createLinePaths();
    }

    public void clearLinePathData() {
        linePathGenerator = LinePathGenerator.getInstance();
        linePathGenerator.clearData();
        linePathData.clearData();
    }

    public void addToModel(Map<OSMType, List<LinePath>> linePaths) {
        linePathData.setLinePaths(linePaths);
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

    public void alertOK(Alert.AlertType type, String text){
        view.displayError(type, text);
    }

    //TODO: NOT BEING USED?
    public void generateBinary() throws IOException {
        FileHandler fileHandler = FileHandler.getInstance();
        fileHandler.generateBinary();
    }
}