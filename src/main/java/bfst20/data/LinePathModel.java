package bfst20.data;

import bfst20.logic.Type;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.entities.LinePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinePathModel {

    private static boolean isLoaded = false;
    private static LinePathModel linePathModel;
    private Map<Type, List<LinePath>> linePaths;
    private Map<Node, Way> nodeToCoastline;
    private Map<Node, Way> nodeToForest;
    private Map<Node, Way> nodeToFarmland;

    private LinePathModel() {
        linePaths = new HashMap<>();
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

    public Map<Type, List<LinePath>> getLinePaths() {
        return linePaths;
    }

    public void addLinePath(Type type, LinePath linePath) {
        if (linePaths.get(type) == null) linePaths.put(type, new ArrayList<>());
        linePaths.get(type).add(linePath);
    }

    public void setLinePaths(Map<Type, List<LinePath>> linePaths) {
        this.linePaths = linePaths;
    }


    public void addType(Type type) {
        linePaths.put(type, new ArrayList<>());
    }


    public Map<Node, Way> getNodeToCoastline() {
        return nodeToCoastline;

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


    public void clearData() {
        linePaths = new HashMap<>();
        nodeToCoastline = new HashMap<>();
        nodeToForest = new HashMap<>();
        nodeToFarmland = new HashMap<>();
        System.gc();
    }
}
