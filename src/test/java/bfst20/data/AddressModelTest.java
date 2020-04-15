package bfst20.data;

import bfst20.logic.AppController;
import bfst20.logic.entities.Address;
import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class AddressModelTest {

    @Test
    void search() throws IOException, XMLStreamException {

        AddressModel addressModel = AddressModel.getInstance();

        addressModel.putAddress(1, new Address("Samsoe", "1", "1234", "Smediegyde", 1, 1));

        String searchString = "Smediegyde 1";

        Address address = addressModel.search(searchString);

        assert address != null;

        System.out.println(address.toString());
    }
}