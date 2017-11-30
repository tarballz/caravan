package edu.cmps121.app.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.cmps121.app.R;
import edu.cmps121.app.dynamo.DynamoDB;
import edu.cmps121.app.utilities.State;
import edu.cmps121.app.dynamo.Car;
import edu.cmps121.app.dynamo.User;
import edu.cmps121.app.utilities.ThreadHandler;

import static edu.cmps121.app.utilities.CaravanUtils.isValidString;
import static edu.cmps121.app.utilities.CaravanUtils.shortToast;

public class MapsOverlayActivity extends AppCompatActivity implements OnMapReadyCallback {
    private State state;
    private DynamoDB dynamoDB;
    private GoogleMap googleMap;
    private Thread trackingThread;
    private HashMap<String, Marker> markers;
    private List<String> cars;
    private List<String> drivers;
    private List<String> colors;
    private List<LatLng> positions;
    private List<Float> bearings;
    private String currentColor;
    private boolean threadStop;

    private static final String TAG = MapsOverlayActivity.class.getSimpleName();
    private static final int TIME_LIMIT_MILLI = 500000000;
    private static final float INITIAL_ZOOM = 14.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_overlay);

        state = new State(this);
        dynamoDB = new DynamoDB(this);
        markers = new HashMap<>();
        threadStop = false;

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        threadStop = false;
        startTrackingThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        threadStop = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        threadStop = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        setStyle();
        setCameraPosition();
        setIcons();
        startTrackingThread();
    }

    private void setCameraPosition() {
        User userItem = (User) dynamoDB.getItem(User.class, state.user);

        if (userItem == null)
            throw new RuntimeException("User does not exist in DB. Critical Failure");

        if (isValidString(userItem.getCar()))
            googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(getCarPosition(userItem.getCar()), INITIAL_ZOOM)
            );
        else
            googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(getUserPosition(), INITIAL_ZOOM)
            );
    }

    private LatLng getCarPosition(String car) {
        Car carItem = (Car) dynamoDB.getItem(Car.class, car);

        return new LatLng(carItem.getLat(), carItem.getLng());
    }

    private LatLng getUserPosition() {
        LocationManager locationManager =
                (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            throw new RuntimeException("Permission should be requested upon starting the app");

        Location currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    private void setStyle() {
        try {
            boolean success;
            switch (state.jsonOption) {
                case NIGHT:
                    success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.mapstyle_night));
                    break;
                case GREYSCALE:
                    success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.mapstyle_greyscale));
                    break;
                case RETRO:
                    success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.mapstyle_retro));
                    break;
                default:
                    throw new RuntimeException("Json style was not initialized");
            }

            if (!success)
                Log.e(TAG, "Style parsing failed.");
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setIcons() {
        checkCarsTable();

        if (cars.size() > 0)
            spawnMarkers();
        else
            shortToast(this, "Create a car to view its location on the map");
    }

    private void checkCarsTable() {
        try {
            List<Map<String, AttributeValue>> carsTable = dynamoDB.queryTableByParty("cars", state.party);

            cars = carsTable.stream()
                    .map(e -> e.get("car").getS())
                    .collect(Collectors.toList());

            drivers = carsTable.stream()
                    .map(e -> e.get("driver").getS())
                    .collect(Collectors.toList());

            positions = carsTable.stream()
                    .map(e -> new LatLng(Float.valueOf(e.get("lat").getN()), Float.valueOf(e.get("lng").getN())))
                    .collect(Collectors.toList());

            bearings = carsTable.stream()
                    .map(e -> Float.valueOf(e.get("bearing").getN()))
                    .collect(Collectors.toList());

            colors = carsTable.stream()
                    .map(e -> e.get("color").getS())
                    .collect(Collectors.toList());

            if (cars.size() != drivers.size() ||
                    cars.size() != colors.size() ||
                    cars.size() != positions.size())
                throw new RuntimeException("Error in our DynamoDB cars table. " +
                        "|drivers| != |cars| != |colors| != |positions|");
        } catch (NullPointerException e) {
            Log.i(TAG, "Cars table could not be loaded, leaving values");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void spawnMarkers() {
        List<Map<String, AttributeValue>> usersTable = dynamoDB.queryTableByParty("users", state.party);

        for (int i = 0; i < cars.size(); ++i) {
            currentColor = colors.get(i);
            int color = getCarColor(currentColor);

            String carName = cars.get(i);
            String carDriver = drivers.get(i);
            String snippet = createSnippet(usersTable, carName, carDriver);

            BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(color, null);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap smallCar = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(positions.get(i))
                    .title(carName)
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromBitmap(smallCar))
                    .rotation(bearings.get(i)));
            marker.setTag(currentColor);

            markers.put(cars.get(i), marker);
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

    private void startTrackingThread() {
        try {
            checkCarsTable();
            ArrayList<LatLng> oldPositions = new ArrayList<>(positions);

            ThreadHandler handler = new ThreadHandler(this);
            Runnable runnable = () -> trackDynamo(handler, oldPositions);

            if (trackingThread == null || !trackingThread.isAlive()) {
                trackingThread = new Thread(runnable);
                trackingThread.setName("TrackingThread");
                trackingThread.start();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.i(TAG, "Cars table was updated, resulting in |positions| > |oldPositions|");
            startTrackingThread();
        }
    }

    // TODO: remove endTime
    private void trackDynamo(ThreadHandler handler, ArrayList<LatLng> oldPositions) throws IndexOutOfBoundsException {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        long endTime = System.currentTimeMillis() + TIME_LIMIT_MILLI;

        while (System.currentTimeMillis() < endTime && !threadStop) {
            checkCarsTable();

            for (int i = 0; i < cars.size(); ++i) {
                String carName = cars.get(i);
                LatLng newPosition = positions.get(i);
                LatLng oldPosition = oldPositions.get(i);
                float newBearing = bearings.get(i);

                if (newPosition.latitude != oldPosition.latitude ||
                        newPosition.longitude != oldPosition.longitude) {
                    Message msg = new Message();
                    Bundle bundle = new Bundle();

                    oldPositions.set(i, newPosition);

                    bundle.putString("carName", carName);
                    bundle.putDouble("lat", newPosition.latitude);
                    bundle.putDouble("lng", newPosition.longitude);
                    bundle.putFloat("bearing", newBearing);

                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void processBundle(Bundle bundle) {
        String carName = bundle.getString("carName");
        Marker marker = markers.get(carName);
        double lat = bundle.getDouble("lat");
        double lng = bundle.getDouble("lng");
        float bearing = bundle.getFloat("bearing");

        if (!isValidString(carName) || lat == 0.0 || lng == 0.0)
            throw new RuntimeException("Error in thread data transfer");

        marker.setPosition(new LatLng(lat, lng));
        marker.setRotation(bearing);

        Log.i(TAG, "Successfully updated " + carName + " marker position");
    }

    /**
     * Customizes a marker's info window and its contents.
     */
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View mWindow;
        private final View mContents;

        CustomInfoWindowAdapter() {
            mWindow = getLayoutInflater().inflate(R.layout.custom_info_window, null);
            mContents = getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {
            TextView titleUi = ((TextView) view.findViewById(R.id.title));
            TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
            String title = marker.getTitle();
            String snippet = marker.getSnippet();
            String color = (String) marker.getTag();
            assert color != null;

            int badge;
            switch (color) {
                case "green":
                    badge = R.drawable.badge_green;
                    break;
                case "yellow":
                    badge = R.drawable.badge_yellow;
                    break;
                case "cyan":
                    badge = R.drawable.badge_blue;
                    break;
                case "red":
                    badge = R.drawable.badge_blue;
                    break;
                default:
                    badge = 0;
            }

            ((ImageView) view.findViewById(R.id.badge)).setImageResource(badge);

            if (title != null) {
                SpannableString titleText = new SpannableString(title);
                titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);

                titleUi.setText(titleText);
            } else
                titleUi.setText("");

            if (snippet != null && snippet.length() > 12) {
                SpannableString snippetText = new SpannableString(snippet);

                snippetText.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, 10, 0);
                snippetText.setSpan(new ForegroundColorSpan(Color.BLUE), 12, snippet.length(), 0);

                snippetUi.setText(snippetText);
            } else
                snippetUi.setText("");
        }
    }
}
