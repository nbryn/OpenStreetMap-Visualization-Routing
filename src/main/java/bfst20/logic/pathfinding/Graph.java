package bfst20.logic.pathfinding;

import bfst20.logic.entities.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

    private final int nodeCount;
    private int edgeCount;
    private Map<Node, List<Node>> adj;


    public Graph(List<Node> nodes) {
        this.nodeCount = nodes.size();
        this.edgeCount = 0;
        adj = new HashMap<>();
        for (int i = 0; i < nodeCount; i++) {
            adj.put(nodes.get(i), new ArrayList<>());
        }
    }


    public int V() {
        return nodeCount;
    }


    public int E() {
        return edgeCount;
    }


    public void addEdge(Node source, Node target) {
        edgeCount++;
        adj.get(source).add(target);
        adj.get(target).add(source);

    }


    public Iterable<Node> adj(Node node) {
        return adj.get(node);
    }


    public int degree(Node node) {
        return adj.get(node).size();
    }


}
