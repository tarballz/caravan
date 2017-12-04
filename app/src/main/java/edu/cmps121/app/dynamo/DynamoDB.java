package edu.cmps121.app.dynamo;

import android.content.Context;
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

    public DynamoDB(Context context) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context.getApplicationContext(),
                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
                Regions.US_WEST_2
        );

        client = new AmazonDynamoDBClient(credentialsProvider);
        client.setRegion(Region.getRegion(Regions.US_WEST_2));

        mapper = new DynamoDBMapper(client);
    }

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

    public <T> boolean itemExists(Class<T> itemClass, String primaryKey) {
        Object item = getItem(itemClass, primaryKey);

        return item != null;
    }

    public List<Map<String, AttributeValue>> queryTableByParty(String table, String party) {
        try {
            CountDownLatch latch = new CountDownLatch(1);

            Map<String, AttributeValue> expressionAttributeValues;
            expressionAttributeValues = new HashMap<>();
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

    public <T> void updateItem(Class<T> itemClass, String primaryKey, ItemUpdater updater) {
        Object item = getItem(itemClass, primaryKey);

        updater.update(item);
    }

    public interface ItemUpdater {
        void update(Object item);
    }
}
