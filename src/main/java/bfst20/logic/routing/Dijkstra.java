package bfst20.logic.routing;

import bfst20.logic.entities.Node;

import java.util.HashMap;
import java.util.Map;

public class Dijkstra {
    private Map<Node, Double> distTo;
    private Map<Node, Edge> edgeTo;
    private MinPQ<Node> pq;


    public Dijkstra(Graph graph, Node root, Node target) {
        pq = new MinPQ<>(graph.nodeCount());
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        findShortestPath(graph, root, target);
    }

    public void findShortestPath(Graph graph, Node root, Node target) {
        for (int i = 0; i < graph.getNodes().size(); i++) {
            distTo.put(graph.getNodes().get(i), Double.POSITIVE_INFINITY);
        }
        root.setDistTo(0.0);
        distTo.put(root, 0.0);

        pq.insert(root);
        while (!pq.isEmpty()) {
            Node min = pq.delMin();

            if (min.getId() == target.getId()) return;

            for (Edge e : graph.adj(min)) {
                relax(e, min, graph);
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

    private void relax(Edge edge, Node min, Graph graph) {
         // Current node can be target or source for current edge because the graph is not directed
        Node current = edge.getTarget() == min ? edge.getSource() : edge.getTarget();

        if (distTo.get(current) > distTo.get(min) + edge.getLength()) {
            distTo.put(current, distTo.get(min) + edge.getLength());
            edgeTo.put(current, edge);
            current.setDistTo(distTo.get(current));

            pq.insert(current);
        }
    }
}
