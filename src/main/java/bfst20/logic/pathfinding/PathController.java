package bfst20.logic.pathfinding;

import bfst20.logic.AppController;
import bfst20.logic.Type;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PathController {

    private static PathController pathController;
    private AppController appController;
    private List<Edge> edges;
    private static boolean isLoaded = false;
    private List<Node> nodesInGraph;

    private PathController() {
        appController = new AppController();
        edges = new ArrayList<>();
        nodesInGraph = new ArrayList<>();

    }

    public static PathController getInstance() {
        if (!isLoaded) {
            pathController = new PathController();
        }

        return pathController;
    }

    public void generateEdges(List<LinePath> highWays, Map<Long, Node> nodes) {

        for (LinePath linePath : highWays) {
            Way way = linePath.getWay();
            Type type = linePath.getType();

            for (int i = 1; i < way.getNodeIds().size(); i++) {

                Node sourceNode = nodes.get(way.getNodeIds().get(i - 1));
                Node targetNode = nodes.get(way.getNodeIds().get(i));

                nodesInGraph.add(sourceNode);
                nodesInGraph.add(targetNode);

                if (sourceNode != null && targetNode != null) {

                    double length = Haversine.calculateDist(sourceNode.getLatitude(), sourceNode.getLongitude(), targetNode.getLatitude(), targetNode.getLongitude());

                    Edge edge = new Edge(type, sourceNode, targetNode, length);
                    edges.add(edge);
                }


                //Edge edge = new Edge()


            }


        }

    }

}
