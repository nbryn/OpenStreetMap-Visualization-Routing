package bfst20.data;

import bfst20.data.InterestPointData;
import bfst20.logic.entities.InterestPoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class InterestPointDataTest {

    static InterestPointData interestPointData;
    static InterestPoint interestPoint;

    @BeforeAll
    static void setup() {
        interestPointData = InterestPointData.getInstance();
        interestPoint = new InterestPoint(9999,8888);
    }

    @Test
    void getInstance() {
        assertEquals(interestPointData, InterestPointData.getInstance());
    }

    @Test
    void InterestPointLat(){
        assertEquals(interestPoint.getLatitude(),9999);
    }
    @Test
    void InterestPointLong(){
    assertEquals(interestPoint.getLongitude(),8888);
    }
    @Test
    void addIntrestPoint(){
        interestPointData.addIntrestPoint(interestPoint);
    }
    @Test
    void removeIntrestPoint(){
        interestPointData.addIntrestPoint(interestPoint);
        interestPointData.removeIntrestPoint(0);
    }

}
