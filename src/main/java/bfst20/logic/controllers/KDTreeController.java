package bfst20.logic.controllers;

import bfst20.data.KDTreeData;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.misc.OSMType;

import java.util.List;

public class KDTreeController implements KDTreeAPI {

    private KDTreeData kdTreeData;

    public KDTreeController(KDTreeData kdTreeData) {
        this.kdTreeData = kdTreeData;
    }

    @Override
    public void saveRect(Bounds bounds) {
        kdTreeData.saveRectValues(bounds.getMinLat(), bounds.getMaxLat(), bounds.getMinLon(), bounds.getMaxLon());
    }

    @Override
    public void saveKDTree(OSMType type, List<LinePath> linePaths) {
        if (type == OSMType.COASTLINE) return;
        kdTreeData.saveKDTree(type, new KDTree(linePaths, fetchRectData()));
    }

    @Override
    public Rect fetchRectData() {
        return kdTreeData.getRect();
    }

    @Override
    public KDTree fetchKDTree(OSMType OSMType) {
        return kdTreeData.getKDTree(OSMType);
    }

}
