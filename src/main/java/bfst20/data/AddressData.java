package bfst20.data;

import bfst20.logic.entities.Address;
import bfst20.logic.ternary.TST;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressData {
    private static String streethouse = "[,. ]*(?<street>[\\D]+)[,. ]+(?<house>[\\d\\w]{0,3}[\\w])[,.\\V ]*";
    private static AddressData addressData;
    private TST tst;

    private AddressData() {
        tst = new TST();
    }

    public static AddressData getInstance() {
        if (addressData == null) {
            addressData = new AddressData();
        }

        return addressData;
    }

    public void addAddress(long id, Address address) {
        if(address.getStreet() == null) return;
        tst.put(address.getStreet(), address);
    }

    public TST getTst(){
        return tst;
    }

    public void saveAddresses(Map<Long, Address> addresses) {
        //this.addresses = addresses;
    }

    public Map<Long, Address> getAddresses() {
        //return addresses;
        return null;
    }

    public String[] parseAddress(String input) {
        Matcher pattern = Pattern.compile(streethouse).matcher(input);

        if (pattern.matches() && !input.equals("")) {
            String street = pattern.group("street");
            String house = pattern.group("house");

            String[] address = {street, house};

            return address;
        } else {

            return new String[0];
        }
    }

    public Address search(String input) {
        String[] addressStrings = parseAddress(input);
        if (addressStrings.length == 0) return null;
        
        for (Address address : tst.keysWithPrefix(addressStrings[0])) {
            if (address.getStreet() == null) continue;

            if (
                    address.getStreet().trim().toLowerCase().equals(addressStrings[0].trim().toLowerCase())
                            && address.getHousenumber().toLowerCase().trim().equals(addressStrings[1].trim().toLowerCase())

            ) {

                return address;
            }
        }

        return null;
    }

	public void setTst(TST tst) {
        this.tst = tst;
	}
}
