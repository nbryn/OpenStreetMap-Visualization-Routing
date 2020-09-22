package bfst20.logic;

import bfst20.logic.entities.Address;
import bfst20.logic.routing.TernarySearchTree;
import org.junit.jupiter.api.Test;

import java.util.Queue;

public class TernarySearchTreeTest {

    @Test
    public void put() {
        Address address = new Address("Hilleroed", "10", "3400", "test", 1, 1);
        TernarySearchTree ternarySearchTree = new TernarySearchTree();

        ternarySearchTree.put("Hello", address);
        ternarySearchTree.put("Hej", address);
        ternarySearchTree.put("Hellsevej", address);

        assert ternarySearchTree.getSize() == 11;
    }

    @Test
    public void get() {
        Address addressRes = new Address("Not hillerod", "10", "3400", "test", 1, 1);
        Address address = new Address("Hilleroed", "10", "3400", "test", 1, 1);
        TernarySearchTree ternarySearchTree = new TernarySearchTree();

        ternarySearchTree.put("Hello", address);
        ternarySearchTree.put("Hej", addressRes);

        ternarySearchTree.put("Hellsevej", address);
        ternarySearchTree.put("Hellsevej", address);

        assert ternarySearchTree.get("Hej").get(0) == addressRes;
    }

    @Test
    public void keysWithPrefix() {
        Address Address1 = new Address("Hilleroed", "10", "3400", "Hello", 1, 1);
        Address Address2 = new Address("Hilleroed", "10", "3400", "Hej", 1, 1);

        Address Address3 = new Address("Hilleroed", "10", "3400", "Hellsevej", 1, 1);
        TernarySearchTree ternarySearchTree = new TernarySearchTree();

        ternarySearchTree.put("Hello", Address1);
        ternarySearchTree.put("Hej", Address2);
        ternarySearchTree.put("Hellsevej", Address3);

        Queue<Address> addresses = ternarySearchTree.keysWithPrefix("Hello");

        assert addresses.size() == 1;
    }

    @Test
    public void spaceTest() {
        Address Address1 = new Address("Hjørring", "10", "9800", "Skagen landevej", 1, 1);
        TernarySearchTree ternarySearchTree = new TernarySearchTree();

        ternarySearchTree.put(Address1.getStreet(), Address1);

        Queue<Address> addresses = ternarySearchTree.keysWithPrefix("Skagen landevej");

        assert addresses.poll() == Address1;
    }
}
