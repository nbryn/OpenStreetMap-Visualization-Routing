package bfst20.data;

import bfst20.logic.Type;
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


class LinePathModelTest {

    static LinePathModel linePathModel;
    static Node node;

    @BeforeAll
    static void setup() {
        linePathModel = LinePathModel.getInstance();
        node = new Node();

    }

    @Test
    void getInstance() {
        assertEquals(linePathModel, LinePathModel.getInstance());
    }

    @Test
    void getLinePaths() {
        LinePath linePath = new LinePath(new Way(), Type.COASTLINE, new HashMap<>(), new HashMap<>(), true);

        linePathModel.addLinePath(Type.COASTLINE, linePath);

        assertEquals(linePath, linePathModel.getLinePaths().get(Type.COASTLINE).get(0));
    }

    @Test
    void addLinePath() {
        LinePath linePath = new LinePath(new Way(), Type.HIGHWAY, new HashMap<>(), new HashMap<>(), true);

        linePathModel.addLinePath(Type.HIGHWAY, linePath);

        assertEquals(linePath, linePathModel.getLinePaths().get(Type.HIGHWAY).get(0));
    }

    @Test
    void setLinePaths() {
        LinePath linePath = new LinePath(new Way(), Type.MOTORWAY, new HashMap<>(), new HashMap<>(), true);
        List<LinePath> motorWays = new ArrayList<>();
        motorWays.add(linePath);

        Map<Type, List<LinePath>> linePaths = new HashMap<>();
        linePaths.put(Type.MOTORWAY, motorWays);
        linePathModel.setLinePaths(linePaths);

        assertEquals(linePath, linePathModel.getLinePaths().get(Type.MOTORWAY).get(0));
    }

    @Test
    void addType() {
        linePathModel.addType(Type.FOREST);

        assertNotNull(linePathModel.getLinePaths().get(Type.FOREST));
    }

    @Test
    void addNodeToForest() {
        linePathModel.addNodeToForest(node, new Way());

        assertEquals(1, linePathModel.getNodeToForest().size());
    }

    @Test
    void addToNodeToFarmland() {
        linePathModel.addToNodeToFarmland(node, new Way());

        assertEquals(1, linePathModel.getNodeToFarmland().size());
    }

    @Test
    void addToNodeToCoastline() {
        linePathModel.addToNodeToCoastline(node, new Way());

        assertEquals(1, linePathModel.getNodeToCoastline().size());
    }


    @Test
    void getNodeToCoastline() {
        linePathModel.addToNodeToCoastline(node, new Way());

        assertEquals(1, linePathModel.getNodeToCoastline().size());
    }

    @Test
    void getNodeToForest() {
        linePathModel.addNodeToForest(node, new Way());

        assertEquals(1, linePathModel.getNodeToForest().size());
    }

    @Test
    void getNodeToFarmland() {
        linePathModel.addToNodeToFarmland(node, new Way());

        assertEquals(1, linePathModel.getNodeToFarmland().size());
    }

    @Test
    void removeWayFromNodeToForest() {
        linePathModel.removeWayFromNodeToForest(node);

        assertEquals(0, linePathModel.getNodeToForest().size());
    }

    @Test
    void removeWayFromNodeToFarmland() {
        linePathModel.removeWayFromNodeToFarmland(node);

        assertEquals(0, linePathModel.getNodeToFarmland().size());
    }

    @Test
    void removeWayFromNodeToCoastline() {
        linePathModel.removeWayFromNodeToCoastline(node);

        assertEquals(0, linePathModel.getNodeToCoastline().size());
    }
}