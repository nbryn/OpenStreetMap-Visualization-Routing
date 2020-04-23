package bfst20.logic.routing;

import bfst20.logic.entities.Node;
import bfst20.logic.misc.Vehicle;

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
        setup(graph, source);

        while (!pq.isEmpty()) {
            Node min = pq.delMin();

            if (min.getId() == target.getId()) return;

            for (Edge edge : graph.adj(min)) {
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

    public Map<Node, Edge> getEdgeTo() {
        return edgeTo;
    }

    public double distTo(Node node) {
        return distTo.get(node);
    }

    public boolean hasPathTo(Node node) {
        return distTo.get(node) < Double.POSITIVE_INFINITY;
    }

    private void relax(Edge edge, Node min, Vehicle vehicle) {
        // Current node can be target or source for current edge because the graph is not directed
        Node current;
        if (min == edge.getSource()) {
            current = edge.getTarget();
            if (edge.isVehicleAllowed(vehicle)) {
                setDistTo(edge, min, current);
            }
        } else {
            current = edge.getSource();
            if (!edge.isOneWay(vehicle)) {
                if (edge.isVehicleAllowed(vehicle)) {

                    setDistTo(edge, min, current);
                }
            }
        }
    }

    private void setDistTo(Edge edge, Node min, Node current) {
        if (distTo.get(current) > distTo.get(min) + edge.getLength()) {
            distTo.put(current, distTo.get(min) + edge.getLength());
            edgeTo.put(current, edge);
            current.setDistTo(distTo.get(current));

            pq.insert(current);
        }
    }
}
