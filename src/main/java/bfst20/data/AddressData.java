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

    // TODO: Move this out of model
    static String regexMain = "^ *(?<street>[a-zA-ZæøåÆØÅ ]+)? *(?<house>[0-9]*)?(\\, *| *)(?<floor>[a-zA-Z0-9]?)(\\. *| *|\\.)(?<side>[a-zA-Z0-9.]{0,2})?(\\. *| *)(?<postcode>[0-9]{4})? *(?<city>[a-zA-ZæøåÆØÅ -]+)?$";
    public String[] parseAddress(String input){
        Matcher pattern = Pattern.compile(regexMain).matcher(input);

        if(pattern.matches() && !input.equals("")){
            String street = pattern.group("street");
            String house = pattern.group("house");
            String postcode = pattern.group("postcode");
            String city = pattern.group("city");

            String[] address = {street, house, postcode, city};

            return address;
        }else{
            return new String[0];
        }

    }

    public Address search(String input){

        String[] addressStrings = parseAddress(input);

        for(Address address : addresses.values()){
            if(
                        address.getStreet().trim().equals(addressStrings[0].trim())
                    &&  (addressStrings[1] == null || address.getHousenumber().trim().equals(addressStrings[1].trim()))
                    &&  (addressStrings[3] == null || address.getCity().trim().equals(addressStrings[3].trim()))
                    &&  (addressStrings[2] == null || address.getPostcode().trim().equals(addressStrings[2].trim()))
            ){
                return address;
            }
        }

        return null;
    }
}
