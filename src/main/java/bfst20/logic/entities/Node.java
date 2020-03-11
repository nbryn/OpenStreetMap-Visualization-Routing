package bfst20.logic.entities;

import bfst20.logic.interfaces.OSMElement;
import javafx.scene.canvas.GraphicsContext;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        id = Long.parseLong(reader.getAttributeValue(null, "id"));
        latitude = -1 * Double.parseDouble(reader.getAttributeValue(null, "lat"));
        longitude = Double.parseDouble(reader.getAttributeValue(null, "lon")) * 0.56f;
    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }

}
