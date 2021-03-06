package edu.cmps121.app.utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import edu.cmps121.app.activities.MainActivity;

import static edu.cmps121.app.utilities.CaravanUtils.JsonOptions.*;
import static edu.cmps121.app.utilities.CaravanUtils.isValidString;

/**
 * To be instantiated upon application start up. For sharing the user's key data between activities
 */
public class State implements Parcelable {

    private String currentActivityName;
    public String party;
    public String car;
    public String user;
    public CaravanUtils.JsonOptions jsonOption;

    private enum Validate {
        USER, CAR, PARTY, USER_CAR, USER_PARTY, CAR_PARTY, USER_CAR_PARTY
    }

    public State(AppCompatActivity currentActivity) {
        try {
            Intent intent = currentActivity.getIntent();
            State state = intent.getParcelableExtra("state");
            currentActivityName = currentActivity.getClass().getSimpleName();

            party = state.party;
            car = state.car;
            user = state.user;
            jsonOption = state.jsonOption;

            testActivityRequirements();
        } catch (NullPointerException e) {
            if (!currentActivity.getClass().equals(MainActivity.class))
                throw new RuntimeException("State can only be null at start of MainActivity" + e);
            jsonOption = NIGHT;
        }
    }

    protected State(Parcel in) {
        party = in.readString();
        car = in.readString();
        user = in.readString();
        switch (in.readString()) {
            case "retro":
                jsonOption = RETRO;
                break;
            case "night":
                jsonOption = NIGHT;
                break;
            case "greyscale":
                jsonOption = GREYSCALE;
                break;
            default:
                throw new RuntimeException("Bad switch case");
        }

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(party);
        dest.writeString(car);
        dest.writeString(user);
        switch (jsonOption) {
            case RETRO:
                dest.writeString("retro");
                break;
            case NIGHT:
                dest.writeString("night");
                break;
            case GREYSCALE:
                dest.writeString("greyscale");
                break;
            default:
                throw new RuntimeException("Bad switch case");
        }
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

    public <T> void nextActivity(Context currentActivity,
                             Class<T> destinationActivity) {
        Intent intent = new Intent(currentActivity, destinationActivity);
        intent.putExtra("state", this);
        currentActivity.startActivity(intent);
    }

    // Add checks here as necessary
    private void testActivityRequirements() {
        switch (currentActivityName) {
            case "PartyMenuActivity":
            case "PartyOptionsActivity":
            case "CreatePartyActivity":
            case "FindPartyActivity":
            case "SettingsActivity":
                validateFields(Validate.USER);
                break;
            case "FindCarActivity":
            case "CreateCarActivity":
            case "MapsActivity":
                validateFields(Validate.USER_PARTY);
                break;
            case "MainActivity":
                break;
            default:
                throw new RuntimeException("This activity has not yet been registered in " +
                        "State#testActivityRequirements");
        }
    }

    private void validateFields(Validate set) {
        boolean valid;

        switch (set) {
            case USER:
                valid = isValidString(user);
                break;
            case CAR:
                valid = isValidString(car);
                break;
            case PARTY:
                valid = isValidString(party);
                break;
            case USER_CAR:
                valid = isValidString(user) && isValidString(car);
                break;
            case USER_PARTY:
                valid = isValidString(user) && isValidString(party);
                break;
            case CAR_PARTY:
                valid = isValidString(car) && isValidString(party);
                break;
            case USER_CAR_PARTY:
                valid = isValidString(user) && isValidString(car) && isValidString(party);
                break;
            default:
                throw new RuntimeException("Bad switch case in State#validateUser");
        }

        if (!valid)
            throw new RuntimeException("Required state fields not yet set. Terminating app");
    }
}

