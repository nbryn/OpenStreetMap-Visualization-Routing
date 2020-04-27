package bfst20.data;

import bfst20.logic.routing.Edge;
import bfst20.logic.routing.Graph;

import java.util.List;

public class RoutingData {
    private static boolean isLoaded = false;
    private static RoutingData routingData;
    private List<Edge> route;
    private Graph graph;


    private RoutingData() {
    }

    public static RoutingData getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            routingData = new RoutingData();
        }
        return routingData;
    }

    //TODO: Two Graphs in Memory
    public void saveGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public void saveRoute(List<Edge> route) {
        this.route = route;
    }

    public List<Edge> getRoute() {
        return route;
    }
}
