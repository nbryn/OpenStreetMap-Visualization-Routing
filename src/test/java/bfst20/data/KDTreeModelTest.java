package bfst20.data;

import bfst20.logic.Type;
import bfst20.logic.entities.LinePath;
import bfst20.logic.kdtree.KDTree;
import bfst20.logic.kdtree.Rect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class KDTreeModelTest {

    static KDTreeModel kdTreeModel;
    static Rect rect;

    @BeforeAll
    static void setup() {
        kdTreeModel = KDTreeModel.getInstance();
        rect = new Rect();

    }

    @Test
    void getInstance() {
        assertEquals(kdTreeModel, KDTreeModel.getInstance());
    }

    @Test
    void getRect() {
        assertNotNull(kdTreeModel.getRect());
    }

    @Test
    void setValuesOnRect() throws NoSuchFieldException {
        kdTreeModel.setValuesOnRect(10, 15, 20, 25);

        assertEquals(10, kdTreeModel.getRect().getMinLat());
        assertEquals(15, kdTreeModel.getRect().getMaxLat());
        assertEquals(20, kdTreeModel.getRect().getMinLon());
        assertEquals(25, kdTreeModel.getRect().getMaxLon());
    }

    @Test
    void addKDTree() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(40, 45, 50, 55);
        linePaths.add(linePath);

        KDTree kdTree = new KDTree(linePaths, rect);

        kdTreeModel.addKDTree(Type.COASTLINE, kdTree);

     /*   Field kdTrees = KDTreeModel.class.getDeclaredField("kdTrees");
        kdTrees.setAccessible(true);
        Map<Type, KDTree> map = (Map<Type, KDTree>) kdTrees.get(kdTreeModel);

        Method get = map.getClass().getDeclaredMethod("get", new Class[]{});

        KDTree returned = (KDTree) get.invoke(map, Type.COASTLINE);*/

        assertEquals(kdTreeModel.getKDTree(Type.COASTLINE), kdTree);

    }

    @Test
    void getKDTree() {
        List<LinePath> linePaths = new ArrayList<>();
        LinePath linePath = new LinePath(40, 45, 50, 55);
        linePaths.add(linePath);

        KDTree kdTree = new KDTree(linePaths, rect);
        kdTreeModel.addKDTree(Type.COASTLINE, kdTree);

        assertEquals(kdTreeModel.getKDTree(Type.COASTLINE), kdTree);
    }
}