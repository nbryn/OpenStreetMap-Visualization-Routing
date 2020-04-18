package bfst20.data;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OSMElementModelTest {

    static OSMElementModel osmElementModel;

    @BeforeAll
    static void setup() {
        osmElementModel = OSMElementModel.getInstance();
    }

    @Test
    void getInstance() {
        assertEquals(osmElementModel, OSMElementModel.getInstance());
    }

    @Test
    void addRelation() {
        Relation relation = new Relation();

        osmElementModel.addRelation(relation);

        assertEquals(relation, osmElementModel.getOSMRelations().get(0));
    }

    @Test
    void getOSMRelations() {
        osmElementModel.getOSMRelations().clear();

        Relation relation = new Relation();
        Relation relation2 = new Relation();

        osmElementModel.addRelation(relation);
        osmElementModel.addRelation(relation2);



        assertEquals(2, osmElementModel.getOSMRelations().size());
    }

    @Test
    void setBounds() {
        Bounds bounds = new Bounds(12, 14, 16, 18);

        osmElementModel.setBounds(bounds);

        assertEquals(12, osmElementModel.getBounds().getMaxLat());
        assertEquals(14, osmElementModel.getBounds().getMinLat());
        assertEquals(16, osmElementModel.getBounds().getMaxLon());
        assertEquals(18, osmElementModel.getBounds().getMinLon());
    }

    @Test
    void getBounds() {
        Bounds bounds = new Bounds(12, 14, 16, 18);

        osmElementModel.setBounds(bounds);

        assertNotNull(osmElementModel.getBounds());
    }

    @Test
    void addToNodeMap() {
        long id = 12345;
        Node node = new Node();

        osmElementModel.addToNodeMap(id, node);

        assertNotNull(osmElementModel.getOSMNodes().get(id));
    }

    @Test
    void getOSMNodes() {
        long id = 2223;
        Node node = new Node();

        osmElementModel.addToNodeMap(id, node);

        assertEquals(node, osmElementModel.getOSMNodes().get(id));
        assertNotNull(osmElementModel.getOSMNodes());
    }

    @Test
    void addWay() {
        Way way = new Way();

        osmElementModel.addWay(way);

        assertEquals(way, osmElementModel.getOSMWays().get(0));
    }

    @Test
    void getOSMWays() {
        Way way = new Way();
        Way way2 = new Way();

        osmElementModel.addWay(way);
        osmElementModel.addWay(way2);

        assertNotNull(osmElementModel.getOSMWays());
    }


}