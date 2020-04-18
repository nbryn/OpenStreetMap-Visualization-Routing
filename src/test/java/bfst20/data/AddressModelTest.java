package bfst20.data;


import bfst20.logic.entities.Address;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;


class AddressModelTest {

    static AddressModel addressModel;

    @BeforeAll
    static void setup() {
        addressModel = AddressModel.getInstance();
    }

    @Test
    void putAddress() {
        Address address = new Address("Farum", "21", "3520", "2", 21, 22);
        addressModel.putAddress(23232, address);

        assertEquals(address, addressModel.getAddresses().get(22));
    }

    @Test
    void getAddresses() {
        assertEquals(1, addressModel.getAddresses().size());
    }

    @Test
    void getInstance() {
        assertEquals(addressModel, AddressModel.getInstance());
    }

    @Test
    void search() {
        addressModel.putAddress(1, new Address("Samsoe", "1", "1234", "Smediegyde", 1, 1));
        String searchString = "Smediegyde 1";
        Address address = addressModel.search(searchString);

        assertEquals("Smediegyde", address.getStreet());
    }
}