package bfst20.logic.routing;

import bfst20.logic.Type;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;

public class Edge {
    private double speedLimit;
    private LinePath linePath;
    private Type highwayType;
    private double length;
    private Node source;
    private Node target;



    public Edge(Type highwayType, Node source, Node target, double length, LinePath linePath) {
        this.highwayType = highwayType;
        this.linePath = linePath;
        this.source = source;
        this.target = target;
        this.length = length;
    }

    public Edge(Type highwayType, Node source, Node target, double length, LinePath linePath, int speedLimit) {
        this.highwayType = highwayType;
        this.speedLimit = speedLimit;
        this.linePath = linePath;
        this.source = source;
        this.target = target;
        this.length = length;
    }




    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public LinePath getLinePath() {
        return linePath;
    }

    public double getLength() {
        return length;
    }

}
