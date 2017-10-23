package edu.cmps121.app.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "cars")
public class Car {
    private String car;
    private String driver;
    private String party;

    @DynamoDBIndexHashKey(attributeName = "car")
    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    @DynamoDBIndexHashKey(attributeName = "driver")
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    @DynamoDBIndexHashKey(attributeName = "party")
    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }
}
