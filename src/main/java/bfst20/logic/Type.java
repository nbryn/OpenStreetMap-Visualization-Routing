package bfst20.logic;

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
    FARMLAND,
    LANDUSE,
    PLACE;

    public static Boolean getFill(Type type){
        switch (type) {
            case NATURAL:
            case FARMLAND:
            case FOREST:
            case WATER:
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
            case GREEN:
                return Color.LIGHTGREEN;
            case WATER:
                return Color.BLUE;
            case BUILDING:
                return Color.BROWN;
            case HIGHWAY:
                return Color.BLACK;
            case HEATH:
            case FARMLAND:
                return Color.rgb(238,240,213);
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