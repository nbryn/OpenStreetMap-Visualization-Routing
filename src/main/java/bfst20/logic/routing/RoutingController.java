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
    private static boolean isLoaded = false;
    private AppController appController;
    private Dijkstra dijkstra;
    private List<Edge> edges;

    private RoutingController() {
        appController = new AppController();
        edges = new ArrayList<>();

    }

    public static RoutingController getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            routingController = new RoutingController();
        }
        return routingController;
    }

    public void initialize(List<LinePath> highWays, Map<Long, Node> nodes) {
        generateEdges(highWays, nodes);
        buildGraph(nodes);
    }

    public double calculateShortestRoute(Graph graph, Node source, Node target) {
        dijkstra = new Dijkstra(graph, source, target);
        appController.setPathEdgesOnModel(dijkstra.getEdgeTo());

        if (dijkstra.distTo(target) != Double.POSITIVE_INFINITY) {

            List<LinePath> route = generateRoute(dijkstra.getEdgeTo(), source, target);
            appController.setRouteOnModel(route);

        }
        return dijkstra.distTo(target);
    }

    private List<LinePath> generateRoute(Map<Node, Edge> edgesOnPath, Node source, Node target) {
        long id = 0;
        List<LinePath> linePaths = new ArrayList<>();
        Edge edge = edgesOnPath.get(target);

        while (id != source.getId()) {
            edge = edgesOnPath.get(edge.getSource());
            linePaths.add(edge.getLinePath());
            id = edge.getSource().getId();
        }

        return linePaths;
    }

    private void generateEdges(List<LinePath> highWays, Map<Long, Node> nodes) {
        for (LinePath linePath : highWays) {
            Way way = linePath.getWay();
            Type type = linePath.getType();

            for (int i = 1; i < way.getNodeIds().size(); i++) {
                Node sourceNode = nodes.get(way.getNodeIds().get(i - 1));
                Node targetNode = nodes.get(way.getNodeIds().get(i));

                LinePath edgeLinePath = new LinePath(sourceNode, targetNode, Type.ROUTING, Type.getFill(Type.BOUNDS));

                if (sourceNode != null && targetNode != null) {
                    double length = calculateDistBetween(sourceNode, targetNode);
                    Edge edge = new Edge(type, sourceNode, targetNode, length, edgeLinePath);

                    edges.add(edge);
                }
            }
        }
    }

    private void buildGraph(Map<Long, Node> nodes) {
        Graph graph = new Graph(new ArrayList<>(nodes.values()));

        for (Edge edge : edges) {
            graph.addEdge(edge);
        }

        appController.saveGraphOnModel(graph);
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
