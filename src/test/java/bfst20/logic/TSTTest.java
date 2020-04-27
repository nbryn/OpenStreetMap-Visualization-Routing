package bfst20.logic;

import bfst20.logic.entities.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TSTTest {


    @Test
    public void put(){

        Address address = new Address("Hilleroed", "10", "3400", "test", 1, 1, 1);

        bfst20.logic.ternary.TST tst = new bfst20.logic.ternary.TST();
        tst.put("Hello 1", address);
        tst.put("Hej 2", address);
        tst.put("Hellsevej 3", address);
        tst.put("Hellsevej 4", address);

        assert tst.getSize() == 18;
    }

    @Test
    public void get(){
        Address addressRes = new Address("Not hillerod", "10", "3400", "test", 1, 1, 1);
        Address address = new Address("Hilleroed", "10", "3400", "test", 1, 1, 1);

        bfst20.logic.ternary.TST tst = new bfst20.logic.ternary.TST();
        tst.put("Hello 1", address);
        tst.put("Hej 2", addressRes);
        tst.put("Hellsevej 3", address);
        tst.put("Hellsevej 4", address);

        assert tst.get("Hej 2") == addressRes;
    }
}
