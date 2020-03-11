package bfst20.logic.entities;

import bfst20.logic.interfaces.Drawable;
import bfst20.logic.interfaces.OSMElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Way implements OSMElement, Drawable {
    private long id;
    private List<OSMElement> tags;
    private List<OSMElement> nodes;
    private XMLStreamReader reader;

    public Way() {
        tags = new ArrayList<OSMElement>();
        nodes = new ArrayList<OSMElement>();
    }

    public long getId() {
        return id;
    }

    public void addNode(OSMElement node) {
        this.nodes.add(node);
    }

    @Override
    public void setValues() {
        id = Long.parseLong(reader.getAttributeValue(null, "id"));
    }

    public void addTag(OSMElement tag) {
        this.tags.add(tag);
    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }

    @Override
    public void Draw(GraphicsContext gc) {
        Node start = (Node) nodes.get(0);

        gc.beginPath();
        gc.moveTo(start.getLatitude(), start.getLongitude());

        for(int i = 0; i< nodes.size(); i++){
            Node node1 = (Node) nodes.get(i);
            if(i+1 == nodes.size() - 1) return;
            Node node2 = (Node) nodes.get(i+1);

            gc.beginPath();
            gc.moveTo(node1.getLongitude(), node1.getLatitude());
            gc.lineTo(node2.getLongitude(), node2.getLatitude());
            gc.stroke();
        }

        gc.stroke();

    }
}
