package bfst20.logic.entities;

import java.io.Serializable;

public class Address implements Serializable {
    private String city,
            houseNumber,
            postcode,
            street;
    private float lat, lon;

    public Address(String city,
                   String houseNumber,
                   String postcode,
                   String street,
                   float lat,
                   float lon) {
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
        this.postcode = postcode;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStreet(){
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    @Override
    public String toString() {
        return street + " " + houseNumber + " " + postcode;
    }
}
