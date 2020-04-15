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
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("samsoe.osm").getFile());
        AppController appController = new AppController();
        appController.loadFile(file);

        AddressModel addressModel = AddressModel.getInstance();

        String searchString = "Smediegyde 1";

        Address address = addressModel.search(searchString);

        assert address != null;

        System.out.println(address.toString());
    }
}