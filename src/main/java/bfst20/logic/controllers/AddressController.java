package bfst20.logic.controllers;

import bfst20.data.AddressData;
import bfst20.logic.controllers.interfaces.AddressAPI;
import bfst20.logic.entities.Address;
import bfst20.logic.routing.TernarySearchTree;
import bfst20.logic.services.AddressService;

import java.util.Queue;

public class AddressController implements AddressAPI {
    private AddressService addressService;
    private AddressData addressData;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
        this.addressData = AddressData.getInstance();
    }

    @Override
    public void saveAddressData(Address address) {
        addressData.saveAddress(address);

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
        return addressService.findAddress(input);
    }

    @Override
    public Queue<Address> fetchSearchSuggestions(String input) {
        return addressService.generateSearchSuggestions(input);
    }
}
