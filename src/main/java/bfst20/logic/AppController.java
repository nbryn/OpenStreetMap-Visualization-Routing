package bfst20.logic;

import java.io.*;
import java.util.List;
import java.util.Map;

import bfst20.data.AddressModel;
import bfst20.data.KDTreeModel;
import bfst20.data.LinePathModel;
import bfst20.data.OSMElementModel;
import bfst20.logic.entities.*;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.entities.LinePath;
import bfst20.presentation.View;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;

import javax.xml.stream.XMLStreamException;

public class AppController {

    private OSMElementModel OSMElementModel;
    private LinePathModel linePathModel;
    private KDTreeModel kdTreeModel;
    private Parser parser;
    private LinePathGenerator linePathGenerator;
    private View view;
    private boolean isBinary = false;


    public AppController() {
        OSMElementModel = OSMElementModel.getInstance();
        linePathModel = LinePathModel.getInstance();
        kdTreeModel = KDTreeModel.getInstance();
        parser = Parser.getInstance();

    }

    public void loadFile(File file) throws IOException, XMLStreamException {
        FileHandler fileHandler = FileHandler.getInstance();
        if (file.getName().endsWith(".bin")) isBinary = true;
        fileHandler.load(file);
    }

    public void parseOSM(File file) throws FileNotFoundException, XMLStreamException {
        parser.parseOSMFile(file);
    }

    public boolean isBinary() {
        return isBinary;
    }

    public void createView(Canvas canvas, Label mouseLocationLabel) {
        view = new View(canvas);
        view.setMouseLocationView(mouseLocationLabel);
    }

    public View initialize() throws IOException {
        view.initializeData();

        return view;
    }

    public void putAddressToModel(long id, Address address) {
        AddressModel addressModel = AddressModel.getInstance();
        addressModel.putAddress(id, address);
    }

    public void addRelationToModel(Relation relation) {
        OSMElementModel.addRelation(relation);
    }

    public void setBoundsOnModel(Bounds bounds) {
        OSMElementModel.setBounds(bounds);
    }

    public Bounds getBoundsFromModel() {
        return OSMElementModel.getBounds();
    }

    public void addNodeToModel(long id, Node node) {
        OSMElementModel.addToNodeMap(id, node);
    }

    public void addWayToModel(Way way) {
        OSMElementModel.addWay(way);
    }

    public List<Way> getOSMWaysFromModel() {
        return OSMElementModel.getOSMWays();
    }

    public Map<Long, Node> getOSMNodesFromModel() {
        return OSMElementModel.getOSMNodes();
    }

    public List<Relation> getOSMRelationsFromModel() {
        return OSMElementModel.getOSMRelations();
    }

    public void clearOSMData() {
        OSMElementModel.clearData();
    }

    public Map<Type, List<LinePath>> getLinePathsFromModel() {
        return linePathModel.getLinePaths();
    }

    public Map<Long, Address> getAddresses() {
        AddressModel addressModel = AddressModel.getInstance();
        return addressModel.getAddresses();
    }

    public Way removeWayFromNodeTo(Type type, Node node) {
        Way way = null;
        if (type == Type.COASTLINE) way = linePathModel.removeWayFromNodeToCoastline(node);
        else if (type == Type.FARMLAND) way = linePathModel.removeWayFromNodeToFarmland(node);
        else if (type == Type.FOREST) way = linePathModel.removeWayFromNodeToForest(node);

        return way;
    }

    public void addToMapInModel(Type type, Node node, Way way) {
        if (type == Type.COASTLINE) linePathModel.addToNodeToCoastline(node, way);
        else if (type == Type.FARMLAND) linePathModel.addToNodeToFarmland(node, way);
        else if (type == Type.FOREST) linePathModel.addNodeToForest(node, way);
    }

    public Map<Node, Way> getNodeTo(Type type) {
        Map<Node, Way> nodeTo = null;
        if (type == Type.COASTLINE) nodeTo = linePathModel.getNodeToCoastline();
        else if (type == Type.FARMLAND) nodeTo = linePathModel.getNodeToFarmland();
        else if (type == Type.FOREST) nodeTo = linePathModel.getNodeToForest();

        return nodeTo;
    }

    public void addLinePathToModel(Type type, LinePath linePath) {
        linePathModel.addLinePathToList(type, linePath);
    }

    public void addTypeListToModel(Type type) {
        linePathModel.addTypeList(type);
    }

    public void createLinePaths() {
        linePathGenerator = LinePathGenerator.getInstance();
        linePathGenerator.createLinePaths();
    }

    public void clearLinePathData() {
        linePathGenerator = LinePathGenerator.getInstance();
        linePathGenerator.clearData();
        linePathModel.clearData();
    }

    public void setLinePathsOnModel(Map<Type, List<LinePath>> drawables) {
        linePathModel.setLinePaths(drawables);
    }

    public void setupRect() {
        Bounds bounds = getBoundsFromModel();

        kdTreeModel.setValuesOnRect(bounds.getMinLat(), bounds.getMaxLat(), bounds.getMinLon(), bounds.getMaxLon());
    }

    public Rect getRectFromModel() {
        return kdTreeModel.getRect();
    }

    public void addKDTreeToModel(Type type, List<LinePath> linePaths) {
        kdTreeModel.addKDTree(type, new KDTree(linePaths, getRectFromModel()));
    }

    public KDTree getKDTreeFromModel(Type type) {
        return kdTreeModel.getKDTree(type);
    }

    public void generateBinary() throws IOException {
        FileHandler fileHandler = FileHandler.getInstance();
        fileHandler.generateBinary();
    }
}