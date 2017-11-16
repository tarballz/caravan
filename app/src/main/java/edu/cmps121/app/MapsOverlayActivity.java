package edu.cmps121.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;
import edu.cmps121.app.model.User;

import static edu.cmps121.app.api.CaravanUtils.isValidString;
import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class MapsOverlayActivity extends AppCompatActivity implements OnMapReadyCallback {
    private State state;
    private DynamoDB dynamoDB;
    private GoogleMap googleMap;
    private HashMap<String, Marker> markers;
    private HashMap<String, LatLng> startingPositions;
    private List<String> cars;
    private List<String> drivers;
    private List<String> colors;
    private double prevLat;
    private double prevLon;
    private double nextLat;
    private double nextLon;
    private double startingLat;
    private double startingLon;
    private double RADIUS = .05;
    private boolean quadIorIV;

    private static final String TAG = MapsOverlayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_overlay);

        state = new State(this);
        dynamoDB = new DynamoDB(this);

        markers = new HashMap<>();
        startingPositions = new HashMap<>();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setStyle();
        setCameraPosition();
        setIcons();
        moveIcons();
    }

    private void setCameraPosition() {
        User userItem = (User) dynamoDB.getItem(User.class, state.user);

        if (userItem == null)
            throw new RuntimeException("User does not exist in DB. Critical Failure");

        if (isValidString(userItem.getCar()))
            // TODO: get the driver of the car's gps coordinates
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));
        else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                shortToast(this, "Please turn on GPS");
                return;
            }

            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(locationGPS.getLatitude(), locationGPS.getLongitude())));
        }
    }

    private void setStyle() {
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success)
                Log.e(TAG, "Style parsing failed.");
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setIcons() {
        List<Map<String, AttributeValue>> carsTable = dynamoDB.queryTableByParty("cars", state.party);

        cars = carsTable.stream()
                .map(e -> e.get("car").getS())
                .collect(Collectors.toList());

        drivers = carsTable.stream()
                .map(e -> e.get("driver").getS())
                .collect(Collectors.toList());

//        // TODO: implement this
//        List<String> positions = itemList.stream()
//                .map(e -> e.get("position").getS())
//                .collect(Collectors.toList());
//
        colors = carsTable.stream()
                .map(e -> e.get("color").getS())
                .collect(Collectors.toList());

        if (cars.size() != drivers.size() || cars.size() != colors.size())
            throw new RuntimeException("Error in our DynamoDB cars table. |drivers| != |cars| != |colors|");

        if (cars.size() > 0)
            spawnMarkers();
        else
            shortToast(this, "Create a car to view its location on the map");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void spawnMarkers() {
        List<Map<String, AttributeValue>> usersTable = dynamoDB.queryTableByParty("users", state.party);

        for (int i = 0; i < cars.size(); ++i) {
            int color = getCarColor(colors.get(i));

            String carName = cars.get(i);
            String carDriver = drivers.get(i);
            String snippet = createSnippet(usersTable, carName, carDriver);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(color, null);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap smallCar = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

            markers.put(
                    cars.get(i),
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(0, i / 10.0 + RADIUS))
                            .title(carName)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallCar)))
            );

            // TODO: Remove me. Only for use in demo
            startingPositions.put(cars.get(i), new LatLng(0, (i / 10.0)));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String createSnippet(List<Map<String, AttributeValue>> usersTable,
                                 String currentCar,
                                 String currentDriver) {
        String snippet = "Driver: " + currentDriver + "\n Occupants: ";

        List<String> occupants = usersTable.stream()
                .filter(e -> isOccupant(e, currentCar))
                .map(e -> e.get("user").getS())
                .collect(Collectors.toList());

        for (String occupant : occupants)
            snippet += occupant + "\n";

        return snippet;
    }

    private boolean isOccupant(Map<String, AttributeValue> item, String currentCar) {
        AttributeValue carItem = item.get("car");

        return carItem != null && isValidString(carItem.getS()) && carItem.getS().equals(currentCar);
    }

    private int getCarColor(String color) {
        switch (color) {
            case "cyan":
                return R.drawable.cyan;
            case "red":
                return R.drawable.red;
            case "yellow":
                return R.drawable.yellow;
            case "green":
                return R.drawable.green;
            default:
                throw new RuntimeException("Error in car table. Invalid color");
        }
    }

    private void moveIcons() {
        int count = 0;
        while (count < 60) {
            for (String key : markers.keySet()) {
                Runnable runnable = () -> threadMovement(key);

                Handler handler = new Handler();

                handler.postDelayed(runnable, 1000 + count * 1000);
            }
            count++;
        }
    }

    private void threadMovement(String key) {
        Marker marker = markers.get(key);
        startingLat = startingPositions.get(key).latitude;
        startingLon = startingPositions.get(key).longitude;

        prevLat = marker.getPosition().latitude;
        prevLon = marker.getPosition().longitude;

        findNextLat();
        findNextLon();

        marker.setPosition(new LatLng(nextLat, nextLon));
        marker.setRotation(getRotation());

    }

    private void findNextLat() {
        if (prevLat > startingLat && prevLon > startingLon ||          // Quadrant I
                prevLat <= startingLat && prevLon >= startingLon) {    // Quadrant IV
            nextLat = prevLat + .01;
            quadIorIV = true;
        } else if (prevLat >= startingLat && prevLon <= startingLon || // Quadrant II
                prevLat < startingLat && prevLon < startingLon) {      // Quadrant III
            nextLat = prevLat - .01;
            quadIorIV = false;
        }
    }

    private void findNextLon() {
        double lon = (quadIorIV)
                ? (Math.sqrt(Math.pow(RADIUS, 2) - Math.pow((nextLat - startingLat), 2)) + startingLon)
                : -(Math.sqrt(Math.pow(RADIUS, 2) - Math.pow((nextLat - startingLat), 2)) + startingLon);

        if (Double.isNaN(lon))
            nextLon = startingLon;
        else
            nextLon = lon;
    }

    private float getRotation() {
        double prevLatRad = Math.toRadians(prevLat);
        double nextLatRad = Math.toRadians(nextLat);
        double lonDiff = Math.toRadians(nextLon - prevLon);

        double y = Math.sin(lonDiff) * Math.cos(nextLatRad);
        double x = Math.cos(prevLatRad) * Math.sin(nextLatRad) -
                Math.sin(prevLatRad) * Math.cos(nextLatRad) * Math.cos(lonDiff);

        return (float) -((Math.toDegrees(Math.atan2(y, x)) + 360) % 360);
    }
}
