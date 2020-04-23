package bfst20.logic;

import bfst20.logic.entities.LinePath;
import bfst20.logic.kdtree.Direction;
import bfst20.logic.kdtree.Rect;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
}
