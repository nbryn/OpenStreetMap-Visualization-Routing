package bfst20.logic.controllers.interfaces;

import bfst20.logic.entities.Address;
import bfst20.logic.routing.TernarySearchTree;

import java.util.Queue;

public interface AddressAPI {

    void saveAddressData(long id, Address address);

    void saveTSTData(TernarySearchTree ternarySearchTree);

    TernarySearchTree fetchTSTData();

    Address findAddress(String input);

    Queue<Address> searchSuggestions(String input);
}
