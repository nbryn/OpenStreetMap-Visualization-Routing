package bfst20.logic.routing;

import bfst20.logic.AppController;
import bfst20.logic.Type;
import bfst20.logic.entities.Address;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoutingController {

    private static RoutingController routingController;
    private static boolean isLoaded = false;
    private Map<Long, Node> nodesInGraph;
    private AppController appController;
    private Dijkstra dijkstra;
    private Graph graph;
    private List<Edge> edges;


    private RoutingController() {
        appController = new AppController();
        edges = new ArrayList<>();
        nodesInGraph = new HashMap();

    }

    public static RoutingController getInstance() {
        if (!isLoaded) {
            routingController = new RoutingController();
        }

        return routingController;
    }

    public void initialize(List<LinePath> highWays, Map<Long, Node> nodes) {
        generateEdges(highWays, nodes);
        buildGraph(nodes);
        queryDijkstra(nodes);
    }

    private void queryDijkstra(Map<Long, Node> nodes) {
        String sourceQuery = "Kildemosen 2";
        String targetQuery = "Kildemosen 3";

        Address sourceAddress = appController.findAddress(sourceQuery);
        Address targetAddress = appController.findAddress(targetQuery);
/*
        System.out.println(sourceAddress);
        System.out.println(targetAddress);
*/

        if (sourceAddress != null && targetAddress != null) {
            long sourceNodeID = sourceAddress.getNodeID();
            long targetNodeID = targetAddress.getNodeID();

            /*System.out.println("SourceID is: " + sourceNodeID);
            System.out.println("TargetID is: " + targetNodeID);

            Node source = nodes.get(sourceNodeID);
            Node target = nodes.get(targetNodeID)*/;

            System.out.println(nodes.size());

            Node source = appController.getNodeFromModel(463745263);
            Node target = appController.getNodeFromModel(454644902);

            System.out.println("Source is :" + source);
            System.out.println("Target is :" + target);

            dijkstra = new Dijkstra(graph, source);

            Map<Node, Double> distTo = dijkstra.getDistTo();



            System.out.println(dijkstra.hasPathTo(target));
            System.out.println(dijkstra.distTo(target));


        }



    }

    private void generateEdges(List<LinePath> highWays, Map<Long, Node> nodes) {
        for (LinePath linePath : highWays) {
            Way way = linePath.getWay();
            Type type = linePath.getType();

            for (int i = 1; i < way.getNodeIds().size(); i++) {
                Node sourceNode = nodes.get(way.getNodeIds().get(i - 1));
                Node targetNode = nodes.get(way.getNodeIds().get(i));

                nodesInGraph.put(sourceNode.getId(), sourceNode);
                nodesInGraph.put(targetNode.getId(), targetNode);

                if (sourceNode != null && targetNode != null) {
                    double length = calculateDistBetween(sourceNode, targetNode);
                    Edge edge = new Edge(type, sourceNode, targetNode, length);

                    edges.add(edge);
                }

            }
        }
    }

    private void buildGraph(Map<Long, Node> nodes) {
        graph = new Graph(new ArrayList<>(nodes.values()));



        for (Edge edge : edges) {
            /*System.out.println(edge);*/
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
