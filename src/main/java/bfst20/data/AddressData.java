package bfst20.data;

import bfst20.logic.entities.Address;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressData {

    private static AddressData addressData;
    private Map<Long, Address> addresses;

    private AddressData(){
        addresses = new HashMap<>();
    }

    public static AddressData getInstance(){
        if(addressData == null){
            addressData = new AddressData();
        }

        return addressData;
    }

    public void putAddress(long id, Address address) { addresses.put(id, address);}
    public Map<Long, Address> getAddresses(){return addresses;}


    private static String streethouse = "[,. ]*(?<street>[\\D]+)[,. ]+(?<house>[\\d\\w]{0,3}[\\w])[,.\\V ]*";

    public String[] parseAddress(String input){
        Matcher pattern = Pattern.compile(streethouse).matcher(input);

        if(pattern.matches() && !input.equals("")){
            String street = pattern.group("street");
            String house = pattern.group("house");

            String[] address = {street, house};

            return address;
        }else{
            return new String[0];
        }

    }

    public Address search(String input){

        String[] addressStrings = parseAddress(input);

        if(addressStrings.length == 0) return null;

        for(Address address : addresses.values()){
            if(
                            address.getStreet().trim().equals(addressStrings[0].trim())
                    &&      address.getHousenumber().trim().equals(addressStrings[1].trim())

            ){
                return address;
            }

        }

        return null;
    }
}
