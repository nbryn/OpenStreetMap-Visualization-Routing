package bfst20.data;

import bfst20.logic.entities.Address;

import java.util.HashMap;
import java.util.Map;

public class AddressModel {

    private static AddressModel addressModel;
    private Map<Long, Address> addresses;

    private AddressModel(){
        addresses = new HashMap<>();
    }

    public static AddressModel getInstance(){
        if(addressModel == null){
            addressModel = new AddressModel();
        }

        return addressModel;
    }

    public void putAddress(long id, Address address) { addresses.put(id, address);}
    public Map<Long, Address> getAddresses(){return addresses;}

    public Address search(String search){
        //TODO: Change this to use regex.
        for(Address address : addresses.values()){
            if(address.getStreet().equals(search)){
                return address;
            }
        }

        return null;
    }
}
