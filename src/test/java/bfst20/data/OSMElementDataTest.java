package bfst20.data;

import bfst20.logic.entities.Bounds;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Relation;
import bfst20.logic.entities.Way;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OSMElementDataTest {

    static OSMElementData osmElementData;

    @BeforeAll
    static void setup() {
        osmElementData = OSMElementData.getInstance();
    }

    @Test
    void getInstance() {
        assertEquals(osmElementData, OSMElementData.getInstance());
    }

    @Test
    void addRelation() {
        Relation relation = new Relation();

        osmElementData.addRelation(relation);

        assertEquals(relation, osmElementData.getOSMRelations().get(0));
    }

    @Test
    void getOSMRelations() {
        osmElementData.getOSMRelations().clear();

        Relation relation = new Relation();
        Relation relation2 = new Relation();

        osmElementData.addRelation(relation);
        osmElementData.addRelation(relation2);



        assertEquals(2, osmElementData.getOSMRelations().size());
    }

    @Test
    void setBounds() {
        Bounds bounds = new Bounds(12, 14, 16, 18);

        osmElementData.setBounds(bounds);

        assertEquals(12, osmElementData.getBounds().getMaxLat());
        assertEquals(14, osmElementData.getBounds().getMinLat());
        assertEquals(16, osmElementData.getBounds().getMaxLon());
        assertEquals(18, osmElementData.getBounds().getMinLon());
    }

    @Test
    void getBounds() {
        Bounds bounds = new Bounds(12, 14, 16, 18);

        osmElementData.setBounds(bounds);

        assertNotNull(osmElementData.getBounds());
    }

    @Test
    void addToNodeMap() {
        long id = 12345;
        Node node = new Node();

        osmElementData.addToNodeMap(id, node);

        assertNotNull(osmElementData.getOSMNodes().get(id));
    }

    @Test
    void getOSMNodes() {
        long id = 2223;
        Node node = new Node();

        osmElementData.addToNodeMap(id, node);

        assertEquals(node, osmElementData.getOSMNodes().get(id));
        assertNotNull(osmElementData.getOSMNodes());
    }

    @Test
    void addWay() {
        Way way = new Way();

        osmElementData.addWay(way);

        assertEquals(way, osmElementData.getOSMWays().get(0));
    }

    @Test
    void getOSMWays() {
        Way way = new Way();
        Way way2 = new Way();

        osmElementData.addWay(way);
        osmElementData.addWay(way2);

        assertNotNull(osmElementData.getOSMWays());
    }


}