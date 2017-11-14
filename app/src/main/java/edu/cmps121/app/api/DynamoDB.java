package edu.cmps121.app.api;

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

import edu.cmps121.app.model.Car;
import edu.cmps121.app.model.Party;
import edu.cmps121.app.model.User;

public class DynamoDB {
    private DynamoDBMapper mapper;
    private AmazonDynamoDBClient client;
    private List<Map<String, AttributeValue>> itemsList;
    private Party partyItem;
    private Car carItem;
    private User userItem;
    private boolean doesExist;

    private static final String TAG = DynamoDB.class.getSimpleName();

    public enum ItemStatus {
        USER_EXISTS, USER_AVAILABLE, USER_INVALID;
    }

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

    public ItemStatus userExists(Class<User> itemClass, User user) {
        try {
            CountDownLatch latch = new CountDownLatch(1);
            Bundle bundle = new Bundle();

            Runnable runnable = () -> {
                User item = mapper.load(itemClass, user.getUser());
                if (item != null)
                    bundle.putInt("doesExist", item.getPassword().equals(user.getPassword()) ? 0 : 1);
                else
                    bundle.putInt("doesExist", 2);

                latch.countDown();
            };

            Thread thread = new Thread(runnable);
            thread.start();

            latch.await();

            switch (bundle.getInt("doesExist")) {
                case 0:
                    return ItemStatus.USER_EXISTS;
                case 1:
                    return ItemStatus.USER_INVALID;
                case 2:
                    return ItemStatus.USER_AVAILABLE;
            }

            throw new RuntimeException("Error in checking for user in DB");
        } catch (InterruptedException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean carExists(Class<Car> itemClass, Car car) {
        try {
            CountDownLatch latch = new CountDownLatch(1);

            Runnable runnable = () -> {
                carItem = (Car) mapper.load(itemClass, car.getCar());
                latch.countDown();
            };

            Thread thread = new Thread(runnable);
            thread.start();

            latch.await();

            if (carItem != null)
                return true;
            else
                return false;
        } catch (InterruptedException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean itemExists(Class itemClass, String primaryKey) {
        try {
            CountDownLatch latch = new CountDownLatch(1);

            Runnable runnable = () -> {
                Object item = mapper.load(itemClass, primaryKey);
                if (item != null)
                    doesExist = true;
                else
                    doesExist = false;
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

    public List<Map<String, AttributeValue>> queryTable(String table, String key) {
        try {
            CountDownLatch latch = new CountDownLatch(1);

            Map<String, AttributeValue> expressionAttributeValues =
                    new HashMap<String, AttributeValue>();
            expressionAttributeValues.put(":val", new AttributeValue().withS(key));

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
