package bfst20.logic.controllers.interfaces;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.misc.OSMType;

import java.util.List;
import java.util.Map;

public interface KDTreeAPI {

    void generateKDTrees(Map<OSMType, List<LinePath>> linePaths, Bounds bounds);

    void saveRect(Bounds bounds);

    void saveKDTree(OSMType type, List<LinePath> linePaths);

    Rect fetchRectData();

    KDTree fetchKDTreeByType(OSMType Type);

    void saveAllKDTrees(Map<OSMType, KDTree> trees);

    Map<OSMType, KDTree> fetchAllKDTrees();
}
