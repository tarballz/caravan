package edu.cmps121.app.activities;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import edu.cmps121.app.utilities.GetNearbyPlacesData;
import edu.cmps121.app.utilities.NearbyPlace;
import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

import static edu.cmps121.app.utilities.CaravanUtils.getPlaceUrl;


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

        TextView textView = (TextView) findViewById(R.id.team_name_tv);
        state = new State(this);

        String welcomeText = state.party + "\'s";
        textView.setText(welcomeText);

        // TODO: fix below

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

//        // ASK FOR PERMISSION TO WRITE TO EXTERNAL STORAGE
//        if (ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//        }
//        // ASK FOR PERMISSION TO WRITE TO EXTERNAL STORAGE
//        if (ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            throw new RuntimeException("Permission should be requested upon starting the app");

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location.
                    if (location != null) {
                        double lat = location.getLatitude();
                        double lng = location.getLongitude();

                        // I know this is terrible, I just wanted it done before the presentation.

                        String url = getPlaceUrl(lat, lng, "food");
                        GetNearbyPlacesData foodTask = new GetNearbyPlacesData();
                        // This will add elements to the foodNearby list.
                        foodTask.execute(url, "food");
//                            Log.d("PMA", "foodNearby.size()" + foodNearby.size());


                        url = getPlaceUrl(lat, lng, "gas_station");
                        GetNearbyPlacesData gasTask = new GetNearbyPlacesData();
                        gasTask.execute(url, "gas");

                        url = getPlaceUrl(lat, lng, "lodging");
                        GetNearbyPlacesData restTask = new GetNearbyPlacesData();
                        restTask.execute(url, "rest");
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

    public void onClickLogout(View view) {
        state.nextActivity(this, MainActivity.class);
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


    /**
     * Do nothing, force user to log out if they want to return
     **/
    @Override
    public void onBackPressed() {
    }
}
