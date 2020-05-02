package bfst20.logic.misc;

import org.junit.jupiter.api.Test;

import static bfst20.logic.misc.OSMType.PATH;
import static bfst20.logic.misc.OSMType.RESIDENTIAL_HIGHWAY;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OSMTypeTest {

    @Test
    public void getLineWidth(){
        assertEquals(OSMType.getLineWidth(RESIDENTIAL_HIGHWAY,10),20);
        assertEquals(OSMType.getLineWidth(PATH,10),10);
    }   
    
}
