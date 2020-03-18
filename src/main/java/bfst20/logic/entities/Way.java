package bfst20.logic.entities;

import bfst20.logic.interfaces.Drawable;
import bfst20.logic.interfaces.OSMElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Way implements OSMElement {
    private long id;
    private Map<String, String> tags;
    private List<Long> nodeIds;
    private XMLStreamReader reader;
    private Color drawColor;

    public Way() {
        drawColor = Color.BLACK;
        tags = new HashMap<>();
        nodeIds = new ArrayList<Long>();
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

    public void addTag(String key, String value){
        tags.put(key, value);

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
}