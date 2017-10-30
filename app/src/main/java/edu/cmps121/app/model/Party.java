package edu.cmps121.app.model;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "parties")
public class Party {
    private String party;
    private String owner;
    private double dest_lat;
    private double dest_lng;

    @DynamoDBHashKey(attributeName = "party")
    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    @DynamoDBAttribute(attributeName = "owner")
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @DynamoDBAttribute(attributeName = "dest_lat")
    public double getLat() { return dest_lat;}
    public void setLat(double dest_lat) { this.dest_lat = dest_lat; }

    @DynamoDBAttribute(attributeName = "dest_lng")
    public double getLng() { return dest_lng; }
    public void setLng(double dest_lng) { this.dest_lng = dest_lng; }
}
