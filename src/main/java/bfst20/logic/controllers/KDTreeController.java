package bfst20.logic.controllers;

import bfst20.data.KDTreeData;
import bfst20.logic.controllers.interfaces.KDTreeAPI;
import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.LinePath;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import bfst20.logic.misc.OSMType;

import java.util.List;
import java.util.Map;

public class KDTreeController implements KDTreeAPI {

    private KDTreeData kdTreeData;

    public KDTreeController(KDTreeData kdTreeData) {
        this.kdTreeData = kdTreeData;
    }

    @Override
    public void generateKDTrees(Map<OSMType, List<LinePath>> linePaths, Bounds bounds) {
        saveRect(bounds);

        for (Map.Entry<OSMType, List<LinePath>> entry : linePaths.entrySet()) {
            if (entry.getValue().size() != 0) {
                saveKDTree(entry.getKey(), entry.getValue());
            }
        }
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
    public KDTree fetchKDTreeByType(OSMType type) {
        return kdTreeData.getKDTree(type);
    }

    @Override
    public void saveAllKDTrees(Map<OSMType, KDTree> trees) {
        kdTreeData.saveAllKDTrees(trees);
    }

    @Override
    public Map<OSMType, KDTree> fetchAllKDTrees() {
        return kdTreeData.getAllLKDTrees();
    }

}
