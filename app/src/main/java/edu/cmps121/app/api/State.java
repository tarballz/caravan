package edu.cmps121.app.api;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import edu.cmps121.app.MainActivity;

/**
 * To be instantiated upon application start up. For sharing the user's key data between activities
 */
public class State implements Parcelable {
    public String party;
    public String car;
    public String username;
    public DB db;

    public State(AppCompatActivity currentActivity) {
        db = new DB(currentActivity);

        try {
            Intent intent = currentActivity.getIntent();
            State state = intent.getParcelableExtra("state");

            party = state.party;
            car = state.car;
            username = state.username;
        } catch (NullPointerException e) {
            if (!currentActivity.getClass().equals(MainActivity.class))
                throw new RuntimeException("State can only be null at start of MainActivity" + e);
        }
    }

    protected State(Parcel in) {
        party = in.readString();
        car = in.readString();
        username = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(party);
        dest.writeString(car);
        dest.writeString(username);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<State> CREATOR = new Creator<State>() {
        @Override
        public State createFromParcel(Parcel in) {
            return new State(in);
        }

        @Override
        public State[] newArray(int size) {
            return new State[size];
        }
    };

    public void nextActivity(Context currentActivity,
                             Class destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.putExtra("state", this);
        currentActivity.startActivity(intent);
    }
}

