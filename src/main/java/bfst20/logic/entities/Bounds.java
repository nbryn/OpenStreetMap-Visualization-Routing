package bfst20.logic.entities;

import bfst20.logic.Type;
import bfst20.logic.interfaces.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;

public class Bounds implements Drawable, Serializable {

    float maxLat, minLat, maxLon, minLon;

    public Bounds (float maxLat, float minLat, float maxLon, float minLon) {
        this.maxLat = maxLat;
        this.minLat = minLat;
        this.maxLon = maxLon;
        this.minLon = minLon;
    }

    public float getMaxLat() {
        return maxLat;
    }

    public float getMaxLon() {
        return maxLon;
    }

    public float getMinLon() {
        return minLon;
    }

    public float getMinLat() {
        return minLat;
    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public Type getType() {
        return null;
    }
}
