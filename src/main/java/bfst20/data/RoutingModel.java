package bfst20.data;

import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.routing.Edge;
import bfst20.logic.routing.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutingModel {
    private static RoutingModel routingModel;
    private static boolean isLoaded = false;
    private List<LinePath> route;
    private Graph graph;
    private Map<Node, Edge> edgesOnPath;

    private RoutingModel() {
        edgesOnPath = new HashMap<>();

    }

    public static RoutingModel getInstance() {
        if(!isLoaded) {
            isLoaded = true;
            routingModel = new RoutingModel();
        }
        return routingModel;
    }

    public void saveGraph(Graph graph) {
        this.graph = graph;
    }

    public void saveRoute(List<LinePath> route) {
        this.route = route;
    }

    public List<LinePath> getRoute() {
        return route;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setEdgesOnPath(Map<Node, Edge> edgesOnPath) {
        this.edgesOnPath = edgesOnPath;
    }

    public Map<Node, Edge> getEdgesOnPath() {
        return edgesOnPath;
    }
}
