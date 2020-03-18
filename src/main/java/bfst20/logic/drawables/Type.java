package bfst20.logic.drawables;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public enum Type {
    UNKNOWN,
    BUILDING,
    HIGHWAY,
    COASTLINE,
    WATER,
    GREEN,
    HEATH,
    FOREST,
    PARKING;

    public static Paint getColor(Type type) {
        switch (type) {
            case WATER:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case BUILDING:
                return Color.BROWN;
            case HIGHWAY:
                return Color.RED;
            case HEATH:
                return Color.YELLOW;
            case FOREST:
                return Color.DARKGREEN;
            case PARKING:
                return Color.LIGHTGREY;
            default:
                return Color.BLACK;
        }
    }
}