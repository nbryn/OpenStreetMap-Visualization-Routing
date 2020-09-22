package bfst20.data;

import bfst20.logic.entities.Address;
import bfst20.logic.routing.TernarySearchTree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressData {
    private String addressRegex = "[,. ]*(?<street>[\\D]+)[,. ]+(?<house>[\\d][\\w]*)[,. ]*(?<postcode>[\\w]*)[,.\\V]*";
    private static AddressData addressData;
    private TernarySearchTree ternarySearchTree;

    private AddressData() {
        ternarySearchTree = new TernarySearchTree();
    }

    public static AddressData getInstance() {
        if (addressData == null) {
            addressData = new AddressData();
        }

        return addressData;
    }

    public String getAddressRegex() {
        return this.addressRegex;
    }

    public void saveAddress(Address address) {
        if (address.getStreet() == null) return;
        ternarySearchTree.put(address.getStreet().replaceAll(" ", ""), address);
    }

    public TernarySearchTree getTST() {
        return ternarySearchTree;
    }

    public void clearData() {
        ternarySearchTree = new TernarySearchTree();
    }


    public void saveTST(TernarySearchTree ternarySearchTree) {
        this.ternarySearchTree = ternarySearchTree;
    }
}
