package edu.cmps121.app.model;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.*;
import java.util.Arrays;

/**
 * Created by Payton on 10/10/17.
 */

@DynamoDBTable(tableName = "Teams")
public class Team {

    private String name;
    private String[] cars;

    @DynamoDBIndexRangeKey(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {

    }
}
