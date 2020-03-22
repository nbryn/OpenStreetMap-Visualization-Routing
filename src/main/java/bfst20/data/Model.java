package bfst20.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;

public class Model {
    private List<Way> OSMWays;
    private Map<Long, Node> nodeMap;
    private List<Relation> OSMRelations;
    private float minlat, maxlon, maxlat, minlon;
    private static boolean isLoaded = false;
    private static Model model;

    private Model() {
        OSMWays = new ArrayList<>();
        nodeMap = new HashMap<>();
        OSMRelations = new ArrayList<>();
    }

    public static Model getInstance(){
        if(isLoaded == false){
            isLoaded = true;
            model = new Model();
        }
        return model;
    }

    public void addRelation(Relation relation ){
        OSMRelations.add(relation);
    }

    public void setBounds(float minlat, float maxlon, float maxlat, float minlon){
        this.minlat = minlat;
        this.maxlon = maxlon;
        this.maxlat = maxlat;
        this.minlon = minlon;
    }

    public void addToNodeMap(long id, Node node){
        nodeMap.put(id, node);
    }

    public void addWay(Way way){
        OSMWays.add(way);
    }

    public float getMinLat() {
        return minlat;
    }

    public float getMaxLon() {
        return maxlon;
    }

    public float getMaxLat() {
        return maxlat;
    }

    public float getMinLon() {
        return minlon;
    }

    public List<Way> getOSMWays(){
        return OSMWays;
    }

    public Map<Long, Node> getOSMNodes(){
        return nodeMap;
    }

    public List<Relation> getOSMRelations() {
        return OSMRelations;
    }

    
}