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
    PARKING,
    NATURAL,
    PLACE;

    public static Boolean getFill(Type type){
        switch (type) {
            case COASTLINE:
            case BUILDING:
                return true;
            default: //TODO FIX
                return false;
        }
    } 

    public static Color getColor(Type type) {
        switch (type) {
            case PLACE:
            case COASTLINE:
                return Color.GREEN;
            case WATER:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case BUILDING:
                return Color.BROWN;
            case HIGHWAY:
                return Color.BLACK;
            case HEATH:
                return Color.YELLOW;
            case FOREST:
                return Color.DARKGREEN;
            case PARKING:
                return Color.RED;
            case NATURAL:
                return Color.BLUEVIOLET;
            default: //TODO FIX
                return Color.TRANSPARENT;
        }
    }
}