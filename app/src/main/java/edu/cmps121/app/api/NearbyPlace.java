package edu.cmps121.app.api;

public class NearbyPlace {
    public String name;
    public String vicinity;
    public double lat;
    public double lng;
    public String link;

    public NearbyPlace(String name, String vicinity, double lat, double lng) {
        this.name = name;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lng = lng;
        this.link = "http://maps.google.com/?ie=UTF8&hq=&ll=" + lat + "," + lng + "&z=13";
    }
}
