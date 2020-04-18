package bfst20.logic.pathfinding;

import bfst20.logic.Type;
import bfst20.logic.entities.Node;

public class Edge implements Comparable {
    private final Node source;
    private final Node target;
    private final Type highwayType;
    private final double length;
    private double speedLimit;

    public Edge(Type highwayType, Node source, Node target, double length) {
        this.highwayType = highwayType;
        this.source = source;
        this.target = target;
        this.length = length;


    }

    public Edge(Type highwayType, Node source, Node target, double length, int speedLimit ) {
        this.highwayType = highwayType;
        this.source = source;
        this.target = target;
        this.length = length;
        this.speedLimit = speedLimit;

    }

    public double getLength() {
        return length;
    }

    @Override
    public int compareTo(Object other) {
        return Double.compare(this.length, ((Edge) other).getLength());
    }
}
