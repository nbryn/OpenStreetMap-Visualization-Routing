package bfst20.logic.routing;

import bfst20.logic.entities.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dijkstra {
    private Map<Node, Double> distTo;
    private Map<Node, Edge> edgeTo;
    private List<Edge> edges;
    private MinPQ<Node> pq;

    public Dijkstra(Graph graph, Node root, Node target) {
        edges = new ArrayList<>();
        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        initialize(graph, root, target);
    }

    public void initialize(Graph graph, Node root, Node target) {
        System.out.println(graph.getNodes().size());
        for (int i = 0; i < graph.getNodes().size(); i++) {
            distTo.put(graph.getNodes().get(i), Double.POSITIVE_INFINITY);
        }
        root.setDistTo(0.0);
        distTo.put(root, 0.0);

        pq = new MinPQ<>(graph.nodeCount());
        pq.insert(root);

        while (!pq.isEmpty()) {
            Node min = pq.delMin();
            if (min.getId() == target.getId()) {
                return;
            }
            for (Edge e : graph.adj(min)) {
                relax(e);
            }
        }
    }

    public List<Edge> getEdgesOnPath() {
        return edges;
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

    private void relax(Edge edge) {
        Node source = edge.getSource();
        Node target = edge.getTarget();
        if (distTo.get(target) > distTo.get(source) + edge.getLength()) {
            distTo.put(target, distTo.get(source) + edge.getLength());
            edgeTo.put(target, edge);

            target.setDistTo(distTo.get(target));
            pq.insert(target);
        }
    }
}
