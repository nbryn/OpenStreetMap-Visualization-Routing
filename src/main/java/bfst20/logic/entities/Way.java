package bfst20.logic.entities;

import bfst20.logic.misc.OSMType;
import bfst20.logic.misc.OSMElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Way implements OSMElement, Serializable {
    private List<Long> nodeIds;
    private boolean isOneWay;
    private int maxSpeed;
    private String name;
    private OSMType OSMType;
    private boolean multipolygon;
    private long id;


    public Way() {
        nodeIds = new ArrayList<>();
    }
    public Way(long id) {
        this.id = id;
        nodeIds = new ArrayList<>();
    }

    public Way(Way way) {
        this.nodeIds = new ArrayList<>(way.getNodeIds());
        this.id = way.getId();
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public boolean isOneWay() {
        return isOneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.isOneWay = oneWay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMultipolygon(boolean multipolygon) {
        this.multipolygon = multipolygon;
    }

    public boolean isMultipolygon(){
        return multipolygon;
    }

    public void setOSMType(OSMType OSMType) {
        this.OSMType = OSMType;
    }

    public OSMType getOSMType() {
        return OSMType;
    }

    public List<Long> getNodeIds() {
        return nodeIds;
    }

    public long getId() {
        return id;
    }

    public void addNodeId(long nodeid) {
        nodeIds.add(nodeid);
    }

    public long getFirstNodeId() {
        return nodeIds.get(0);
    }

    public long getLastNodeId() {
        return nodeIds.get(nodeIds.size() - 1);
    }

    public void addAllNodeIds(Way way) {
        nodeIds.addAll(way.getNodeIds());
    }


}