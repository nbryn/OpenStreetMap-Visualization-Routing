package bfst20.logic.kdtree;

import bfst20.presentation.LinePath;

public class Rect {

    private float minlat, maxlat, minlon, maxlon;

    public Rect(float minlat,float maxlat,float minlon,float maxlon){
        this.maxlat = maxlat;
        this.maxlon = maxlon;
        this.minlat = minlat;
        this.minlon = minlon;
    }

    public float getMaxlat() {
        return maxlat;
    }

    public float getMaxlon() {
        return maxlon;
    }

    public float getMinlat() {
        return minlat;
    }

    public float getMinlon() {
        return minlon;
    }


    /*public boolean intersectsRight(KdNode node) {
        LinePath path = node.getLinePath();
        return path.getMinX() >= minlat || path.getMaxY() <= maxlon;
    }*/

    public boolean intersects(KdNode node) {
        LinePath path = node.getLinePath();
        return      path.getMaxX() >= minlat
                ||  path.getMinX() <= maxlat
                ||  path.getMaxY() >= minlon
                ||  path.getMinY() <= maxlon;
    }

    public boolean contains(KdNode node){
        LinePath path = node.getLinePath();
        return (path.getMaxX() >= minlat) && (path.getMinX() <= maxlat)
                && (path.getMaxY() >= minlon) && (path.getMinY() <= maxlon);
    }


}
