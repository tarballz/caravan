package edu.cmps121.app.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "cars")
public class Car {
    private String car;
    private String driver;
    private String party;
    private String color;
    private double lat;
    private double lng;

    @DynamoDBHashKey(attributeName = "car")
    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @DynamoDBAttribute(attributeName = "driver")
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @DynamoDBAttribute(attributeName = "party")
    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    @DynamoDBAttribute(attributeName = "lat")
    public double getLat() { return lat;}

    public void setLat(double lat) { this.lat = lat; }

    @DynamoDBAttribute(attributeName = "lng")
    public double getLng() { return lng; }

    public void setLng(double lng) { this.lng = lng; }

    @DynamoDBAttribute(attributeName = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
