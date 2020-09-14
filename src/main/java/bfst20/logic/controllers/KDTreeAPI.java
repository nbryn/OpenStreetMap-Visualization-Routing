package bfst20.logic.controllers;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.misc.OSMType;

import java.util.List;

public interface KDTreeAPI {

    void saveRect(Bounds bounds);

    void saveKDTree(OSMType type, List<LinePath> linePaths);
    Rect fetchRectData();
    KDTree fetchKDTree(OSMType OSMType);
}
