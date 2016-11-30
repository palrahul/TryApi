package com.watbots.tryapi.model;

import io.realm.RealmObject;

public class Address extends RealmObject {
    public String city;
    public String state;
    public String street;
    public double lat;
    public double lng;
    public String printable_address;
}
