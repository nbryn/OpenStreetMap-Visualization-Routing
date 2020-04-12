package bfst20.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.*;


public class OSMElementModel {
    private List<Way> OSMWays;
    private Map<Long, Node> nodeMap;
    private List<Relation> OSMRelations;
    private Map<Long, Address> addresses;
    private Bounds bounds;
    private static boolean isLoaded = false;
    private static OSMElementModel OSMElementModel;


    private OSMElementModel() {
        OSMWays = new ArrayList<>();
        nodeMap = new HashMap<>();
        OSMRelations = new ArrayList<>();
        addresses = new HashMap<>();
    }

    public static OSMElementModel getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            OSMElementModel = new OSMElementModel();
        }
        return OSMElementModel;
    }

    public void putAddress(long id, Address address) { addresses.put(id, address);}

    public void addRelation(Relation relation) {
        OSMRelations.add(relation);
    }

    public void setBounds(Bounds bounds) {

        this.bounds = new Bounds(bounds.getMaxLat(), bounds.getMinLat(), bounds.getMaxLon(), bounds.getMinLon());


    }

    public Bounds getBounds() {
        return bounds;
    }


    public void addToNodeMap(long id, Node node) {
        nodeMap.put(id, node);
    }

    public void addWay(Way way) {
        OSMWays.add(way);
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

    public Map<Long, Address> getAddresses(){return addresses;}

    public void clearData() {
        OSMWays = null;
        nodeMap = null;
        OSMRelations = null;
        addresses = null;
        System.gc();
    }
}