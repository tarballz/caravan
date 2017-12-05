package edu.cmps121.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
;
import java.util.ArrayList;
import java.util.List;

import edu.cmps121.app.utilities.GetNearbyPlacesData;
import edu.cmps121.app.utilities.NearbyPlace;
import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

import static edu.cmps121.app.utilities.CaravanUtils.shortToast;

public class MapsActivity extends AppCompatActivity implements GetNearbyPlacesData.Callback {

    private State state;
    private ListView nearbyPlaces;
    private List<String> nearbyFood;
    private List<String> nearbyGas;
    private List<String> nearbyRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        state = new State(this);
        nearbyPlaces = (ListView) findViewById(R.id.nearby_places_lv);

        nearbyFood = new ArrayList<>();
        nearbyGas = new ArrayList<>();
        nearbyRest = new ArrayList<>();
    }

    // TODO: make it so it redirects to actual destination for party
    public void onClickDirections(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null)
            startActivity(mapIntent);
        else
            shortToast(MapsActivity.this, "Please install Google Maps on your device");
    }

    public void onClickTrackParty(View view) {
        state.nextActivity(this, MapsOverlayActivity.class);
    }

    public void onClickFood(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nearbyFood
        );
        nearbyPlaces.setAdapter(adapter);
    }

    public void onClickGas(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nearbyGas
        );
        nearbyPlaces.setAdapter(adapter);
    }

    public void onClickRest(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nearbyRest
        );
        nearbyPlaces.setAdapter(adapter);
    }


    @Override
    public void addPlace(String placeType, String placeName) {
        switch (placeType) {
            case "food":
                nearbyFood.add(placeName);
                break;
            case "gas":
                nearbyGas.add(placeName);
                break;
            case "rest":
                nearbyRest.add(placeName);
                break;
            default:
                throw new RuntimeException("Bad switch case. Invalid placeType");
        }
    }
}
