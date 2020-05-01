package bfst20.logic.misc;

import javafx.scene.paint.Color;

public enum OSMType {
    UNKNOWN,
    BEACH,
    BOUNDS,
    BUILDING,
    HIGHWAY,
    MOTORWAY,
    TERTIARY,
    ROUTING,
    RESIDENTIAL_HIGHWAY,
    UNCLASSIFIED_HIGHWAY,
    FOOTWAY,
    CYCLEWAY,
    PATH,
    TRACK,
    UNCLASSIFIED,
    COASTLINE,
    WATER,
    GREEN,
    HEATH,
    FOREST,
    PARKING,
    NATURAL,
    FARMLAND,
    TREE_ROW,
    MEADOW,
    LANDUSE,
    RESIDENTIAL,
    WOOD,
    MULTIPOLYGON,
    PLACE;

    public static double getLineWidth(OSMType OSMType, double lineWidth) {
        switch (OSMType) {
            case TERTIARY:
            case UNCLASSIFIED_HIGHWAY:
            case HIGHWAY:
            case MOTORWAY:
            case RESIDENTIAL_HIGHWAY:
                return lineWidth * 2;
            case FOOTWAY:
            case PATH:
            case TRACK:
            case CYCLEWAY:
                return lineWidth * 1;
            default:
                return lineWidth;
        }
    }

    public static double getZoomLevel(OSMType OSMType) {
        switch (OSMType) {
            case COASTLINE:
            case MOTORWAY:
            case ROUTING:
                return 1;
            case FOREST:
            case HEATH:
            case WATER:
            case FARMLAND:
            case NATURAL:
            case WOOD:
            case TREE_ROW:
            case BEACH:
            case GREEN:
            case HIGHWAY:
            case TERTIARY:
            case MEADOW:
            case UNCLASSIFIED_HIGHWAY:
                return 1.3011416847239474E7;
            case RESIDENTIAL:
            case BUILDING:
            case RESIDENTIAL_HIGHWAY:
            case MULTIPOLYGON:
            case FOOTWAY:
            case PATH:
            case TRACK:
            case CYCLEWAY:
                return 2.8945867784311756E7;
            default:
                return Double.POSITIVE_INFINITY;
        }
    }

    public static boolean getFill(OSMType OSMType) {
        switch (OSMType) {
            case NATURAL:
            case FARMLAND:
            case FOREST:
            case ROUTING:
            case WATER:
            case COASTLINE:
            case BUILDING:
            case MULTIPOLYGON:
            case RESIDENTIAL:
            case WOOD:
            case HEATH:
            case GREEN:
            case BEACH:
            case MEADOW:
                return true;
            default: //TODO FIX
                return false;
        }
    }


    public static Color getColor(OSMType OSMType, Boolean colorBlindMode) {
        if (!colorBlindMode) return getNormalColor(OSMType);
        else return getColorBlindColor(OSMType);
    }

    public static Color getNormalColor(OSMType OSMType) {
        switch (OSMType) {
            case BEACH:
                return Color.rgb(255, 241, 186, 1);
            case MEADOW:
                return Color.rgb(205, 235, 176, 1);
            case COASTLINE:
                return Color.rgb(242, 239, 233, 1);
            case PLACE:
            case GREEN:
                return Color.LIGHTGREEN;
            case WATER:
                return Color.rgb(170, 211, 223, 1);
            case BUILDING:
                return Color.rgb(187, 170, 157, 1);
            case MULTIPOLYGON:
                return Color.BROWN;
            case RESIDENTIAL_HIGHWAY:
                return Color.rgb(92, 92, 92, 1);
            case TERTIARY:
            case MOTORWAY:
                return Color.BLACK;
            case HIGHWAY:
                return Color.rgb(54, 54, 54, 1);
            case UNCLASSIFIED_HIGHWAY:
                return Color.rgb(79, 79, 79, 1);
            case HEATH:
                return Color.rgb(214, 217, 159, 0.9);
            case RESIDENTIAL:
                return Color.rgb(218, 218, 218, 0.9);
            case FARMLAND:
                return Color.rgb(238, 240, 213, 0.9);
            case WOOD:
            case FOREST:
                return Color.rgb(157, 202, 138, 0.9);
            case TREE_ROW:
                return Color.rgb(0, 102, 0, 0.7);
            case PARKING:
                return Color.ROYALBLUE;
            case FOOTWAY:
            case PATH:
            case TRACK:
            case CYCLEWAY:
                return Color.YELLOW;
            case NATURAL:
                return Color.BLUEVIOLET;
            case ROUTING:
                return Color.RED;
            default: //TODO FIX
                return Color.TRANSPARENT;
        }

    }

    public static Color getColorBlindColor(OSMType OSMType) {
        switch (OSMType) {
            case PLACE:
            case COASTLINE:
            case GREEN:
                return Color.PINK;
            case WATER:
                return Color.RED;
            case BUILDING:
            case MULTIPOLYGON:
                return Color.YELLOW;
            case HIGHWAY:
                return Color.BURLYWOOD;
            case HEATH:
                return Color.rgb(255, 178, 102, 0.6);
            case RESIDENTIAL:
                return Color.rgb(128, 128, 128, 0.6);
            case FARMLAND:
                return Color.rgb(238, 240, 213, 0.6);
            case WOOD:
            case FOREST:
            case TREE_ROW:
                return Color.rgb(0, 102, 0, 0.7);
            case PARKING:
                return Color.RED;
            case NATURAL:
                return Color.BLUEVIOLET;
            default: //TODO FIX
                return Color.TRANSPARENT;
        }

    }

    public static int getMaxSpeed(OSMType OSMType) {
        switch (OSMType) {
            case RESIDENTIAL_HIGHWAY:
                return 50;
            case MOTORWAY:
                return 110;
            case TERTIARY:
                return 80;
            case UNCLASSIFIED_HIGHWAY:
                return 70;
            case TRACK:
                return 20;
            default:
                return 60;
        }
    }

}