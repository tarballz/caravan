package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;


import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;
import edu.cmps121.app.model.Party;
import edu.cmps121.app.model.User;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class CreatePartyActivity extends AppCompatActivity {
    private State state;
    private String TAG = "CPA";
    public DynamoDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_party);

        state = new State(this);
        db = new DynamoDB(this); 

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                state.dest_lat = place.getLatLng().latitude;
                state.dest_lng = place.getLatLng().longitude;
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    public void onClickCreateParty(View view) {
        EditText editText = (EditText) findViewById(R.id.enter_create_party_name_et);
        Party party = new Party();
        String potentialParty = editText.getText().toString();

        if (isUnique(potentialParty) && state.dest_lat != 0.0) {
            party.setParty(potentialParty);
            party.setOwner(state.username);
            party.setLat(state.dest_lat);
            party.setLng(state.dest_lng);
            state.party = potentialParty;
            try {
                db.saveItem(party);
                db.updateUserParty(state.username, potentialParty);
                state.nextActivity(this, PartyMenuActivity.class);
            } catch (ResourceNotFoundException e) {
                Log.w("DB", "Table does not exist or invalid POJO");
                shortToast(this, "Failed to save data");
            }
        } else if (state.dest_lat == 0.0) {
            shortToast(this, "Please enter a location.");
        }
        else
            shortToast(this, potentialParty + " has already been taken");
    }

    private boolean isUnique(String potentialParty) {
        return !db.itemExists(Party.class, potentialParty);
    }
}
