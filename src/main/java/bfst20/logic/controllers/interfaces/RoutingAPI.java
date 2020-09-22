package bfst20.logic.controllers.interfaces;

import bfst20.data.OSMElementData;
import bfst20.logic.entities.LinePath;
import bfst20.logic.misc.Vehicle;
import bfst20.logic.routing.Edge;
import bfst20.logic.routing.Graph;

import java.util.List;
import java.util.Map;

public interface RoutingAPI {

    void buildRoutingGraph(List<LinePath> highways);

    void saveGraph(Graph graph);

    Graph fetchGraph();

    double initializeRouting(String sourceQuery, String targetQuery, Vehicle vehicle);

    List<Edge> fetchRouteData();

    Map<String, Double> fetchRouteDirections();

    void clearRouteInfoData();


}
