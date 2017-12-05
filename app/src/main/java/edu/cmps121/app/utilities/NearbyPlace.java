package edu.cmps121.app.utilities;

import com.google.android.gms.maps.model.LatLng;

public class NearbyPlace {
    public String name;
    public String vicinity;
    public LatLng position;
    public String link;

    public NearbyPlace(String name, String vicinity, LatLng position) {
        this.name = name;
        this.vicinity = vicinity;
        this.position = position;
        this.link = "http://maps.google.com/?ie=UTF8&hq=&ll=" +
                position.latitude +
                "," +
                position.longitude +
                "&z=13";
    }
}
