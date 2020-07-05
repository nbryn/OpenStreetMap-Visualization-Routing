package bfst20.logic.routing;

import bfst20.logic.entities.Node;
import bfst20.logic.misc.Vehicle;

import java.util.AbstractQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {
    private AbstractQueue<Node> minPQ;
    private Map<Node, Double> distTo;
    private Map<Node, Edge> edgeTo;

    public Dijkstra(Graph graph, Node source, Node target, Vehicle vehicle) {
        minPQ = new PriorityQueue<>(graph.nodeCount());
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        findShortestPath(graph, source, target, vehicle);
    }

    public void findShortestPath(Graph graph, Node source, Node target, Vehicle vehicle) {
        setup(graph, source);

        while (!minPQ.isEmpty()) {
            Node min = minPQ.poll();

            for (Edge edge : graph.adj(min)) {
                if (min.getId() == target.getId()) {
                    if (edgeTo.size() == 0) edgeTo.put(target, edge);

                    return;
                }

                relax(edge, min, vehicle);
            }
        }
    }

    private void setup(Graph graph, Node source) {
        for (int i = 0; i < graph.getNodes().size(); i++) {
            distTo.put(graph.getNodes().get(i), Double.POSITIVE_INFINITY);
        }
        source.setDistTo(0.0);
        distTo.put(source, 0.0);
        minPQ.add(source);
    }

    private void relax(Edge edge, Node min, Vehicle vehicle) {
        Node current;

        if (min == edge.getSource()) current = edge.getTarget();
        else current = !edge.isOneWay(vehicle) ? edge.getSource() : edge.getTarget();

        vehicleAllowed(edge, min, vehicle, current);
    }

    private void vehicleAllowed(Edge edge, Node min, Vehicle vehicle, Node current) {
        if (edge.isVehicleAllowed(vehicle)) {
            double distance;
            if (vehicle == Vehicle.CAR) distance = distTo.get(min) + (edge.getLength() / edge.getMaxSpeed());
            else distance = distTo.get(min) + edge.getLength();
            if (distTo.get(current) > distance) {
                distTo.put(current, distance);
                edgeTo.put(current, edge);
                current.setDistTo(distTo.get(current));

                minPQ.add(current);
            }
        }
    }

    public Map<Node, Edge> getEdgeTo() {
        return edgeTo;
    }

    public double distTo(Node node) {
        return distTo.get(node);
    }

    public boolean hasPathTo(Node node) {
        return distTo.get(node) < Double.POSITIVE_INFINITY;
    }

    public void clearData() {
        distTo = null;
        edgeTo = null;
        minPQ = null;
        System.gc();
    }
}
