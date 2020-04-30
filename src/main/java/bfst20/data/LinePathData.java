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
    private Map<OSMType, List<LinePath>> linePaths;
    private Map<OSMType, Map<Node, Way>> nodeTo;
    private List<LinePath> coastline;
    private List<LinePath> motorway;
    private static LinePathData linePathData;
    private static boolean isLoaded = false;
    private List<LinePath> highWays;

    private LinePathData() {
        linePaths = new HashMap<>();
        nodeTo = new HashMap<>();
        highWays = new ArrayList<>();
        coastline = new ArrayList<>();
        motorway = new ArrayList<>();
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
        if(OSMType == OSMType.COASTLINE){
            coastline.add(linePath);
            return;
        }else if(OSMType == OSMType.MOTORWAY){
            motorway.add(linePath);
            return;
        }
        linePaths.get(OSMType).add(linePath);
    }

    public List<LinePath> getCoastlines(){
        return coastline;
    }

    public List<LinePath> getMotorway() {
        return motorway;
    }

    public void addCoastLine(List<LinePath> paths){
        this.coastline = paths;
    }

    public void addMotorway(List<LinePath> motorway){
        this.motorway = motorway;
    }

    public void saveLinePaths(Map<OSMType, List<LinePath>> linePaths) {
        this.linePaths = linePaths;
    }

    public void addType(OSMType type) {
        linePaths.put(type, new ArrayList<>());
    }

    public void addNodeTo(OSMType type, Node node, Way way) {
        if (nodeTo.get(type) == null) {
            nodeTo.put(type, new HashMap<>());
        }

        nodeTo.get(type).put(node, way);
    }

    public Map<Node, Way> getNodeTo(OSMType type) {
        return nodeTo.get(type);
    }

    public Way removeWayFromNodeTo(OSMType type, Node node) {
        if (nodeTo.get(type) == null) return null;

        return nodeTo.get(type).remove(node);
    }

    public void clearData() {
        linePaths = new HashMap<>();
        nodeTo = new HashMap<>();

        System.gc();
    }

	public void addSingleCoastLine(LinePath linePath) {
        if(coastline == null){
            coastline = new ArrayList<>();
        }

        coastline.add(linePath);
    }
    
    public void addSingleMotorway(LinePath linePath) {
        if(motorway == null){
            motorway = new ArrayList<>();
        }

        motorway.add(linePath);
	}
}
