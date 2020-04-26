package bfst20.data;

import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.routing.Edge;
import bfst20.logic.routing.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutingData {
    private static RoutingData routingData;
    private static boolean isLoaded = false;
    private List<Edge> route;
    private Graph graph;
    private Map<Node, Edge> edgesOnPath;

    private RoutingData() {
        edgesOnPath = new HashMap<>();

    }

    public static RoutingData getInstance() {
        if(!isLoaded) {
            isLoaded = true;
            routingData = new RoutingData();
        }
        return routingData;
    }

    public void saveGraph(Graph graph) {
        this.graph = graph;
    }

    public void saveRoute(List<Edge> route) {
        this.route = route;
        System.out.println("HEY");
    }

    public List<Edge> getRoute() {
        return route;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setEdgesOnPath(Map<Node, Edge> edgesOnPath) {
        this.edgesOnPath = edgesOnPath;
        System.out.println("HEY");
    }

    public Map<Node, Edge> getEdgesOnPath() {
        return edgesOnPath;
    }
}
