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
    public void setLinePaths(Map<Type, List<LinePath>> linePaths) {
        this.linePaths = linePaths;
    }

    public void addLinePathToList(Type type, LinePath linePath) {
        linePaths.get(type).add(linePath);
    }

    public void addTypeList(Type type) {
        linePaths.put(type, new ArrayList<>());
    }

    public void clearData() {
        linePaths = null;
        nodeToCoastline = null;
        nodeToForest = null;
        nodeToFarmland = null;
        System.gc();
    }
}
