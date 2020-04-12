package bfst20.logic.entities;

public class Address {
    private String  city,
                    housenumber,
                    postcode,
                    street;

    public Address(String city,
                   String housenumber,
                   String postcode,
                   String street){
        this.city = city;
        this.street = street;
        this.housenumber = housenumber;
        this.postcode = postcode;
    }

    public String getStreet(){
        return street;
    }
}
