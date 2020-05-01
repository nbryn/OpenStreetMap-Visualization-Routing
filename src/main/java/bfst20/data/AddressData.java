package bfst20.data;

import bfst20.logic.entities.Address;
import bfst20.logic.ternary.TST;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddressData {

    //TODO: Better naming
    private static String streethouse = "[,. ]*(?<street>[\\D]+)[,. ]+(?<house>[\\d\\w]{0,3}[\\w])[,. ]*(?<postcode>[\\w]*)[,.\\V]*";
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

    public void saveAddress(long id, Address address) {
        if(address.getStreet() == null) return;
        tst.put(address.getStreet(), address);
    }

    public TST getTST(){
        return tst;
    }


    public String[] parseAddress(String input) {
        Matcher pattern = Pattern.compile(streethouse).matcher(input);

        if (pattern.matches() && !input.equals("")) {
            String street = pattern.group("street");
            String house = pattern.group("house");
            String postcode = pattern.group("postcode");

            String[] address = {street, house, postcode};

            return address;
        } else {

            String[] string = {input.trim()};

            return string;
        }
    }

    public Queue<Address> searchSuggestions(String input){

        String[] addressStrings = parseAddress(input);

        if(addressStrings == null) return null;

        Queue<Address> addresses = getTST().keysWithPrefix(addressStrings[0]);
        Queue<Address> newAddresses = new LinkedList<>();

        for(Address address : addresses){

            if(addressStrings.length == 3 && !addressStrings[1].equals("")){
                if(!address.getHousenumber().startsWith(addressStrings[1])){
                    continue;
                }
            }

            if(addressStrings.length == 3 && !addressStrings[2].equals("")){
                if(!address.getPostcode().startsWith(addressStrings[2])){
                    continue;
                }
            }

            newAddresses.add(address);
        }

        return newAddresses;
    }

    public Address findAddress(String input) {
        String[] addressStrings = parseAddress(input);
        if (addressStrings.length == 0) return null;
        
        for (Address address : tst.keysWithPrefix(addressStrings[0])) {
            if (address.getStreet() == null) continue;

            if (
                    address.getStreet().trim().toLowerCase().equals(addressStrings[0].trim().toLowerCase())
                            && address.getHousenumber().toLowerCase().trim().equals(addressStrings[1].trim().toLowerCase())
                            && (addressStrings[2].equals("") || (!addressStrings[2].equals("") && address.getPostcode().trim().equals(addressStrings[2].trim())))

            ) {

                return address;
            }
        }

        return null;
    }

	public void saveTST(TST tst) {
        this.tst = tst;
	}
}
