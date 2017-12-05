package edu.cmps121.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
;
import java.util.List;

import edu.cmps121.app.utilities.CustomListViewAdapter;
import edu.cmps121.app.utilities.NearbyPlace;
import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

import static edu.cmps121.app.utilities.CaravanUtils.shortToast;

public class MapsActivity extends AppCompatActivity {

    private State state;
    ListView foodListView;
    ListView gasListView;
    ListView restListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        state = new State(this);
        // God this is literally the worst code I've ever written.
        List<NearbyPlace> foodNearbyList = PartyMenuActivity.getNearbyFoodList();
        List<NearbyPlace> gasNearbyList  = PartyMenuActivity.getNearbyGasList();
        List<NearbyPlace> restNearbyList = PartyMenuActivity.getNearbyRestList();
        String[] foodNames = new String[foodNearbyList.size()];
        String[] gasNames  = new String[gasNearbyList.size()];
        String[] restNames = new String[restNearbyList.size()];
        for (int i = 0; i < foodNames.length; i++) {
            foodNames[i] = foodNearbyList.get(i).name;
            //Log.d("MAPACT", "foodNames" + foodNames[i]);
        }
        for (int i = 0; i < gasNames.length; i++) {
            gasNames[i] = gasNearbyList.get(i).name;
        }
        for (int i = 0; i < restNames.length; i++) {
            restNames[i] = restNearbyList.get(i).name;
        }

        CustomListViewAdapter customListViewAdapter = new CustomListViewAdapter(this, foodNames);
        foodListView = (ListView) findViewById(R.id.nearby_places_lv);
        foodListView.setAdapter(customListViewAdapter);

        customListViewAdapter = new CustomListViewAdapter(this, gasNames);
//        gasListView = (ListView) findViewById(R.id.nearbyGasLV);
//        gasListView.setAdapter(customListViewAdapter);

        customListViewAdapter = new CustomListViewAdapter(this, restNames);
        restListView = (ListView) findViewById(R.id.nearby_places_lv);
        restListView.setAdapter(customListViewAdapter);

    }

    public void mapsRedirect(View view) {
         Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(getPackageManager()) != null)
            startActivity(mapIntent);
        else
            shortToast(MapsActivity.this, "Please install Google Maps on your device");
    }

    public void mapsActivity(View view) {
        state.nextActivity(this, MapsOverlayActivity.class);
    }



}
