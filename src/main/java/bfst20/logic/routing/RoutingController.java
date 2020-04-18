package bfst20.logic.routing;

import bfst20.logic.AppController;
import bfst20.logic.Type;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RoutingController {

    private static RoutingController routingController;
    private AppController appController;
    private List<Edge> edges;
    private static boolean isLoaded = false;
    private List<Node> nodesInGraph;
    private Graph graph;

    private RoutingController() {
        appController = new AppController();
        edges = new ArrayList<>();
        nodesInGraph = new ArrayList<>();

    }

    public static RoutingController getInstance() {
        if (!isLoaded) {
            routingController = new RoutingController();
        }

        return routingController;
    }

    public void initialize(List<LinePath> highWays, Map<Long, Node> nodes) {
        generateEdges(highWays, nodes);
        buildGraph();
    }

    private void generateEdges(List<LinePath> highWays, Map<Long, Node> nodes) {
        for (LinePath linePath : highWays) {
            Way way = linePath.getWay();
            Type type = linePath.getType();

            for (int i = 1; i < way.getNodeIds().size(); i++) {
                Node sourceNode = nodes.get(way.getNodeIds().get(i - 1));
                Node targetNode = nodes.get(way.getNodeIds().get(i));

                nodesInGraph.add(sourceNode);
                nodesInGraph.add(targetNode);

                if (sourceNode != null && targetNode != null) {
                    double length = calculateDistBetween(sourceNode, targetNode);
                    Edge edge = new Edge(type, sourceNode, targetNode, length);

                    edges.add(edge);
                }

            }
        }
    }

    private void buildGraph() {
        graph = new Graph(nodesInGraph);

        for (Edge edge : edges) {
            graph.addEdge(edge);
        }

    }

    private double calculateDistBetween(Node source, Node target) {
        double sourceLat = source.getLatitude();
        double sourceLong = source.getLongitude();
        double targetLat = target.getLatitude();
        double targetLong = target.getLongitude();

        double earthRadius = 6371.0;
        double dLat = Math.toRadians(targetLat - sourceLat);
        double dLng = Math.toRadians(targetLong - sourceLong);

        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(sourceLat)) * Math.cos(Math.toRadians(targetLat));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;

    }

}
