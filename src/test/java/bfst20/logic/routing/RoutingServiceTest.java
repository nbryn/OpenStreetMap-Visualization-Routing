package bfst20.logic.routing;

import bfst20.logic.controllers.KDTreeController;
import bfst20.logic.entities.Address;
import bfst20.logic.entities.Node;
import bfst20.logic.misc.OSMType;
import bfst20.logic.misc.Vehicle;
import bfst20.logic.services.RoutingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RoutingServiceTest {
    private static RoutingService routingService;
    private static KDTreeController.StartupController startupController;
    private static Graph graph;
    private static List<Node> nodes = new ArrayList<>();
    private static List<Edge> edges = new ArrayList<>();
    private static Node node1 = new Node(26, 2, 3);
    private static Node node2 = new Node(27, 4, 5);
    private static Edge edge1 = new Edge(OSMType.PRIMARY, node1, node2, 3, "Gedevasevej", 75, false);
    private static Edge edge2 = new Edge(OSMType.PRIMARY, node2, node1, 5, "Dyrevænget", 75, false);
    Address address = new Address("Farum", "21", "3520", "Gedevasevej", 1, 2);
    Address address2 = new Address("Farum", "21", "3520", "Dyrevænget", 3, 4);

    @BeforeAll
    static void setup() {
        startupController = mock(KDTreeController.StartupController.class);
        routingService = new RoutingService(startupController);
        Collections.addAll(nodes, node1, node2);
        Collections.addAll(edges, edge1, edge2);
        graph = new Graph(nodes);
        graph.addEdge(edge1);
        graph.addEdge(edge2);


    }

    @Test
    void buildRoutingGraph() {
        routingService.buildRoutingGraph();

        verify(startupController, times(1)).saveGraphData(Mockito.any(Graph.class));
    }

    @Test
    void calculateShortestRoute() {
        routingService.calculateShortestRoute(graph, edges, address, address2, Vehicle.CAR);

        verify(startupController, times(1)).saveRouteData(Mockito.any(ArrayList.class));

        assertEquals(0.04, routingService.calculateShortestRoute(graph, edges, address, address2, Vehicle.CAR));
        assertEquals(3, routingService.calculateShortestRoute(graph, edges, address, address2, Vehicle.BICYCLE));
    }
}