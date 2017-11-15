package edu.cmps121.app.api;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import edu.cmps121.app.MainActivity;

/**
 * To be instantiated upon application start up. For sharing the user's key data between activities
 */
public class State implements Parcelable {
    private String currentActivityName;
    public String party;
    public String car;
    public String user;

    public enum Validate {
        USER, CAR, PARTY, USER_CAR, USER_PARTY, CAR_PARTY, USER_CAR_PARTY;
    }

    public State(AppCompatActivity currentActivity) {
        try {
            Intent intent = currentActivity.getIntent();
            State state = intent.getParcelableExtra("state");
            currentActivityName = currentActivity.getClass().getSimpleName();

            party = state.party;
            car = state.car;
            user = state.user;

            testActivityRequirements();
        } catch (NullPointerException e) {
            if (!currentActivity.getClass().equals(MainActivity.class))
                throw new RuntimeException("State can only be null at start of MainActivity" + e);
        }
    }

    protected State(Parcel in) {
        party = in.readString();
        car = in.readString();
        user = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(party);
        dest.writeString(car);
        dest.writeString(user);
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

    private void testActivityRequirements() {
        switch (currentActivityName) {
            case "CreatePartyActivity":
            case "FindPartyActivity":
                validateFields(Validate.USER);
                break;
            case "FindCarActivity":
            case "CreateCarActivity":
                validateFields(Validate.USER_PARTY);
        }
    }

    public void validateFields(Validate set) {
        boolean valid;

        switch (set) {
            case USER:
                valid = checkNull(user);
                break;
            case CAR:
                valid = checkNull(car);
                break;
            case PARTY:
                valid = checkNull(party);
                break;
            case USER_CAR:
                valid = checkNull(user) && checkNull(car);
                break;
            case USER_PARTY:
                valid = checkNull(user) && checkNull(party);
                break;
            case CAR_PARTY:
                valid = checkNull(car) && checkNull(party);
                break;
            case USER_CAR_PARTY:
                valid = checkNull(user) && checkNull(car) && checkNull(party);
                break;
            default:
                throw new RuntimeException("Bad switch case in State#validateUser");
        }

        if (!valid)
            throw new RuntimeException("Required state fields not yet set. Terminating app");
    }

    private boolean checkNull(String field) {
        return field != null && !field.isEmpty();
    }
}

