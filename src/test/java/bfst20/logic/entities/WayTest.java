package bfst20.logic.entities;

import bfst20.logic.misc.OSMType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WayTest {
    public static Way way1;
    public static Way way2;
    public static Way way3;
    public static List<Node> list;
    public static Node node1;
    public static Node node2;

    @BeforeAll
    public static void setup() {
        node1 = new Node(1, (float) 55.6388937, (float) 12.6195664);
        node2 = new Node(2, (float) 55.6388541, (float) 12.6195888);
        
        
        way1 = new Way();
        way2 = new Way(10);
        way2.addNode(node1);
        way2.addNode(node2);

        way3 = new Way(way2);
    }

    //Combined two methods, not to create excessive test.
    @Test
    public void get_setMaxSpeed(){
        way1.setMaxSpeed(130);
        way2.setMaxSpeed(110);
        way3.setMaxSpeed(80);
        assertEquals(way1.getMaxSpeed(), 130);
        assertEquals(way2.getMaxSpeed(), 110);
        assertEquals(way3.getMaxSpeed(), 80);
    }

    @Test
    public void is_setOneWay(){
        assertEquals(way1.isOneWay(), false);
        assertEquals(way2.isOneWay(), false);
        assertEquals(way3.isOneWay(), false);
        way1.setOneWay(true);
        way2.setOneWay(true);
        way3.setOneWay(true);
        assertEquals(way1.isOneWay(), true);
        assertEquals(way2.isOneWay(), true);
        assertEquals(way3.isOneWay(), true);
    }   

    
    @Test
    public void get_setName(){
        assertEquals(way1.getName(), null);
        assertEquals(way2.getName(), null);
        assertEquals(way3.getName(), null);
        way1.setName("1way");
        way2.setName("2way");
        way3.setName("3way");
        assertEquals(way1.getName(), "1way");
        assertEquals(way2.getName(), "2way");
        assertEquals(way3.getName(), "3way");
    }

    @Test
    public void is_setMultipolygon() {
        assertEquals(way1.isMultipolygon(), false);
        assertEquals(way2.isMultipolygon(), false);
        assertEquals(way3.isMultipolygon(), false);
        way1.setMultipolygon(true);
        way2.setMultipolygon(true);
        way3.setMultipolygon(true);
        assertEquals(way1.isMultipolygon(), true);
        assertEquals(way2.isMultipolygon(), true);
        assertEquals(way3.isMultipolygon(), true);
    }

    @Test
    public void get_setOSMType(){
        way1.setOSMType(OSMType.BUILDING);
        way2.setOSMType(OSMType.COASTLINE);
        way3.setOSMType(OSMType.NATURAL);
        assertEquals(way1.getOSMType(),OSMType.BUILDING);
        assertEquals(way2.getOSMType(),OSMType.COASTLINE);
        assertEquals(way3.getOSMType(),OSMType.NATURAL);
    }

    @Test
    public void getId(){
        assertEquals(way2.getId(),10);
        assertEquals(way3.getId(),10);
    }


    @Test
    public void get_addNodeId(){

    }
 
    @Test
    public void get_addNode(){
        list = new ArrayList<>();
        list.add(node1);
        list.add(node2);

        way1.addNode(node1);
        way1.addNode(node2);
        assertEquals(way1.getNodes(),list);
        assertEquals(way2.getNodes(),list);
        assertEquals(way3.getNodes(),list);

    }
    
    


    
}
