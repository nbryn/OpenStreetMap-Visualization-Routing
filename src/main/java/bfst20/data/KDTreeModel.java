package bfst20.data;

import bfst20.logic.Type;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;

import java.util.HashMap;
import java.util.Map;

public class KDTreeModel {

    private static boolean isLoaded = false;
    private static KDTreeModel kdTreeModel;
    private Map<Type, KDTree> kdTrees;
    private Rect rect;

    private KDTreeModel() {
        kdTrees = new HashMap<>();
        rect = new Rect();
    }

    public static KDTreeModel getInstance() {
        if (!isLoaded) {
            isLoaded = true;
            kdTreeModel = new KDTreeModel();
        }
        return kdTreeModel;
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
        kdTrees = null;
        System.gc();
    }




}
