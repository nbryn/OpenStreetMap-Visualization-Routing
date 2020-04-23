package bfst20.data;

import bfst20.logic.LinePathGenerator;
import bfst20.logic.entities.Node;
import bfst20.logic.entities.Way;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LinePathGeneratorTest {
    static LinePathGenerator linePathGenerator;


    @BeforeAll
    static void setup() {
        linePathGenerator = LinePathGenerator.getInstance();
        Node node1 = new Node();
    }

    @Test
    void getInstance() {
        assertEquals(linePathGenerator, LinePathGenerator.getInstance());
    }

}
