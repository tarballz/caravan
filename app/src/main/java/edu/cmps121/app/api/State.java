package edu.cmps121.app.api;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import java.io.Serializable;

import edu.cmps121.app.MainActivity;

/**
 * To be instantiated upon application start up. For sharing the user's key data between activities
 */
public class State implements Serializable {
    public String party;
    public String car;
    public String username;
    public DB db;

    public State(AppCompatActivity currentActivity) {
        db = new DB(currentActivity);

        try {
            Intent intent = currentActivity.getIntent();
            State state = (State) intent.getSerializableExtra("state");

            party = state.party;
            car = state.car;
            username = state.username;
        } catch (NullPointerException e) {
            if (!currentActivity.getClass().equals(MainActivity.class))
                throw new RuntimeException("State can only be null at start of MainActivity" + e);
        }
    }

    public void nextActivity(AppCompatActivity currentActivity,
                             Class destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.putExtra("state", this);
        currentActivity.startActivity(intent);
    }
}

