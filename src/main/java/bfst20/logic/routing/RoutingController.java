package bfst20.logic.routing;

import bfst20.logic.AppController;
import bfst20.logic.misc.OSMType;
import bfst20.logic.entities.Address;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.misc.Vehicle;

import java.util.*;

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
        List<LinePath> highways = appController.fetchHighwayData();
        List<Node> highwayNodes = new ArrayList<>();

        for (LinePath lp : highways) {
            if (lp.getWay() == null) continue;
            highwayNodes.addAll(lp.getWay().getNodes());
        }


        Graph graph = new Graph(new ArrayList<>(highwayNodes));
        generateGraphEdges(highways, graph);
        graph.sortEdges();

        appController.saveGraphData(graph);

        //TODO: Needed?
        highwayNodes = null;
        graph = null;
        System.gc();
    }

    private void generateGraphEdges(List<LinePath> highWays, Graph graph) {
        for (LinePath linePath : highWays) {
            Way way = linePath.getWay();
            OSMType type = way.getOSMType();

            if (way != null) {
                for (int i = 1; i < way.getNodes().size(); i++) {
                    Node sourceNode = way.getNodes().get(i - 1);
                    Node targetNode = way.getNodes().get(i);

                    double length = calculateDistBetween(sourceNode, targetNode);
                    Edge edge = new Edge(type, sourceNode, targetNode, length, way.getName(), way.getMaxSpeed(),
                            way.isOneWay());

                    graph.addEdge(edge);
                }
            }
        }
    }


    public double calculateShortestRoute(Graph graph, List<Edge> edges, Address srcAddress, Address trgAddress, Vehicle vehicle) {
        Node srcNode = findClosestNodeTo(srcAddress, edges, vehicle);
        Node trgNode = findClosestNodeTo(trgAddress, edges, vehicle);

        dijkstra = new Dijkstra(graph, srcNode, trgNode, vehicle);

        System.out.println("Has path to: " + dijkstra.hasPathTo(trgNode));

        if (dijkstra.distTo(trgNode) != Double.POSITIVE_INFINITY) {
            List<Edge> route = new ArrayList<>();

            if (dijkstra.getEdgeTo().size() == 1) route.addAll(dijkstra.getEdgeTo().values());
            else route.addAll(extractEdgesOnRoute(dijkstra.getEdgeTo(), srcNode, trgNode));

            Map<String, Double> routeInfo = extractRouteInfo(route);
            appController.saveRouteData(route);
            appController.saveRouteInfo(routeInfo);

            // TODO: Needed?
            route = null;
            routeInfo = null;
            System.gc();
        }
        System.out.println("Finished Routing");
        double dist = dijkstra.distTo(trgNode);
        dijkstra.clearData();
        return dist;
    }

    private Node findClosestNodeTo(Address address, List<Edge> edges, Vehicle vehicle) {
        List<Edge> closestEdges = new ArrayList<>();
        int addressIndex = binarySearch(edges, address.getStreet());
        int searchInterval = 5000;

        for (int i = addressIndex - searchInterval; i < addressIndex + searchInterval; i++) {
            if (i >= 0 && i < edges.size() && edges.get(i).getStreet().equals(address.getStreet())) {
                closestEdges.add(edges.get(i));
            }
        }
        Node closestNode = calculateDistanceBetween(address, closestEdges, vehicle);

        return closestNode;
    }


    private Node calculateDistanceBetween(Address address, List<Edge> closestEdges, Vehicle vehicle) {
        float shortestDistance = Float.POSITIVE_INFINITY;
        Node closestNode = null;

        for (Edge e : closestEdges) {

            float distance = (float) Math.sqrt(Math.pow(e.getTarget().getLatitude() - address.getLat(), 2)
                    + Math.pow(e.getTarget().getLongitude() - address.getLon(), 2));

            if (distance < shortestDistance && e.isVehicleAllowed(vehicle)) {
                closestNode = e.getTarget();
                shortestDistance = distance;
            }

        }

        return closestNode;
    }


    private List<Edge> extractEdgesOnRoute(Map<Node, Edge> edgesFromDijkstra, Node source, Node target) {
        List<Edge> edges = new ArrayList<>();
        Edge edge = edgesFromDijkstra.get(target);
        long id = 0;

        while (id != source.getId()) {
            edge = edgesFromDijkstra.get(edge.getTarget());
            long tempID = edge.getTarget().getId();


            // Follow either target or source node
            edge = id == tempID ? edgesFromDijkstra.get(edge.getTarget()) : edgesFromDijkstra.get(edge.getSource());
            id = id == tempID ? edge.getSource().getId() : edge.getTarget().getId();

            edges.add(edge);
        }

        return edges;
    }

    private Map<String, Double> extractRouteInfo(List<Edge> edges) {
        Map<String, Double> routeInfo = new LinkedHashMap<>();

        for (int i = edges.size() - 1; i >= 0; i--) {
            if (!routeInfo.containsKey(edges.get(i).getStreet())) {
                routeInfo.put(edges.get(i).getStreet(), (double) Math.round(edges.get(i).getLength() * 100) / 100);
            } else {
                routeInfo.put(edges.get(i).getStreet(), (double) Math.round((routeInfo.get(edges.get(i).getStreet()) + edges.get(i).getLength()) * 100) / 100);
            }
        }

        return routeInfo;
    }

    private int binarySearch(List<Edge> list, String address) {
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = (low + high) / 2;
            Edge midElement = list.get(mid);
            String midStreet = midElement.getStreet();

            if (midStreet.compareTo(address) < 0) {
                low = mid + 1;
            } else if (midStreet.compareTo(address) > 0) {
                high = mid - 1;
            } else {
                return low;
            }
        }

        return 0;
    }

    //TODO: Add explanation
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
        double a = Math.pow(sindLat, 2)
                + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(sourceLat)) * Math.cos(Math.toRadians(targetLat));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }
}

