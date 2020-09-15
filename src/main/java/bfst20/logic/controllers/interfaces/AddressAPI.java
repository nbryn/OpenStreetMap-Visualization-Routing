package bfst20.logic.controllers.interfaces;

import bfst20.logic.entities.Address;
import bfst20.logic.ternary.TST;

public interface AddressAPI {

    void saveAddressData(long id, Address address);

    void saveTSTData(TST tst);

    TST fetchTSTData();
}
