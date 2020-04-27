package bfst20.data;


import bfst20.logic.entities.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


class AddressDataTest {

    static AddressData addressData;

    @BeforeAll
    static void setup() {
        addressData = AddressData.getInstance();
    }

    @Test
    void getInstance() {
        assertEquals(addressData, AddressData.getInstance());
    }

    @Test
    void putAddress() {
        addressData.getAddresses().clear();
        Address address = new Address("Farum", "21", "3520", "2", 21, 22, 26);
        addressData.addAddress(23232, address);

        assertEquals(1, addressData.getAddresses().size());
    }

    @Test
    void getAddresses() {
        addressData.getAddresses().clear();
        addressData.addAddress(23232, new Address("Farum", "21", "3520", "2", 21, 22, 25));

        assertEquals(1, addressData.getAddresses().size());
    }


    @Test
    void search() {
        addressData.addAddress(1, new Address("Samsoe", "1", "1234", "Smediegyde", 1, 1, 321));
        String searchString = "Smediegyde 1";
        Address address = addressData.search(searchString);

        assertEquals("Smediegyde", address.getStreet());
    }
}