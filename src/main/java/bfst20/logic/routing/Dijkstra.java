package bfst20.logic.routing;

import bfst20.logic.entities.Node;

import java.util.HashMap;
import java.util.Map;

public class Dijkstra {

    private Map<Node, Double> distTo;
    private Map<Node, Edge> edgeTo;
    private MinPQ<Node> pq;


    public Dijkstra(Graph graph, Node root) {

        distTo = new HashMap<>();
        edgeTo = new HashMap<>();

        for (int i = 0; i < graph.getNodes().size(); i++) {
            distTo.put(graph.getNodes().get(i), Double.POSITIVE_INFINITY);
        }
        root.setDistTo(0.0);
        distTo.put(root, 0.0);


        pq = new MinPQ<>(graph.nodeCount());
        pq.insert(root);
        while (!pq.isEmpty()) {
            try {
                Node min = pq.delMin();
                for (Edge e : graph.adj(min)) {
                    relax(e);
                }
            } catch (Exception es) {
                es.printStackTrace();
            }
        }
    }

    public Map<Node, Double> getDistTo() {
        return distTo;
    }

    private void relax(Edge edge) {
        Node source = edge.getSource();
        Node target = edge.getTarget();
        if (distTo.get(target) > distTo.get(source) + edge.getLength()) {
            distTo.put(target, distTo.get(source) + edge.getLength());
            edgeTo.put(target, edge);

            pq.insert(target);
        }
    }

    public double distTo(Node node) {
        return distTo.get(node);
    }

    public boolean hasPathTo(Node node) {
        return distTo.get(node) < Double.POSITIVE_INFINITY;
    }
}
