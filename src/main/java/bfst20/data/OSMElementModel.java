package bfst20.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.Type;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import bfst20.presentation.LinePath;

public class OSMElementModel {
    private List<Way> OSMWays;
    private Map<Long, Node> nodeMap;
    private List<Relation> OSMRelations;
    private Bounds bounds;
    private float minLat, maxLon, maxLat, minLon;
    private static boolean isLoaded = false;
    private static OSMElementModel OSMElementModel;


    private OSMElementModel() {
        OSMWays = new ArrayList<>();
        nodeMap = new HashMap<>();
        OSMRelations = new ArrayList<>();
    }

    public static OSMElementModel getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            OSMElementModel = new OSMElementModel();
        }
        return OSMElementModel;
    }

    public void addRelation(Relation relation) {
        OSMRelations.add(relation);
    }

    public void setBounds(Bounds bounds) {
        this.minLat = bounds.getMinLat();
        this.maxLon = bounds.getMaxLon();
        this.maxLat = bounds.getMaxLat();
        this.minLon = bounds.getMinLon();
        this.bounds = new Bounds(maxLat, minLat, maxLon, minLon);


    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(float minLat, float maxLon, float maxLat, float minLon) {
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.maxLat = maxLat;
        this.minLon = minLon;
        this.bounds = new Bounds(maxLat, minLat, maxLon, minLon);
    }

    public void addToNodeMap(long id, Node node) {
        nodeMap.put(id, node);
    }

    public void addWay(Way way) {
        OSMWays.add(way);
    }

    public float getMinLat() {
        return minLat;
    }

    public float getMaxLon() {
        return maxLon;
    }

    public float getMaxLat() {
        return maxLat;
    }

    public float getMinLon() {
        return minLon;
    }



    public List<Way> getOSMWays() {
        return OSMWays;
    }

    public Map<Long, Node> getOSMNodes() {
        return nodeMap;
    }

    public List<Relation> getOSMRelations() {
        return OSMRelations;
    }

    public void clearData() {
        OSMWays = null;
        nodeMap = null;
        OSMRelations = null;
        System.gc();
    }
}