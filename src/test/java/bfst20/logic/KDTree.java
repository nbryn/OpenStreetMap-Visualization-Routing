package bfst20.logic;

import bfst20.logic.entities.LinePath;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.kdtree.Direction;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.misc.OSMType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KDTree {

    @Test
    void structureRoot(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(18, 16, 11, 13);
        linePaths.add(linePath);

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        assert kdTree.getRoot() != null;
    }

    @Test
    void structureRootDirection(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(18, 16, 11, 13);
        linePaths.add(linePath);

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        assertEquals(kdTree.getRoot().getDirection(), Direction.Latitudinal);
    }

    @Test
    void structureRootLeftChild(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(18, 16, 11, 13);
        LinePath linePath2 = new LinePath(18, 16, 11, 13);
        linePaths.add(linePath);
        linePaths.add(linePath2);

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        assert kdTree.getRoot().getLeftNode() != null;
        assertEquals(kdTree.getRoot().getLeftNode().getDirection(), Direction.Longitudinal);
    }

    @Test
    void structureRootRightChild(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(18, 16, 11, 13);
        LinePath linePath2 = new LinePath(19, 16, 12, 13);
        linePaths.add(linePath);
        linePaths.add(linePath2);

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        assert kdTree.getRoot().getRightNode() != null;
        assertEquals(kdTree.getRoot().getRightNode().getDirection(), Direction.Longitudinal);
    }

    @Test
    void structureRootLeftChildLon(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(18, 16, 11, 13);
        LinePath linePath2 = new LinePath(18, 16, 11, 13);
        LinePath linePath3 = new LinePath(18, 16, 11, 13);
        linePaths.add(linePath);
        linePaths.add(linePath2);
        linePaths.add(linePath3);

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        assert kdTree.getRoot().getLeftNode().getLeftNode() != null;
        assertEquals(kdTree.getRoot().getLeftNode().getLeftNode().getDirection(), Direction.Latitudinal);
    }

    @Test
    void structureRootRightChildLon(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(18, 16, 11, 13);
        LinePath linePath2 = new LinePath(18, 16, 11, 13);
        LinePath linePath3 = new LinePath(18, 12, 11, 10);
        linePaths.add(linePath);
        linePaths.add(linePath2);
        linePaths.add(linePath3);

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        assert kdTree.getRoot().getLeftNode().getRightNode() != null;
        assertEquals(kdTree.getRoot().getLeftNode().getRightNode().getDirection(), Direction.Latitudinal);
    }

    @Test
    void query(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();

        Map<Long, Node> nodes1 = new HashMap<>();
        nodes1.put((long) 1, new Node(1, 10, 10));
        nodes1.put((long) 2, new Node(2, 11, 10));
        Way way1 = new Way();
        way1.addNodeId(1);
        way1.addNodeId(2);

        linePaths.add(new LinePath(way1, OSMType.BUILDING, nodes1,true));

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        int i = 0;

        for(LinePath path : kdTree.getElementsInRect(new Rect(0, 100, 0, 100), 2.8945867784311756E8)){
            i++;
        }

        assertEquals(i, 1);
    }

    @Test
    void queryOut(){
        Rect rect = new Rect(10, 20, 10, 20);

        List<LinePath> linePaths = new ArrayList<>();

        Map<Long, Node> nodes1 = new HashMap<>();
        nodes1.put((long) 1, new Node(1, 10, 10));
        nodes1.put((long) 2, new Node(2, 11, 10));
        Way way1 = new Way();
        way1.addNodeId(1);
        way1.addNodeId(2);

        linePaths.add(new LinePath(way1, OSMType.BUILDING, nodes1,true));

        bfst20.logic.kdtree.KDTree kdTree = new bfst20.logic.kdtree.KDTree(linePaths, rect);

        int i = 0;

        for(LinePath path : kdTree.getElementsInRect(new Rect(0, 9, 0, 9), 2.8945867784311756E8)){
            i++;
        }

        assertEquals(i, 0);
    }
}
