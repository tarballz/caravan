package edu.cmps121.app.api;

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

import edu.cmps121.app.model.Car;
import edu.cmps121.app.model.Party;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class DB {
    private AmazonDynamoDBClient ddbClient;
    private DynamoDBMapper mapper;
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

        // Do not use Handler in main thread. Could cause memory leak
        messageHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                shortToast(activity, msg.toString());
            }
        };
    }

    public <T> boolean saveItem(AppCompatActivity activity, T item) {
        // This try catch might not be effective. Consider moving inside thread
        try {
            Runnable runnable = () -> {
                mapper.save(item);
            };

            Thread thread = new Thread(runnable);
            thread.start();

            return true;
        } catch (ResourceNotFoundException e) {
            Log.w("DB", "Table does not exist or invalid POJO");
            shortToast(activity, "Failed to save data");
            return false;
        }
    }

    public void saveCarDB() {
        Log.d("blah", "balaahh");
        Runnable runnable = () -> {
            Car car = new Car();
            car.setCar("Batmobile");
            car.setDriver("Batman");
            car.setParty("Cool Party");

            Party party = new Party();
            party.setParty("Rager");
            party.setOwner("Blah");

            mapper.save(car);
            mapper.save(party);
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
    }
}
