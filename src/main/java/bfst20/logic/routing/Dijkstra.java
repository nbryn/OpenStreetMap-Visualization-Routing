package bfst20.logic.routing;

import bfst20.logic.entities.Node;
import bfst20.logic.misc.Vehicle;

import javax.xml.stream.events.StartDocument;
import java.util.HashMap;
import java.util.Map;

public class Dijkstra {
    private Map<Node, Double> distTo;
    private Map<Node, Edge> edgeTo;
    private MinPQ<Node> pq;


    public Dijkstra(Graph graph, Node source, Node target, Vehicle vehicle) {
        pq = new MinPQ<>(graph.nodeCount());
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        findShortestPath(graph, source, target, vehicle);
    }


    public void findShortestPath(Graph graph, Node source, Node target, Vehicle vehicle) {
        System.out.println(source.getId());
        System.out.println(target.getId());
        setup(graph, source);

        while (!pq.isEmpty()) {
            Node min = pq.delMin();

            for (Edge edge : graph.adj(min)) {
                if (min.getId() == target.getId()) {
                    // Source and Target have same ID
                    if (edgeTo.size() == 0) {
                        edgeTo.put(target, edge);
                        return;
                    }
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
        pq.insert(source);
    }


    private void relax(Edge edge, Node min, Vehicle vehicle) {
        // Current node can be target or source for current edge because the graph is not directed

        Node current;
        if (min == edge.getSource()) {
            current = edge.getTarget();
            // Only need to check oneway when coming from source
            // As we know it's not oneway when coming from target
            if (!edge.isOneWay(vehicle)) {
                vehicleAllowed(edge, min, vehicle, current);
            }
        } else {
            current = edge.getSource();
            vehicleAllowed(edge, min, vehicle, current);
        }
    }

    private void vehicleAllowed(Edge edge, Node min, Vehicle vehicle, Node current) {
        if (edge.isVehicleAllowed(vehicle)) {
            if (vehicle == Vehicle.CAR) {
                setDistToCar(edge, min, current);
            } else {
                setDistTo(edge, min, current);
            }
        }
    }

    private void setDistToCar(Edge edge, Node min, Node current) {
        if (distTo.get(current) > distTo.get(min) + (edge.getLength() / edge.getMaxSpeed())) {
            distTo.put(current, distTo.get(min) + (edge.getLength() / edge.getMaxSpeed()));
            insertNodePQ(edge, current);
        }
    }


    private void setDistTo(Edge edge, Node min, Node current) {
        if (distTo.get(current) > distTo.get(min) + edge.getLength()) {
            distTo.put(current, distTo.get(min) + edge.getLength());
            insertNodePQ(edge, current);
        }
    }


    private void insertNodePQ(Edge edge, Node current) {
        edgeTo.put(current, edge);
        current.setDistTo(distTo.get(current));

        pq.insert(current);
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
        pq = null;
        System.gc();
    }

}
