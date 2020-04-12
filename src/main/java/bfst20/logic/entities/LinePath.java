package bfst20.logic.entities;

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
    boolean fill;
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

    public boolean getFill() {
        return this.fill;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public Long getWayId() {
        return wayId;
    }

}