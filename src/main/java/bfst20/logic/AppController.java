package bfst20.logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bfst20.data.*;
import bfst20.logic.entities.*;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.entities.LinePath;
import bfst20.logic.routing.Edge;
import bfst20.logic.routing.Graph;
import bfst20.logic.routing.RoutingController;
import bfst20.presentation.View;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

import javax.xml.stream.XMLStreamException;

public class AppController {
    private RoutingController routingController;
    private LinePathGenerator linePathGenerator;
    private OSMElementData OSMElementData;
    private LinePathData linePathData;
    private RoutingData routingData;
    private AddressData addressData;
    private boolean isBinary = false;
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
        }
        generateHighways();
        routingController.initialize(getHighwaysFromModel(), getOSMNodesFromModel());
        //clearNodeData();
        view.initialize();

        return view;
    }

    public void loadFile(File file) throws IOException, XMLStreamException {
        FileHandler fileHandler = FileHandler.getInstance();
        if (file.getName().endsWith(".bin")) isBinary = true;
        fileHandler.load(file);
    }

    public void parseOSM(File file) throws FileNotFoundException, XMLStreamException {
        parser.parseOSMFile(file);
    }

    public void setPathEdgesOnModel(Map<Node, Edge> edges) {
        routingData.setEdgesOnPath(edges);
    }

    public double initializeRouting(Node source, Node target) {
        routingController = routingController.getInstance();

        return routingController.calculateShortestRoute(getGraphFromModel(), source, target);
    }

    public void setRouteOnModel(List<LinePath> route) {
        routingData.saveRoute(route);

    }

    public List<LinePath> getRouteFromModel() {
        return routingData.getRoute();
    }

    public Map<Node, Edge> getEdgesOnPathFromModel() {
        return routingData.getEdgesOnPath();
    }

    public void saveGraphOnModel(Graph graph) {
        routingData.saveGraph(graph);
    }

    public Graph getGraphFromModel() {
        return routingData.getGraph();
    }

    public Node getNodeFromModel(long id) {
        return OSMElementData.getNode(id);
    }

    public void createView(Canvas canvas, Label mouseLocationLabel) {
        view = new View(canvas);
        view.setMouseLocationView(mouseLocationLabel);
    }



    public List<LinePath> getHighwaysFromModel() {
        return linePathData.getHighWays();
    }

    public void generateHighways() {
        Map<Type, List<LinePath>> linePaths = linePathData.getLinePaths();
        List<LinePath> highWays = new ArrayList<>();
        highWays.addAll(linePaths.get(Type.HIGHWAY));
        highWays.addAll(linePaths.get(Type.TERTIARY));
        highWays.addAll(linePaths.get(Type.UNCLASSIFIED_HIGHWAY));
        highWays.addAll(linePaths.get(Type.RESIDENTIAL_HIGHWAY));
        if (linePaths.get(Type.MOTORWAY) != null) highWays.addAll(linePaths.get(Type.MOTORWAY));

        linePathData.saveHighways(highWays);
    }

    public void putAddressToModel(long id, Address address) {
        addressData.putAddress(id, address);
    }

    public Address findAddress(String query) {
        Address address = addressData.search(query);

        return address;
    }

    public void addRelationToModel(Relation relation) {
        OSMElementData.addRelation(relation);
    }

    public void setBoundsOnModel(Bounds bounds) {
        OSMElementData.setBounds(bounds);
    }

    public Bounds getBoundsFromModel() {
        return OSMElementData.getBounds();
    }

    public void addNodeToModel(long id, Node node) {
        OSMElementData.addToNodeMap(id, node);
    }

    public void addWayToModel(Way way) {
        OSMElementData.addWay(way);
    }

    public List<Way> getOSMWaysFromModel() {
        return OSMElementData.getOSMWays();
    }

    public Map<Long, Node> getOSMNodesFromModel() {
        return OSMElementData.getOSMNodes();
    }

    public List<Relation> getOSMRelationsFromModel() {
        return OSMElementData.getOSMRelations();
    }

    public void clearOSMData() {
        OSMElementData.clearData();
    }

    public void clearNodeData() {
        OSMElementData.clearNodeData();
    }

    public Map<Type, List<LinePath>> getLinePathsFromModel() {
        return linePathData.getLinePaths();
    }

    public Map<Long, Address> getAddresses() {
        AddressData addressData = AddressData.getInstance();
        return addressData.getAddresses();
    }

    public Way removeWayFromNodeTo(Type type, Node node) {
        Way way = null;
        if (type == Type.COASTLINE) way = linePathData.removeWayFromNodeToCoastline(node);
        else if (type == Type.FARMLAND) way = linePathData.removeWayFromNodeToFarmland(node);
        else if (type == Type.FOREST) way = linePathData.removeWayFromNodeToForest(node);

        return way;
    }

    public void addToModel(Type type, Node node, Way way) {
        if (type == Type.COASTLINE) linePathData.addToNodeToCoastline(node, way);
        else if (type == Type.FARMLAND) linePathData.addToNodeToFarmland(node, way);
        else if (type == Type.FOREST) linePathData.addNodeToForest(node, way);
    }

    public Map<Node, Way> getNodeTo(Type type) {
        Map<Node, Way> nodeTo = null;
        if (type == Type.COASTLINE) nodeTo = linePathData.getNodeToCoastline();
        else if (type == Type.FARMLAND) nodeTo = linePathData.getNodeToFarmland();
        else if (type == Type.FOREST) nodeTo = linePathData.getNodeToForest();

        return nodeTo;
    }

    public void addLinePathToModel(Type type, LinePath linePath) {
        linePathData.addLinePath(type, linePath);
    }

    public void addTypeToModel(Type type) {
        linePathData.addType(type);
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

    public void setLinePathsOnModel(Map<Type, List<LinePath>> drawables) {
        linePathData.setLinePaths(drawables);
    }

    public void setupRect() {
        Bounds bounds = getBoundsFromModel();

        kdTreeData.setValuesOnRect(bounds.getMinLat(), bounds.getMaxLat(), bounds.getMinLon(), bounds.getMaxLon());
    }

    public Rect getRectFromModel() {
        return kdTreeData.getRect();
    }

    public void addKDTreeToModel(Type type, List<LinePath> linePaths) {
        kdTreeData.addKDTree(type, new KDTree(linePaths, getRectFromModel()));
    }

    public KDTree getKDTreeFromModel(Type type) {
        return kdTreeData.getKDTree(type);
    }

    public void generateBinary() throws IOException {
        FileHandler fileHandler = FileHandler.getInstance();
        fileHandler.generateBinary();
    }
}