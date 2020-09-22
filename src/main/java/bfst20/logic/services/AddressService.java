package bfst20.logic.services;

import bfst20.data.AddressData;
import bfst20.logic.entities.Address;
import bfst20.logic.routing.TernarySearchTree;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressService {

    private AddressData addressData;

    public AddressService(AddressData addressData) {
        this.addressData = addressData;
    }

    public Address findAddress(String input) {
        TernarySearchTree tst = addressData.getTST();
        String[] addressStrings = parseAddress(input);

        if (addressStrings.length == 0) return null;
        addressStrings[0] = addressStrings[0].replaceAll(" ", "");

        for (Address address : tst.keysWithPrefix(addressStrings[0])) {
            if (address.getStreet() == null) continue;

            if (
                    address.getStreet().trim().toLowerCase().replaceAll(" ", "").equals(addressStrings[0].trim().toLowerCase())
                            && address.getHouseNumber().toLowerCase().trim().equals(addressStrings[1].trim().toLowerCase())
                            && (addressStrings[2].equals("") || (!addressStrings[2].equals("") && address.getPostcode().trim().equals(addressStrings[2].trim())))

            ) {

                return address;
            }
        }

        return null;
    }

    public Queue<Address> generateSearchSuggestions(String input) {
        TernarySearchTree tst = addressData.getTST();
        String[] addressStrings = parseAddress(input);

        if (addressStrings == null) return null;

        addressStrings[0] = addressStrings[0].replaceAll(" ", "");

        Queue<Address> addresses = tst.keysWithPrefix(addressStrings[0]);
        Queue<Address> newAddresses = new LinkedList<>();

        for (Address address : addresses) {

            if (addressStrings.length == 3 && !addressStrings[1].equals("")) {
                if (!address.getHouseNumber().startsWith(addressStrings[1])) {
                    continue;
                }
            }

            if (addressStrings.length == 3 && !addressStrings[2].equals("")) {
                if (!address.getPostcode().startsWith(addressStrings[2])) {
                    continue;
                }
            }

            newAddresses.add(address);
        }

        return newAddresses;
    }


    private String[] parseAddress(String input) {
        String addressRegex = addressData.getAddressRegex();
        Matcher pattern = Pattern.compile(addressRegex).matcher(input);

        if (pattern.matches() && !input.equals("") && pattern.groupCount() == 3) {
            String street = pattern.group("street");
            String house = pattern.group("house");
            String postcode = pattern.group("postcode");

            String[] address = {street, house, postcode};

            return address;
        } else {

            String[] string = {input.trim().replaceAll(" ", "")};

            return string;
        }
    }
}
