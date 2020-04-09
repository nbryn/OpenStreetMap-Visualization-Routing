package bfst20.data;

import bfst20.logic.Type;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.presentation.LinePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinePathModel {

    private static boolean isLoaded = false;
    private static LinePathModel linePathModel;
    private Map<Type, List<LinePath>> drawables;
    private Map<Node, Way> nodeToCoastline;
    private Map<Node, Way> nodeToForest;
    private Map<Node, Way> nodeToFarmland;

    private LinePathModel() {
        drawables = new HashMap<>();
        nodeToCoastline = new HashMap<>();
        nodeToForest = new HashMap<>();
        nodeToFarmland = new HashMap<>();
    }

    public static LinePathModel getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            linePathModel = new LinePathModel();
        }
        return linePathModel;
    }

    public Map<Type, List<LinePath>> getDrawables() {
        return drawables;
    }


    public Map<Node, Way> getNodeToCoastline() {
        return nodeToCoastline;

    }

    public Map<Node, Way> getNodeToForest() {
        return nodeToForest;

    }

    public Map<Node, Way> getNodeToFarmland() {
        return nodeToFarmland;

    }

    public Way removeWayFromNodeToForest(Node node) {
        return nodeToForest.remove(node);
    }

    public Way removeWayFromNodeToFarmland(Node node) {
        return nodeToFarmland.remove(node);


    }

    public Way removeWayFromNodeToCoastline(Node node) {
        return nodeToCoastline.remove(node);

    }

    public void addNodeToForest(Node node, Way way) {
        nodeToForest.put(node, way);
    }

    public void addToNodeToFarmland(Node node, Way way) {
        nodeToFarmland.put(node, way);


    }

    public void addToNodeToCoastline(Node node, Way way) {
        nodeToCoastline.put(node, way);

    }

    public void addLinePathToList(Type type, LinePath linePath) {
        drawables.get(type).add(linePath);
    }

    public void addTypeList(Type type) {
        drawables.put(type, new ArrayList<>());
    }

    public void clearData() {
        drawables = null;
        nodeToCoastline = null;
        nodeToForest = null;
        nodeToFarmland = null;
        System.gc();
    }
}
