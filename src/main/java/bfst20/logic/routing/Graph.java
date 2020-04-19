package bfst20.logic.routing;

import bfst20.logic.entities.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private final int nodeCount;
    private List<Edge> edges;
    private Map<Node, List<Edge>> adj;
    private List<Node> nodes;



    public Graph(List<Node> nodes) {
        this.nodes = nodes;
        edges = new ArrayList<>();
        adj = new HashMap<>();
        this.nodeCount = nodes.size();

        for (int i = 0; i < nodeCount; i++) {
            adj.put(nodes.get(i), new ArrayList<>());
        }
    }

    public int nodeCount() {
        return nodeCount;
    }

    public int edgeCount() {
        return edges.size();
    }

    public Iterable<Edge> getEdges() {
        return edges;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
      /*  System.out.println("Source is: " + edge.getSource() );
        System.out.println(adj.get(edge.getSource()).size());*/
        adj.get(edge.getSource()).add(edge);


    }

    public Iterable<Edge> adj(Node node) {
        return adj.get(node);
    }

    public int degree(Node node) {
        return adj.get(node).size();
    }

}
