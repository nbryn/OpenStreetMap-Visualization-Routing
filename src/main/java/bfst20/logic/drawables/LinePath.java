package bfst20.logic.drawables;

import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.interfaces.Drawable;
import bfst20.logic.interfaces.OSMElement;
import javafx.scene.canvas.GraphicsContext;

public class LinePath implements Drawable {
    float[] coords;
    Type type;

    public LinePath(Way way, Type type, Map<Long, Node> OSMNodes) {

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
        trace(gc);
        gc.stroke();
    }

    @Override
    public Type getType() {
        return type;
    }

    public void trace(GraphicsContext gc) {
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i < coords.length; i += 2) {
            gc.lineTo(coords[i], coords[i + 1]);
        }
    }
}