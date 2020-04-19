package bfst20.logic.entities;

import bfst20.logic.Type;
import bfst20.logic.interfaces.OSMElement;
import bfst20.logic.routing.Edge;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Node implements OSMElement, Comparable {
    private long id;
    private float latitude;
    private float longitude;
    private double distTo;

    private XMLStreamReader reader;

    public Node() {
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

    public void setValues() {
        setId();
        setLatitude();
        setLongitude();
    }

    private void setId() {
        id = Long.parseLong(reader.getAttributeValue(null, "id"));

    }

    public void setDistTo(double distTo) {
        this.distTo = distTo;
    }

    public double getDistTo() {
        return distTo;
    }

    private void setLatitude() {
        latitude = -Float.parseFloat(reader.getAttributeValue(null, "lat"));

    }

    private void setLongitude() {
        longitude = Float.parseFloat(reader.getAttributeValue(null, "lon")) * 0.56f;

    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }

    @Override
    public void setName(String name) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setType(Type type) {
        // TODO Auto-generated method stub

    }

    @Override
    public int compareTo(Object other) {
        return Double.compare(this.distTo, ((Node) other).getDistTo());
    }
}
