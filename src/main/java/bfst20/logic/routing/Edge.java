package bfst20.logic.routing;

import bfst20.logic.misc.OSMType;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.misc.Vehicle;

public class Edge {
    private OSMType highwayType;
    private LinePath linePath;
    private boolean isOneWay;
    private double length;
    private int maxSpeed;
    private Node source;
    private Node target;
    private String name;


    public Edge(OSMType highwayType, Node source, Node target, double length, LinePath linePath, String name, int maxSpeed, boolean isOneWay) {
        this.maxSpeed = maxSpeed != 0 ? maxSpeed : OSMType.getMaxSpeed(highwayType);
        this.highwayType = highwayType;
        this.linePath = linePath;
        this.isOneWay = isOneWay;
        this.source = source;
        this.target = target;
        this.length = length;

        this.name = name == null ? "ååååå" : name;
    }

    public boolean isOneWay(Vehicle vehicle) {
        if (vehicle == Vehicle.CAR) return isOneWay;

        return false;
    }

    public boolean isVehicleAllowed(Vehicle vehicle) {
        if (vehicle == Vehicle.BICYCLE && highwayType == OSMType.MOTORWAY || vehicle == Vehicle.WALK && highwayType == OSMType.MOTORWAY) {
            return false;
        } else if (vehicle == Vehicle.CAR && highwayType == OSMType.FOOTWAY || vehicle == Vehicle.CAR && highwayType == OSMType.PATH) {
            return false;
        }

        return true;
    }

    public String getName() {
        return name;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public LinePath getLinePath() {
        return linePath;
    }

    public double getLength() {
        return length;
    }

}
