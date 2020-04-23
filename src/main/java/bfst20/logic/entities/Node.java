package bfst20.logic.entities;

import bfst20.logic.misc.OSMType;
import bfst20.logic.misc.OSMElement;

import javax.xml.stream.XMLStreamReader;

public class Node implements OSMElement, Comparable {
    private long id;
    protected float latitude;
    protected float longitude;
    private double distTo;


    public Node(long id, float latitude, float longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Node(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public long getId() {
        return id;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }


    public void setDistTo(double distTo) {
        this.distTo = distTo;
    }

    public double getDistTo() {
        return distTo;
    }


    @Override
    public void setName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setOSMType(OSMType OSMType) {
        // TODO Auto-generated method stub

    }

    @Override
    public int compareTo(Object other) {
        return Double.compare(this.distTo, ((Node) other).getDistTo());
    }
}
