package edu.cmps121.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


import edu.cmps121.app.R;
import edu.cmps121.app.dynamo.DynamoDB;
import edu.cmps121.app.utilities.State;
import edu.cmps121.app.dynamo.Party;
import edu.cmps121.app.dynamo.User;

import static edu.cmps121.app.utilities.CaravanUtils.shortToast;

public class CreatePartyActivity extends AppCompatActivity {

    private State state;
    private DynamoDB dynamoDB;
    private Place destination;
    private String partyName;


    private static final String TAG = CreatePartyActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        state = new State(this);
        dynamoDB = new DynamoDB(this);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                destination = place;
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "An error occurred: " + status);
            }
        });

    }

    public void onClickCreateParty(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_create_party_name_et);
        partyName = editText.getText().toString();

        if (destination.getLatLng().longitude == 0.0 || destination.getLatLng().latitude == 0.0)
            shortToast(this, "Please enter a location");
        else if (dynamoDB.itemExists(Party.class, partyName))
            shortToast(this, partyName + " has already been taken");
        else {
                updateDB();
                state.nextActivity(this, PartyMenuActivity.class);
        }
    }

    private void updateDB() {
        try {
            Party party = new Party();

            party.setParty(partyName);
            party.setOwner(state.user);
            party.setLat(destination.getLatLng().latitude);
            party.setLng(destination.getLatLng().longitude);

            dynamoDB.saveItem(party);

            dynamoDB.updateItem(User.class, state.user, (obj) -> {
                User user = (User) obj;
                user.setParty(partyName);

                dynamoDB.saveItem(user);
            });

            state.party = partyName;
        } catch (ResourceNotFoundException e) {
            Log.w("DB", "Table does not exist or invalid POJO");
            shortToast(this, "Failed to save data");
        }
    }
}
