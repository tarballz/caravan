package edu.cmps121.app.model;


import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

@DynamoDBTable(tableName = "parties")
public class Party {
    private String party;
    private String owner;

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
}
