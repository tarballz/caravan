package edu.cmps121.app.api;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

/**
 * To be instantiated upon application start up. For sharing the user's key data between activities
 */
public class State {
    public String party;
    public String car;
    public String username;


    public void transitionActivity(AppCompatActivity currentActivity,
                                Class destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        Bundle bundle = new Bundle();
        bundle.putParcelable("state", (Parcelable) this);
        intent.putExtras(bundle);
        currentActivity.startActivity(intent);

    }
}

