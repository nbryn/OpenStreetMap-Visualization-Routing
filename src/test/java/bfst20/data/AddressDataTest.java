package bfst20.data;


import bfst20.logic.entities.Address;
import bfst20.logic.routing.TernarySearchTree;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AddressDataTest {

    private static AddressData addressData;


    @BeforeAll
    static void setup() {
        addressData = AddressData.getInstance();


    }

    @Test
    void getInstance() {
        assertEquals(addressData, AddressData.getInstance());
    }

    @Test
    void saveAddress() {
        TernarySearchTree ternarySearchTree = new TernarySearchTree();
        addressData.saveTST(ternarySearchTree);
        Address address = new Address("Farum", "21", "3520", "2", 21, 22, 26);
        addressData.saveAddress(23232, address);

        assertEquals(1, ternarySearchTree.getSize());
    }

    @Test
    void getTST() {
        TernarySearchTree ternarySearchTree = new TernarySearchTree();
        addressData.saveTST(ternarySearchTree);
        assertEquals(ternarySearchTree, addressData.getTST());
    }


    @Test
    void findAddress() {
        addressData.saveAddress(1, new Address("Samsoe", "1", "1234", "Smediegyde", 1, 1, 321));
        String searchString = "Smediegyde 1";
        Address address = addressData.findAddress(searchString);

        assertEquals("Smediegyde", address.getStreet());
    }

    @Test
    void parseAddress() {
    }

    @Test
    void parseAddressElse() {
        String[] test = addressData.parseAddress("as");

        String[] testString = {"as"};

        assertEquals(test[0], testString[0]);
    }

    @Test
    void searchSuggestions() {
        Address address = new Address("Samsoe", "1", "1234", "Smediegydee", 1, 1, 321);
        addressData.saveAddress(1, address);
        Queue<Address> test = addressData.searchSuggestions("Smediegydee");

        assertEquals(test.poll(), address);

        test = addressData.searchSuggestions("Smediegydee 1");

        assertEquals(test.poll(), address);

        test = addressData.searchSuggestions("Smediegydee 1 1234");

        assertEquals(test.poll(), address);
    }

    @Test
    void saveTST() {
    }
}