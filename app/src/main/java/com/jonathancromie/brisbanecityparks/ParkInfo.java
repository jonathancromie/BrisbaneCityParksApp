package com.jonathancromie.brisbanecityparks;

/**
 * Created by jonathancromie on 6/07/15.
 */
public class ParkInfo {
    String id;
    String name;
    String street;
    String suburb;
    String latitude;
    String longitude;
    String distance;

    public ParkInfo(String id, String name, String street, String suburb, String latitude, String longitude, String distance) {
        this.id = id;
        this.name = name;
        this.street = street;
        this.suburb = suburb;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }



}
