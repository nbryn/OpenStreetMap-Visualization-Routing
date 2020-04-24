package bfst20.logic.routing;

import bfst20.logic.AppController;
import bfst20.logic.misc.OSMType;
import bfst20.logic.entities.Address;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.misc.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RoutingController {
    private static RoutingController routingController;
    private static boolean isLoaded = false;
    private AppController appController;
    private Dijkstra dijkstra;

    private RoutingController() {
        appController = new AppController();

    }

    public static RoutingController getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            routingController = new RoutingController();
        }

        return routingController;
    }

    public void buildRoutingGraph() {
        List<LinePath> highWays = appController.getHighwaysFromModel();
        Map<Long, Node> nodes = appController.getNodesFromModel();
        Graph graph = new Graph(new ArrayList<>(nodes.values()));

        generateGraphEdges(highWays, nodes, graph);
        appController.addToModel(graph);
    }

    public double calculateShortestRoute(Graph graph, Node source, Node target, Vehicle vehicle) {
        dijkstra = new Dijkstra(graph, source, target, vehicle);

        if (dijkstra.distTo(target) != Double.POSITIVE_INFINITY) {

            List<LinePath> route = extractEdgesOnRoute(dijkstra.getEdgeTo(), source, target);
            appController.setRouteOnModel(route);
        }

        return dijkstra.distTo(target);
    }

    public Node findClosestNode(Address address, List<Edge> edges) {
        List<Edge> closestEdges = new ArrayList<>();
        findClosestEdges(address, edges, closestEdges);

        Node closestNode = calculateDistanceBetween(address, closestEdges);

        return closestNode;
    }

    private Node calculateDistanceBetween(Address address, List<Edge> closestEdges) {
        float shortestDistance = Float.POSITIVE_INFINITY;
        Node closestNode = null;

        for (Edge e : closestEdges) {
            float distance = (float) Math.sqrt(Math.pow(e.getTarget().getLatitude() - address.getLat(), 2) + Math.pow(e.getTarget().getLongitude() - address.getLon(), 2));

            if (distance < shortestDistance) {
                closestNode = e.getTarget();
                shortestDistance = distance;
            }
        }

        return closestNode;
    }

    private void findClosestEdges(Address address, List<Edge> edges, List<Edge> closestEdges) {
        int addressIndex = binarySearch(edges, address.getStreet());

        int val = 100;

        for (int i = addressIndex - val; i < addressIndex + val; i++) {
            if (edges.get(i).getName().equals(address.getStreet())) {
                closestEdges.add(edges.get(i));
            }
        }
    }

    private void generateGraphEdges(List<LinePath> highWays, Map<Long, Node> nodes, Graph graph) {
        for (LinePath linePath : highWays) {
            Way way = linePath.getWay();
            OSMType OSMType = linePath.getOSMType();

            for (int i = 1; i < way.getNodeIds().size(); i++) {
                Node sourceNode = nodes.get(way.getNodeIds().get(i - 1));
                Node targetNode = nodes.get(way.getNodeIds().get(i));

                LinePath edgeLinePath = new LinePath(sourceNode, targetNode, OSMType.ROUTING, OSMType.getFill(OSMType.BOUNDS));

                if (sourceNode != null && targetNode != null) {
                    double length = calculateDistBetween(sourceNode, targetNode);

                    Edge edge = new Edge(OSMType, sourceNode, targetNode, length, edgeLinePath, way.getName(), way.getMaxSpeed(), way.isOneWay());

                    graph.addEdge(edge);
                }
            }
        }
    }

    private List<LinePath> extractEdgesOnRoute(Map<Node, Edge> edgesFromDijkstra, Node source, Node target) {
        List<LinePath> linePaths = new ArrayList<>();
        Edge edge = edgesFromDijkstra.get(target);
        long id = 0;

        while (id != source.getId()) {
            edge = edgesFromDijkstra.get(edge.getTarget());
            long tempID = edge.getTarget().getId();

            // Follow either target or source node
            edge = id == tempID ? edgesFromDijkstra.get(edge.getTarget()) : edgesFromDijkstra.get(edge.getSource());
            id = id == tempID ? edge.getSource().getId() : edge.getTarget().getId();

            linePaths.add(edge.getLinePath());
        }

        return linePaths;
    }

    private int binarySearch(List<Edge> list, String address) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Edge midElement = list.get(mid);
            String midID = midElement.getName();

            if (midID.compareTo(address) < 0) {
                low = mid + 1;
            } else if (midID.compareTo(address) > 0) {
                high = mid - 1;
            } else {
                return low;
            }
        }

        return 0;
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
