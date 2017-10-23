package edu.cmps121.app.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "cars")
public class Car {
    private String car;
    private String driver;
    private String party;

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
}
