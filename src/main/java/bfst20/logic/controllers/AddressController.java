package bfst20.logic.controllers;

import bfst20.data.AddressData;
import bfst20.logic.controllers.interfaces.AddressAPI;
import bfst20.logic.entities.Address;
import bfst20.logic.routing.TernarySearchTree;

import java.util.Queue;

public class AddressController implements AddressAPI {

    private AddressData addressData;

    public AddressController(AddressData addressData) {
        this.addressData = addressData;
    }

    @Override
    public void saveAddressData(long id, Address address) {
        addressData.saveAddress(id, address);

    }

    @Override
    public void saveTSTData(TernarySearchTree ternarySearchTree) {
        addressData.saveTST(ternarySearchTree);
    }

    @Override
    public TernarySearchTree fetchTSTData() {
        return addressData.getTST();
    }

    @Override
    public Address findAddress(String input) {
        return addressData.findAddress(input);
    }

    @Override
    public Queue<Address> searchSuggestions(String input) {
        return addressData.searchSuggestions(input);
    }
}
