package bfst20.logic.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class interestpointTest {
    public static InterestPoint interestPoint;

    @BeforeAll
    public static void setup() {
        interestPoint = new InterestPoint(10, 12);
    }

    @Test
    public void getLatitude() {
        assertEquals(interestPoint.getLatitude(), 10);
    }

    @Test
    public void getLongitude() {
        assertEquals(interestPoint.getLongitude(), 12);
    }
}
