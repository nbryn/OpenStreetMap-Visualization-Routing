package bfst20.logic.entities;

import bfst20.logic.Type;
import bfst20.logic.interfaces.Drawable;
import bfst20.logic.interfaces.OSMElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.LongSupplier;

public class Way implements OSMElement {


    private long id;
    private List<Long> nodeIds;
    private XMLStreamReader reader;
    private Color drawColor;
    private String firstKey, firstValue;

    private String name;
    private Type type;

    public Way() {
        drawColor = Color.BLACK;
        nodeIds = new ArrayList<Long>();
    }

    public Way(Way way) {
        this.nodeIds = new ArrayList<Long>(way.getNodeIds());
        this.id = way.getId();
        drawColor = Color.BLACK;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(Type type){
        this.type = type;
    }

    
    public Type getType(){
        return type;
    }

    public String getName(){
        return name;
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

    @Override
    public void setValues() {
        id = Long.parseLong(reader.getAttributeValue(null, "id"));
    }
    
    public String[] getFirstTag() {
        String[] first = new String[2];
        first[0] = firstKey;
        first[1] = firstValue;
        return first;
    }


    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
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