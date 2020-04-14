package bfst20.logic.entities;

public class Address {
    private String  city,
                    housenumber,
                    postcode,
                    street;
    private float lat, lon;

    public Address(String city,
                   String housenumber,
                   String postcode,
                   String street,
                   float lat,
                   float lon){
        this.city = city;
        this.street = street;
        this.housenumber = housenumber;
        this.postcode = postcode;
        this.lat = lat;
        this.lon = lon;
    }

    public String getStreet(){
        return street;
    }
    public float getLat(){return lat;}
    public float getLon(){return lon;}
}
