package bfst20.logic.entities;

import bfst20.logic.interfaces.OSMElement;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Node implements OSMElement {
    private long id;
    private double latitude;
    private double longitude;
    private List<Tag> tags;
    private XMLStreamReader reader;

    public Node() {
        tags = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    @Override
    public void setValues() {
        setId();
        setLatitude();
        setLongitude();
    }

    private void setId(){
        id = Long.parseLong(reader.getAttributeValue(null, "id"));

    }

    private void setLatitude(){
        latitude = -Double.parseDouble(reader.getAttributeValue(null, "lat"));


    }

    private void setLongitude(){
        longitude = Double.parseDouble(reader.getAttributeValue(null, "lon")) * 0.56f;

    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }

}
