package bfst20.logic.kdtree;

import bfst20.presentation.LinePath;

public class Rect {

    private float minLat, maxLat, minLon, maxLon;

    public Rect() {

    }

    public Rect(float minLat, float maxLat, float minLon, float maxLon){
        this.maxLat = maxLat;
        this.maxLon = maxLon;
        this.minLat = minLat;
        this.minLon = minLon;
    }

    public void setMinLat(float minLat) {
        this.minLat = minLat;
    }

    public void setMaxLat(float matLat) {
        this.maxLat = matLat;
    }

    public void setMinLon(float minLon) {
        this.minLon = minLon;
    }

    public void setMaxLon(float maxLon) {
        this.maxLon = maxLon;
    }

    public float getMaxLat() {
        return maxLat;
    }

    public float getMaxLon() {
        return maxLon;
    }

    public float getMinLat() {
        return minLat;
    }

    public float getMinLon() {
        return minLon;
    }


    /*public boolean intersectsRight(KdNode node) {
        LinePath path = node.getLinePath();
        return path.getMinX() >= minlat || path.getMaxY() <= maxlon;
    }*/

    public boolean intersects(KDNode node) {
        LinePath path = node.getLinePath();
        return      path.getMaxX() >= minLat
                ||  path.getMinX() <= maxLat
                ||  path.getMaxY() >= minLon
                ||  path.getMinY() <= maxLon;
    }

    public boolean contains(KDNode node){
        LinePath path = node.getLinePath();
        return (path.getMaxX() >= minLat) && (path.getMinX() <= maxLat)
                && (path.getMaxY() >= minLon) && (path.getMinY() <= maxLon);
    }


}
