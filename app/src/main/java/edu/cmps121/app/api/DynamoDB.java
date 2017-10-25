package edu.cmps121.app.api;

import android.support.v7.app.AppCompatActivity;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class DynamoDB {
    private AmazonDynamoDBClient ddbClient;
    private DynamoDBMapper mapper;
    private CountDownLatch latch;
    private boolean doesExist;

    public DynamoDB(AppCompatActivity activity) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                activity.getApplicationContext(),
                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
                Regions.US_WEST_2
        );

        ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));

        mapper = new DynamoDBMapper(ddbClient);
    }

    public boolean itemExists(Class itemClass, String primaryKey) {
        try {
            latch = new CountDownLatch(1);

            Runnable runnable = () -> {
                Object item = mapper.load(itemClass, primaryKey);
                doesExist = item != null;
                latch.countDown();
            };

            Thread thread = new Thread(runnable);
            thread.start();

            latch.await();

            return doesExist;
        } catch (InterruptedException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void saveItem(T item) throws ResourceNotFoundException {
        Runnable runnable = () -> mapper.save(item);

        Thread thread = new Thread(runnable);
        thread.start();
    }

    // TODO: implement a method that queries the cars table, adding the primary key of every item that matches the desired party to an ArrayList of Strings.
    // Use this method ^ to then get the names of all the cars for your party in the FindCarActivity
    public ArrayList<String> queryTable() { return null; }
}
