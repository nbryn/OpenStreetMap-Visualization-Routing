package bfst20.logic.controllers;

import bfst20.data.AddressData;
import bfst20.logic.entities.Address;

public class AddressController implements AddressAPI {

    private AddressData addressData;

    public AddressController(AddressData addressData) {
        this.addressData = addressData;
    }


    @Override
    public void saveAddressData(long id, Address address) {
        addressData.saveAddress(id, address);

    }
}
