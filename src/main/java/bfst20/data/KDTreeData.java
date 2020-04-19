package bfst20.data;

import bfst20.logic.Type;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;

import java.util.HashMap;
import java.util.Map;

public class KDTreeData {

    private static boolean isLoaded = false;
    private static KDTreeData kdTreeData;
    private Map<Type, KDTree> kdTrees;
    private Rect rect;

    private KDTreeData() {
        kdTrees = new HashMap<>();
        rect = new Rect();
    }

    public static KDTreeData getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            kdTreeData = new KDTreeData();
        }
        return kdTreeData;
    }

    public Rect getRect() {
        return this.rect;
    }


    public void setValuesOnRect(float minLat, float maxLat, float minLon, float maxLon) {
        rect.setMinLat(minLat);
        rect.setMaxLat(maxLat);
        rect.setMinLon(minLon);
        rect.setMaxLon(maxLon);
    }

    public void addKDTree(Type type, KDTree kdTree) {
        kdTrees.put(type, kdTree);

    }

    public KDTree getKDTree(Type type) {
        return kdTrees.get(type);
    }

    public void clearData() {
        kdTrees = new HashMap<>();
        System.gc();
    }




}
