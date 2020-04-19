package bfst20.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.*;


public class OSMElementData {
    private List<Way> OSMWays;
    private Map<Long, Node> nodeMap;
    private List<Relation> OSMRelations;
    private Bounds bounds;
    private static boolean isLoaded = false;
    private static OSMElementData OSMElementData;


    private OSMElementData() {
        OSMWays = new ArrayList<>();
        nodeMap = new HashMap<>();
        OSMRelations = new ArrayList<>();
    }

    public static OSMElementData getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            OSMElementData = new OSMElementData();
        }
        return OSMElementData;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = new Bounds(bounds.getMaxLat(), bounds.getMinLat(), bounds.getMaxLon(), bounds.getMinLon());

    }

    public Node getNode(long id) {
        return nodeMap.get(id);
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void addToNodeMap(long id, Node node) {
        nodeMap.put(id, node);
    }

    public Map<Long, Node> getOSMNodes() {
        return nodeMap;
    }

    public void addWay(Way way) {
        OSMWays.add(way);
    }

    public List<Way> getOSMWays() {
        return OSMWays;
    }

    public void addRelation(Relation relation) {
        OSMRelations.add(relation);
    }

    public List<Relation> getOSMRelations() {
        return OSMRelations;
    }

    public void clearData() {
        OSMWays = new ArrayList<>();
        OSMRelations = new ArrayList<>();

    }

    public void clearNodeData() {
      /*  nodeMap = new HashMap<>();

        System.gc();*/
    }
}