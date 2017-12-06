package edu.cmps121.app.dynamo;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "parties")
public class Party {
    private String party;
    private String owner;
    private String dest_name; 
    private double destLat;
    private double destLng;

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

    @DynamoDBAttribute(attributeName = "destLat")
    public double getLat() { 
        return destLat;
    }
    
    public void setLat(double destLat) { 
        this.destLat = destLat; 
    }

    @DynamoDBAttribute(attributeName = "destLng")
    public double getLng() { 
        return destLng; 
    }
    
    public void setLng(double destLng) { 
        this.destLng = destLng; 
    }

    @DynamoDBAttribute(attributeName = "destName")
    public String getDestName() {
        return dest_name;
    }

    public void setDestName(String dest_name) {
        this.dest_name = dest_name;
    }
}
