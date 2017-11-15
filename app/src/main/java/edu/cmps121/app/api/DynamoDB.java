package edu.cmps121.app.api;

import android.content.ClipData;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import edu.cmps121.app.model.User;

public class DynamoDB {
    private DynamoDBMapper mapper;
    private AmazonDynamoDBClient client;
    private List<Map<String, AttributeValue>> itemsList;
    private Object item;

    private static final String TAG = DynamoDB.class.getSimpleName();

    public DynamoDB(AppCompatActivity activity) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                activity.getApplicationContext(),
                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
                Regions.US_WEST_2
        );

        client = new AmazonDynamoDBClient(credentialsProvider);
        client.setRegion(Region.getRegion(Regions.US_WEST_2));

        mapper = new DynamoDBMapper(client);
    }

//    public ItemStatus analyzeUser(Class<User> itemClass, User user) {
//        try {
//            CountDownLatch latch = new CountDownLatch(1);
//
//            Runnable runnable = () -> {
//                userItem = mapper.load(itemClass, user.getUser());
//
//                latch.countDown();
//            };
//
//            Thread thread = new Thread(runnable);
//            thread.start();
//
//            latch.await();
//
//            if (userItem == null)
//                return ItemStatus.USER_AVAILABLE;
//            if (userItem.getPassword().equals(user.getPassword()))
//                return ItemStatus.USER_EXISTS;
//            else
//                return ItemStatus.USER_INVALID;
//
//        } catch (InterruptedException | NullPointerException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public <T> Object getItem(Class<T> itemClass, String primaryKey) {
        try {
            CountDownLatch latch = new CountDownLatch(1);

            Runnable runnable = () -> {
                item = mapper.load(itemClass, primaryKey);
                latch.countDown();
            };

            Thread thread = new Thread(runnable);
            thread.start();

            latch.await();

            return item;
        } catch (InterruptedException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean itemExists(Class itemClass, String primaryKey) {
        Object item = getItem(itemClass, primaryKey);

        if (item == null)
            return false;
        else
            return true;
    }

//    public boolean itemExists(Class itemClass, String primaryKey) {
//        try {
//            CountDownLatch latch = new CountDownLatch(1);
//
//            Runnable runnable = () -> {
//                Object item = mapper.load(itemClass, primaryKey);
//                if (item != null)
//                    doesExist = true;
//                else
//                    doesExist = false;
//                latch.countDown();
//            };
//
//            Thread thread = new Thread(runnable);
//            thread.start();
//
//            latch.await();
//
//            return doesExist;
//        } catch (InterruptedException | NullPointerException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public List<Map<String, AttributeValue>> queryTableByParty(String table, String party) {
        try {
            CountDownLatch latch = new CountDownLatch(1);

            Map<String, AttributeValue> expressionAttributeValues;
            expressionAttributeValues = new HashMap<String, AttributeValue>();
            expressionAttributeValues.put(":val", new AttributeValue().withS(party));

            Runnable runnable = () -> {
                ScanRequest scanRequest = new ScanRequest()
                        .withTableName(table)
                        .withFilterExpression("party = :val")
                        .withExpressionAttributeValues(expressionAttributeValues);

                ScanResult result = client.scan(scanRequest);
                itemsList = result.getItems();

                latch.countDown();
            };

            Thread thread = new Thread(runnable);
            thread.start();

            latch.await();

            return itemsList;
        } catch (InterruptedException e) {
            Log.w(TAG, "Thread was interrupted: " + e);
            return null;
        }
    }

    public <T> void saveItem(T item) throws ResourceNotFoundException {
        Runnable runnable = () -> mapper.save(item);

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void updateUserParty(String username, String partyName) throws ResourceNotFoundException {
        Runnable runnable = () -> {
            User user = mapper.load(User.class, username);
            user.setParty(partyName);
            mapper.save(user);
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
