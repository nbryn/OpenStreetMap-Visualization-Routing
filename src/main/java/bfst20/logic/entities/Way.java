package bfst20.logic.entities;

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
    private Map<String, String> tags;
    private List<Long> nodeIds;
    private XMLStreamReader reader;
    private Color drawColor;
    private String firstKey, firstValue;

    public Way() {
        drawColor = Color.BLACK;
        tags = new HashMap<>();
        nodeIds = new ArrayList<Long>();
    }

    public Way(Way way) {
        this.nodeIds = new ArrayList<Long>(way.getNodeIds());
        this.id = way.getId();
        drawColor = Color.BLACK;
        tags = new HashMap<>();
    }
    

    public List<Long> getNodeIds(){
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
    
    public String getTagValue(String key){
        return tags.get(key);
    }

    public String[] getFirstTag(){
        String[] first = new String[2];
        first[0] = firstKey;
        first[1] = firstValue;
        return first;
    }

    public void addTag(String key, String value){
        tags.put(key, value);

        if(firstKey == null){
            firstKey = key;
            firstValue = value;
        }

        if  (key.equals("border_type") && value.equals("territorial")
        ||  (key.equals("route") && value.equals("ferry"))
        ||  (key.equals("boundary") && value.equals("protected_area"))){
            drawColor = Color.TRANSPARENT;
        }
    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }

    public long getFirstNodeId(){
        return nodeIds.get(0);
    }

    public long getLastNodeId(){
        return nodeIds.get(nodeIds.size() - 1);
    }

    public void addAllNodeIds(Way way){
        nodeIds.addAll(way.getNodeIds());
    }

    public static Way merge(Way before, Way after, Map<Long, Node> OSMNodes){
        if (before == null) return after;
        if (after == null) return before;

        Way res = new Way();
        if (before.getFirstNodeId() == after.getFirstNodeId()) {
            res.addAllNodeIds(before);
            Collections.reverse(res.getNodeIds());
            res.getNodeIds().remove(res.getNodeIds().size() - 1);
            res.addAllNodeIds(after);
        } else if (before.getFirstNodeId() == after.getLastNodeId()) {
            res.addAllNodeIds(after);
            res.getNodeIds().remove(res.getNodeIds().size() - 1);
            res.addAllNodeIds(before);
        } else if (before.getLastNodeId() == after.getFirstNodeId()) {
            res.addAllNodeIds(before);
            res.getNodeIds().remove(res.getNodeIds().size() - 1);
            res.addAllNodeIds(after);
        } else if (before.getLastNodeId() == after.getLastNodeId()) {
            Way tmp = new Way(after);
            Collections.reverse(tmp.getNodeIds());
            res.addAllNodeIds(before);
            res.getNodeIds().remove(res.getNodeIds().size() - 1);
            res.addAllNodeIds(tmp);
        } else {
            throw new IllegalArgumentException("Cannot merge unconnected OSMWays");
        }

        return res;
    }

}