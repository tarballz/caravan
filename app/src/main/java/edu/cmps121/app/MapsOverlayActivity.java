package edu.cmps121.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
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

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class MapsOverlayActivity extends AppCompatActivity implements OnMapReadyCallback {
    private State state;
    private DynamoDB dynamoDB;
    private GoogleMap googleMap;
    private HashMap<String, Marker> markers;
    private List<String> cars;
    private List<String> drivers;
    private List<String> colors;

    private static final String TAG = MapsOverlayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_overlay);

        state = new State(this);
        dynamoDB = new DynamoDB(this);

        markers = new HashMap<>();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

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

        if (userItem.getCar() != null && !userItem.getCar().isEmpty())
            // TODO: get the driver of the car's gps coordinates
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));
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

        spawnMarkers();
    }

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
                            .position(new LatLng(-34, 150 + i / 10))
                            .title(carName)
                            .snippet(snippet)
                            .icon(BitmapDescriptorFactory.fromBitmap(smallCar)))
            );
        }
    }

    private String createSnippet(List<Map<String, AttributeValue>> usersTable,
                                 String currentCar,
                                 String currentDriver) {
        String snippet = "Driver: " + currentDriver + "\n Occupants: ";

        List<String> occupants = usersTable.stream()
                .filter(e -> e.get("car").getS().equals(currentCar))
                .map(e -> e.get("user").getS())
                .collect(Collectors.toList());

        for (String occupant : occupants)
            snippet += occupant + "\n";

        return snippet;
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
//        Iterator it = markers.entrySet().iterator();
//        for (Map.Entry e = (Map.Entry) it.next(); it.hasNext(); e = (Map.Entry) it.next()) {
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    boolean blah = false;
//
//                    while (true) {
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (!blah)
//                            e.getValue().setCameraPosition(new LatLng(-34, 151));
//                        else
//                            e.getValue().setCameraPosition(new LatLng(-34, 150));
//                    }
//                }
//            };
//
//            Thread thread = new Thread(runnable);
//            thread.start();
//        }
    }
}
