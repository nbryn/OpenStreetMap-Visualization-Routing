package bfst20.logic;

import bfst20.data.LinePathData;
import bfst20.logic.LinePathGenerator;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import bfst20.logic.misc.OSMType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LinePathGeneratorTest {
    static LinePathGenerator linePathGenerator;
    public static Way way1;
    public static Way way2;
    public static Way way3;

    @BeforeAll
    static void setup() {
        linePathGenerator = LinePathGenerator.getInstance();
        Node node1 = new Node(1, (float) 55.6388937, (float) 12.6195664);
        Node node2 = new Node(2, (float) 55.6388541, (float) 12.6195888);
        Node node3 = new Node(3, (float) 55.6388636, (float) 12.6196557);
        Node node4 = new Node(4, (float) 55.6389036, (float) 12.6196342);
        Node node5 = new Node(5, (float) 55.6389305, (float) 12.6193886);
        Node node6 = new Node(6, (float) 55.6388968, (float) 12.6194073);
        Node node7 = new Node(7, (float) 55.6389019, (float) 12.6194476);
        Node node8 = new Node(8, (float) 55.6389368, (float) 12.6194316);
        Node node9 = new Node(9, (float) 55.6390171, (float) 12.6192845);
        Node node10 = new Node(10, (float) 55.6389979, (float) 12.6192956);
        Node node11 = new Node(11, (float) 55.6390061, (float) 12.6193435);
        Node node12 = new Node(12, (float) 55.6390253, (float) 12.6193344);
        way1 = new Way(13);
        way1.addNode(node1);
        way1.addNode(node2);
        way1.addNode(node3);
        way1.addNode(node4);
        way1.addNode(node1);
        way1.addNodeId(1);
        way1.addNodeId(2);
        way1.addNodeId(3);
        way1.addNodeId(4);
        way1.addNodeId(1);

        way2 = new Way(14);
        way2.addNode(node5);
        way2.addNode(node6);
        way2.addNode(node7);
        way2.addNode(node8);
        way2.addNode(node5);

        way2.addNodeId(5);
        way2.addNodeId(6);
        way2.addNodeId(7);
        way2.addNodeId(8);
        way2.addNodeId(5);

        way3 = new Way(15);

        way3.addNode(node9);
        way3.addNode(node10);
        way3.addNode(node11);
        way3.addNode(node12);
        way3.addNode(node9);

        way3.addNodeId(9);
        way3.addNodeId(10);
        way3.addNodeId(11);
        way3.addNodeId(12);
        way3.addNodeId(9);
    }

    @Test
    void getInstance() {
        assertEquals(LinePathGenerator.getInstance(),linePathGenerator);
    }

    @Test
    void combine() throws Exception {

        Method method = LinePathGenerator.class.getDeclaredMethod("combine", Way.class, Way.class);
        method.setAccessible(true);
        Way way = (Way) method.invoke(linePathGenerator, way1, way2);
        assertEquals(way.getFirstNodeId(), 1);
        assertEquals(way.getLastNodeId(), 5);
    }
    @Test
    void combineSize() throws Exception {

        Method method = LinePathGenerator.class.getDeclaredMethod("combine", Way.class, Way.class);
        method.setAccessible(true);
        Way way = (Way) method.invoke(linePathGenerator, way1, way2);
        assertEquals(way.getFirstNodeId(), 1);
        assertEquals(way.getNodeIds().size(), 10);
    }

}