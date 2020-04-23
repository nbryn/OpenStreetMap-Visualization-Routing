package bfst20.data;

import bfst20.logic.misc.OSMType;
import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class LinePathDataTest {

    static LinePathData linePathData;
    static Node node;

    @BeforeAll
    static void setup() {
        linePathData = LinePathData.getInstance();
        node = new Node();

    }

    @Test
    void getInstance() {
        assertEquals(linePathData, LinePathData.getInstance());
    }

    @Test
    void getLinePaths() {
        LinePath linePath = new LinePath(new Way(), OSMType.COASTLINE, new HashMap<>(), new HashMap<>(), true);

        linePathData.addLinePath(OSMType.COASTLINE, linePath);

        assertEquals(linePath, linePathData.getLinePaths().get(OSMType.COASTLINE).get(0));
    }

    @Test
    void addLinePath() {
        LinePath linePath = new LinePath(new Way(), OSMType.HIGHWAY, new HashMap<>(), new HashMap<>(), true);

        linePathData.addLinePath(OSMType.HIGHWAY, linePath);

        assertEquals(linePath, linePathData.getLinePaths().get(OSMType.HIGHWAY).get(0));
    }

    @Test
    void setLinePaths() {
        LinePath linePath = new LinePath(new Way(), OSMType.MOTORWAY, new HashMap<>(), new HashMap<>(), true);
        List<LinePath> motorWays = new ArrayList<>();
        motorWays.add(linePath);

        Map<OSMType, List<LinePath>> linePaths = new HashMap<>();
        linePaths.put(OSMType.MOTORWAY, motorWays);
        linePathData.setLinePaths(linePaths);

        assertEquals(linePath, linePathData.getLinePaths().get(OSMType.MOTORWAY).get(0));
    }

    @Test
    void addType() {
        linePathData.addType(OSMType.FOREST);

        assertNotNull(linePathData.getLinePaths().get(OSMType.FOREST));
    }

    @Test
    void addNodeToForest() {
        linePathData.addNodeToForest(node, new Way());

        assertEquals(1, linePathData.getNodeToForest().size());
    }

    @Test
    void addToNodeToFarmland() {
        linePathData.addToNodeToFarmland(node, new Way());

        assertEquals(1, linePathData.getNodeToFarmland().size());
    }

    @Test
    void addToNodeToCoastline() {
        linePathData.addToNodeToCoastline(node, new Way());

        assertEquals(1, linePathData.getNodeToCoastline().size());
    }


    @Test
    void getNodeToCoastline() {
        linePathData.addToNodeToCoastline(node, new Way());

        assertEquals(1, linePathData.getNodeToCoastline().size());
    }

    @Test
    void getNodeToForest() {
        linePathData.addNodeToForest(node, new Way());

        assertEquals(1, linePathData.getNodeToForest().size());
    }

    @Test
    void getNodeToFarmland() {
        linePathData.addToNodeToFarmland(node, new Way());

        assertEquals(1, linePathData.getNodeToFarmland().size());
    }

    @Test
    void removeWayFromNodeToForest() {
        linePathData.removeWayFromNodeToForest(node);

        assertEquals(0, linePathData.getNodeToForest().size());
    }

    @Test
    void removeWayFromNodeToFarmland() {
        linePathData.removeWayFromNodeToFarmland(node);

        assertEquals(0, linePathData.getNodeToFarmland().size());
    }

    @Test
    void removeWayFromNodeToCoastline() {
        linePathData.removeWayFromNodeToCoastline(node);

        assertEquals(0, linePathData.getNodeToCoastline().size());
    }
}