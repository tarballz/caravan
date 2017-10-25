package edu.cmps121.app.api;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

// TODO: implement a method that queries our tables, checking if an item's primary key has already been used

public class DB {
    private AmazonDynamoDBClient ddbClient;
    private DynamoDBMapper mapper;
    // If try catch block for saveItem fails, implement messageHanlder. So far it's working
    private Handler messageHandler;

    public DB(AppCompatActivity activity) {
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                activity.getApplicationContext(),
                "us-west-2:3d86ea2c-db71-4953-bc20-8eb77c931e43", // Identity pool ID
                Regions.US_WEST_2
        );

        ddbClient = new AmazonDynamoDBClient(credentialsProvider);
        ddbClient.setRegion(Region.getRegion(Regions.US_WEST_2));
        mapper = new DynamoDBMapper(ddbClient);

        messageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                shortToast(activity, msg.toString());
            }
        };
    }

    public boolean itemExists(Class itemClass, String primaryKey) {
        Runnable runnable = () -> {
            Object item = mapper.load(itemClass, primaryKey);
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putBoolean("itemExists", item != null);
            message.setData(bundle);
            messageHandler.dispatchMessage(message);
        };

        Thread thread = new Thread(runnable);
        thread.start();

        Message message = Message.obtain(messageHandler);
        Bundle bundle = message.getData();
        return bundle.getBoolean("itemExists");
    }

    public <T> void saveItem(T item) throws ResourceNotFoundException {
        Runnable runnable = () -> mapper.save(item);

        Thread thread = new Thread(runnable);
        thread.start();
    }
}
