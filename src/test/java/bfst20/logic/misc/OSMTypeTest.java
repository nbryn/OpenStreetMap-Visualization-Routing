package bfst20.logic.misc;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static bfst20.logic.misc.OSMType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OSMTypeTest {
    public static Color color;
    
    
    @Test
    public void getLineWidth(){
        assertEquals(OSMType.getLineWidth(RESIDENTIAL_HIGHWAY,10),20);
        assertEquals(OSMType.getLineWidth(PATH,10),10);
    }  
    
    @Test
    public void getZoomLevel(){
        assertEquals(OSMType.getZoomLevel(ROUTING),1);
        assertEquals(OSMType.getZoomLevel(HIGHWAY),1.3011416847239474E9);
    }   
    
    @Test
    public void getFill(){
        assertEquals(OSMType.getFill(RESIDENTIAL_HIGHWAY),false);
        assertEquals(OSMType.getFill(WOOD),true);
    }   
    
    @Test
    public void getColorBlindColor() {
        assertEquals(OSMType.getColor(GREEN,true),Color.PINK);
        assertEquals(OSMType.getColor(WATER,true), Color.RED);
        assertEquals(OSMType.getColor(MEADOW,true),Color.TRANSPARENT);

    }

    @Test
    public void getNormalColor(){
        color = Color.rgb(255, 241, 186, 1);
        assertEquals(OSMType.getColor(BEACH,false),color);
        assertEquals(OSMType.getColor(BOUNDS,false),Color.TRANSPARENT);
        
    }

    @Test
    public void getMaxSpeed(){
        assertEquals(OSMType.getMaxSpeed(TERTIARY),80);
        assertEquals(OSMType.getMaxSpeed(PATH),60);
    }
}
