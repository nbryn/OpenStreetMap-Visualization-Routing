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

public class Way implements OSMElement, Drawable {
    private long id;
    private Map<String, String> tags;
    private List<Node> nodes;
    private XMLStreamReader reader;
    private Color drawColor;

    public Way() {
        drawColor = Color.BLACK;
        tags = new HashMap<>();
        nodes = new ArrayList<Node>();
    }

    public long getId() {
        return id;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public void setValues() {
        id = Long.parseLong(reader.getAttributeValue(null, "id"));
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

    @Override
    public void Draw(GraphicsContext gc) {
        Node start = nodes.get(0);

        gc.moveTo(start.getLatitude(), start.getLongitude());


        gc.setStroke(drawColor);


        for(int i = 1; i< nodes.size(); i++){
            Node node1 = nodes.get(i-1);
            Node node2 = nodes.get(i);

            gc.beginPath();
            gc.moveTo(node1.getLongitude(), node1.getLatitude());
            gc.lineTo(node2.getLongitude(), node2.getLatitude());
            gc.stroke();
        }

    }
}