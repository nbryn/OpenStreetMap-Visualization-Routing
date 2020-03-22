package bfst20.presentation;

import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;
import javafx.scene.canvas.GraphicsContext;
import bfst20.logic.Type;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class LinePath implements Drawable {
    float[] coords;
    Type type;
    Color color;
    Boolean fill;
    Way way;

    public LinePath(Way way, Type type, Map<Long, Node> OSMNodes, Color color, Boolean fill) {
        this.color = color;
        this.fill = fill;
        this.way = way;
        List<Long> nodeIds = way.getNodeIds();

        coords = new float[nodeIds.size() * 2];
        for (int i = 0; i < nodeIds.size(); ++i) {
            coords[i * 2] = OSMNodes.get(nodeIds.get(i)).getLongitude();
            coords[i * 2 + 1] = OSMNodes.get(nodeIds.get(i)).getLatitude();
        }
        this.type = type;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.beginPath();
        gc.setStroke(color);
        gc.setFill(fill ? color : Color.TRANSPARENT);

        if(way.getTagValue("name") != null){
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(0.00022));
            gc.fillText(way.getTagValue("name"), coords[0], coords[1]);
            gc.setFill(fill ? color : Color.TRANSPARENT);
        }

        trace(gc);
        gc.stroke();
    }

    @Override
    public Type getType() {
        return type;
    }

    private void trace(GraphicsContext gc) {
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i < coords.length; i += 2) {
            gc.lineTo(coords[i], coords[i + 1]);
        }
    }
}