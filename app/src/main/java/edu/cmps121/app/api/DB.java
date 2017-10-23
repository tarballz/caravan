package edu.cmps121.app.api;

import android.support.v7.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import edu.cmps121.app.model.Party;

public class DB {
    private AmazonDynamoDBClient ddbClient;

    public DB(AppCompatActivity activity) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                activity.getApplicationContext(),
                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
                Regions.US_WEST_2
        );

        ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
    }

    public boolean loadPartyDB(String partyName) {
        DynamoDBMapper mapper = new DynamoDBMapper(ddbClient);
        Party party = mapper.load(Party.class, partyName);
        return party != null;
    }

    public boolean queryDB() {
        return true;
    }
}
