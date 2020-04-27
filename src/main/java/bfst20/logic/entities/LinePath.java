package bfst20.logic.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import bfst20.logic.misc.OSMType;

public class LinePath implements Serializable {
    private float[] coords;
    private OSMType OSMType;
    private boolean fill;
    private float minY, minX, maxY, maxX, centerLatitude, centerLongitude;
    private Bounds bounds;
    private String name;
    private long wayId;
    private Way way;
    private boolean multiploygon;

    public LinePath(float maxLat, float maxLon, float minLat, float minLon) {
        this.bounds = new Bounds(maxLat, minLat, maxLon, minLon);
        minY = minLon;
        maxY = maxLon;
        minX = minLat;
        maxX = maxLat;
        centerLatitude = (maxX - minX) / 2 + minX;
        centerLongitude = (maxY - minY) / 2 + minY;
    }

    public void setWayNull(){
        way = null;
        wayId = 0;
    }

    public LinePath(Way way, OSMType OSMType, Map<Long, Node> OSMNodes, Boolean fill) {
        name = way.getName();
        wayId = way.getId();
        this.way = way;
        this.fill = fill;
        this.OSMType = OSMType;
        List<Long> nodeIds = way.getNodeIds();

        minY = Float.POSITIVE_INFINITY;
        minX = Float.POSITIVE_INFINITY;
        maxY = Float.NEGATIVE_INFINITY;
        maxX = Float.NEGATIVE_INFINITY;

        coords = new float[nodeIds.size() * 2];
        for (int i = 0; i < nodeIds.size(); i++) {
            if(nodeIds.get(i) == -99999){
                coords[i * 2] = -99999;
                coords[i * 2 + 1] = -99999;
            }else{
                coords[i * 2] = OSMNodes.get(nodeIds.get(i)).getLongitude();
                coords[i * 2 + 1] = OSMNodes.get(nodeIds.get(i)).getLatitude();

                if (minX > coords[i * 2 + 1]) minX = coords[i * 2 + 1];
                if (minY > coords[i * 2]) minY = coords[i * 2];
                if (maxX < coords[i * 2 + 1]) maxX = coords[i * 2 + 1];
                if (maxY < coords[i * 2]) maxY = coords[i * 2];
            }


        }

        centerLatitude = (maxX - minX) / 2 + minX;
        centerLongitude = (maxY - minY) / 2 + minY;

    }

    public boolean isMultiploygon(){return multiploygon;}

    public void setMultiploygon(boolean multiploygon){this.multiploygon = multiploygon;}

    public Bounds getBounds() {
        return this.bounds;
    }

    public float getCenterLatitude() {
        return centerLatitude;
    }

    public float getCenterLongitude() {
        return centerLongitude;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float[] getCoords() {
        return coords;
    }

    public boolean getFill() {
        return this.fill;
    }

    public String getName() {
        return name;
    }

    public OSMType getOSMType() {
        return OSMType;
    }

    public Way getWay() {
        return this.way;
    }

    public Long getWayId() {
        return wayId;
    }

}