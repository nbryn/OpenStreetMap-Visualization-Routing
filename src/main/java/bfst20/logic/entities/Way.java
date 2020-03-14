package bfst20.logic.entities;

import bfst20.logic.interfaces.Drawable;
import bfst20.logic.interfaces.OSMElement;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import javax.xml.stream.XMLStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Way implements OSMElement, Drawable {
    private long id;
    private List<OSMElement> tags;
    private List<Node> nodes;
    private XMLStreamReader reader;

    public Way() {
        tags = new ArrayList<OSMElement>();
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

    public void addTag(OSMElement tag) {
        this.tags.add(tag);
    }

    @Override
    public void setReader(XMLStreamReader reader) {
        this.reader = reader;
    }

    @Override
    public void Draw(GraphicsContext gc) {
        Node start = nodes.get(0);

        gc.moveTo(start.getLatitude(), start.getLongitude());



        gc.setFill(Color.BLACK);


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