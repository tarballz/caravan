package edu.cmps121.app;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import edu.cmps121.app.api.CaravanUtils;
import edu.cmps121.app.api.GetNearbyPlacesData;
import edu.cmps121.app.api.NearbyPlace;
import edu.cmps121.app.api.State;


public class PartyMenuActivity extends AppCompatActivity {
    private State state;
    public static List<NearbyPlace> foodNearby = new ArrayList<>();
    public static List<NearbyPlace> gasNearby = new ArrayList<>();
    public static List<NearbyPlace> restNearby = new ArrayList<>();
    // Used to get the User's location.
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_menu);

        state = new State(this);

        TextView textView = (TextView) findViewById(R.id.team_name_tv);
        textView.setText(state.party + "\'s");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // ASK FOR PERMISSION TO WRITE TO EXTERNAL STORAGE
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        // ASK FOR PERMISSION TO WRITE TO EXTERNAL STORAGE
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location.
                        if (location != null) {
                            double lat = location.getLatitude();
                            double lng = location.getLongitude();

                            // I know this is terrible, I just wanted it done before the presentation.

                            String url = CaravanUtils.getPlaceUrl(lat, lng, "food");
                            GetNearbyPlacesData foodTask = new GetNearbyPlacesData();
                            // This will add elements to the foodNearby list.
                            foodTask.execute(url, 'f');
//                            Log.d("PMA", "foodNearby.size()" + foodNearby.size());


                            url = CaravanUtils.getPlaceUrl(lat, lng, "gas_station");
                            GetNearbyPlacesData gasTask = new GetNearbyPlacesData();
                            gasTask.execute(url, 'g');

                            url = CaravanUtils.getPlaceUrl(lat, lng, "lodging");
                            GetNearbyPlacesData restTask = new GetNearbyPlacesData();
                            restTask.execute(url, 'r');
                        }
                    }
                });

    }

    public void onClickCreateCarMenu(View view) {
       state.nextActivity(this, CreateCarActivity.class);
    }

    public void onClickFindCarMenu(View view) {
        state.nextActivity(this, FindCarActivity.class);
    }

    public void onClickMapsMenu(View view) {
        state.nextActivity(this, MapsActivity.class);
    }

    public void onClickSettingsMenu(View view) {
        state.nextActivity(this, SettingsActivity.class);
    }

    public static void addNearbyFoodPlace(NearbyPlace p) {
        foodNearby.add(p);
    }
    public static List<NearbyPlace> getNearbyFoodList() {
        return foodNearby;
    }

    public static void addNearbyGasPlace(NearbyPlace p) {
        gasNearby.add(p);
    }

    public static List<NearbyPlace> getNearbyGasList() {
        return gasNearby;
    }

    public static void addNearbyRestPlace(NearbyPlace p) {
        restNearby.add(p);
    }

    public static List<NearbyPlace> getNearbyRestList() {
        return restNearby;
    }
}
