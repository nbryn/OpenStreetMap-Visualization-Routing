package bfst20.logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import bfst20.data.LinePathModel;
import bfst20.data.OSMElementModel;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.presentation.LinePath;
import bfst20.presentation.Parser;
import bfst20.presentation.View;
import javafx.scene.canvas.Canvas;

import javax.xml.stream.XMLStreamException;

public class AppController {

    private OSMElementModel OSMElementModel;
    private LinePathModel linePathModel;
    private Parser parser;
    private DrawableGenerator drawableGenerator;
    private View view;


    public AppController() {
        OSMElementModel = OSMElementModel.getInstance();
        linePathModel = LinePathModel.getInstance();
        parser = Parser.getInstance();
    }

    public void startParsing(File file) throws FileNotFoundException, XMLStreamException {
        parser.parseOSMFile(file);
    }

    public void createView(Canvas canvas) {
        view = new View(canvas);
    }

    public View initialize() {
        view.initializeData();

        return view;
    }

    public float getMinLonFromModel() {
        return OSMElementModel.getMinLon();
    }

    public float getMaxLonFromModel() {
        return OSMElementModel.getMaxLon();
    }

    public float getMinLatFromModel() {
        return OSMElementModel.getMinLat();
    }

    public float getMaxLatFromModel() {
        return OSMElementModel.getMaxLat();
    }

    public void addRelationToModel(Relation relation) {
        OSMElementModel.addRelation(relation);
    }

    public void setBoundsOnModel(float minLat, float maxLon, float maxLat, float minLon) {
        OSMElementModel.setBounds(minLat, maxLon, maxLat, minLon);
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



    public Map<Type, List<LinePath>> getDrawablesFromModel() {
        return linePathModel.getDrawables();
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

    public void createDrawables() {
        drawableGenerator = DrawableGenerator.getInstance();
        drawableGenerator.createDrawables();


    }
    public void clearDrawableData() {
        drawableGenerator = DrawableGenerator.getInstance();
        drawableGenerator.clearData();
        linePathModel.clearData();
    }

}