package bfst20.data;

import bfst20.logic.misc.OSMType;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.entities.LinePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinePathData {

    private static boolean isLoaded = false;
    private static LinePathData linePathData;
    private Map<OSMType, List<LinePath>> linePaths;
    private List<LinePath> highWays;

    private Map<OSMType, Map<Node, Way>> nodeTo;

    private LinePathData() {
        linePaths = new HashMap<>();

        nodeTo = new HashMap<>();
        highWays = new ArrayList<>();
    }

    public static LinePathData getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            linePathData = new LinePathData();
        }
        return linePathData;
    }

    public void saveHighways(List<LinePath> highways) {
        this.highWays = highways;
    }

    public List<LinePath> getHighWays() {
        return this.highWays;
    }

    public Map<OSMType, List<LinePath>> getLinePaths() {
        return linePaths;
    }

    public void addLinePath(OSMType OSMType, LinePath linePath) {
        if (linePaths.get(OSMType) == null) linePaths.put(OSMType, new ArrayList<>());
        linePaths.get(OSMType).add(linePath);
    }

    public void setLinePaths(Map<OSMType, List<LinePath>> linePaths) {
        this.linePaths = linePaths;
    }


    public void addType(OSMType OSMType) {
        linePaths.put(OSMType, new ArrayList<>());
    }

    public void addNodeTo(OSMType osmType, Node node, Way way){

        if(nodeTo.get(osmType) == null){
            nodeTo.put(osmType, new HashMap<>());
        }

        nodeTo.get(osmType).put(node, way);
    }


    public Map<Node, Way> getNodeTo(OSMType osmType){return nodeTo.get(osmType);}


    public Way removeWayFromNodeTo(OSMType osmType, Node node) {
        if(nodeTo.get(osmType) == null) return null;
        return nodeTo.get(osmType).remove(node);
    }


    public void clearData() {
        linePaths = new HashMap<>();
            
        nodeTo = new HashMap<>();
        System.gc();
    }

}
