package bfst20.presentation;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

import bfst20.logic.entities.Address;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;

import javafx.scene.canvas.GraphicsContext;
import bfst20.logic.Type;
import javafx.scene.paint.Color;

public class LinePath implements Serializable {
    float[] coords;
    Type type;
    Boolean fill;
    float minY, minX, maxY, maxX, centerLatitude, centerLongitude;
    Bounds bounds;
    String name;
    long wayId;

    public LinePath(float maxLat, float maxLon, float minLat, float minLon) {
        this.bounds = new Bounds(maxLat, minLat, maxLon, minLon);

    }

    public LinePath(Way way, Type type, Map<Long, Node> OSMNodes, Map<Long, Address> addresses, Boolean fill) {
        name = way.getName();
        wayId = way.getId();
        this.fill = fill;
        List<Long> nodeIds = way.getNodeIds();

        minY = Float.POSITIVE_INFINITY;
        minX = Float.POSITIVE_INFINITY;
        maxY = Float.NEGATIVE_INFINITY;
        maxX = Float.NEGATIVE_INFINITY;

        coords = new float[nodeIds.size() * 2];
        for (int i = 0; i < nodeIds.size(); ++i) {
            coords[i * 2] = OSMNodes.get(nodeIds.get(i)).getLongitude();
            coords[i * 2 + 1] = OSMNodes.get(nodeIds.get(i)).getLatitude();

            if (minX > coords[i * 2 + 1]) minX = coords[i * 2 + 1];
            if (minY > coords[i * 2]) minY = coords[i * 2];
            if (maxX < coords[i * 2 + 1]) maxX = coords[i * 2 + 1];
            if (maxY < coords[i * 2]) maxY = coords[i * 2];

        }

        centerLatitude = (maxX - minX) / 2 + minX;
        centerLongitude = (maxY - minY) / 2 + minY;

        this.type = type;
    }

    public Bounds getBounds() {
        return this.bounds;
    }

    public float getCenterLatitude() {
        return centerLatitude;
    }

    public float getCenterLongitude() {
        return centerLongitude;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMinY() {
        return minY;
    }

    public float[] getCoords() {
        return coords;
    }


    public String getName(){return name;}
    public Long getWayId(){return wayId;}




        public void draw(GraphicsContext gc, double lineWidth,boolean isColorBlindMode) {
        gc.setLineWidth(Type.getLineWidth(type, lineWidth));
        gc.beginPath();
        gc.setStroke(Type.getColor(type,isColorBlindMode));
        gc.setFill(fill ? Type.getColor(type,isColorBlindMode) : Color.TRANSPARENT);

        /*if(way.getTagValue("name") != null){
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(0.00022));
            gc.fillText(way.getTagValue("name"), coords[0], coords[1]);
            gc.setFill(fill ? color : Color.TRANSPARENT);
        }*/
        //gc.setStroke(Color.BLUE);
        //gc.strokeRect(minY, minX, maxY-minY, maxX-minX);
        //  gc.setStroke(color);


        trace(gc);
        gc.stroke();
    }

    public Type getType() {
        return type;
    }

    private void trace(GraphicsContext gc) {
        gc.moveTo(coords[0], coords[1]);
        for (int i = 2; i <= coords.length; i += 2) {
            gc.lineTo(coords[i - 2], coords[i - 1]);
        }
    }
}