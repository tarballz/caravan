package edu.cmps121.app;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.cmps121.app.api.DynamoDB;
import edu.cmps121.app.api.State;

public class MapsActivityRaw extends AppCompatActivity implements OnMapReadyCallback {
    State state;
    DynamoDB dynamoDB;
    GoogleMap googleMap;
    Map<String, Marker> markers;

    private static final String TAG = MapsActivityRaw.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_raw);

        state = new State(this);
        dynamoDB = new DynamoDB(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setStyle();
        setPosition();
        setIcons();

//        // TODO: fix this. Make it stop after 20 seconds of alternating
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                boolean blah = false;
//
//                while (true) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (!blah)
//                        marker.setPosition(new LatLng(-34, 151));
//                    else
//                        marker.setPosition(new LatLng(-34, 150));
//                }
//            }
//        };
//
//        Thread thread = new Thread(runnable);
//        thread.start();
    }

    private void setPosition() {
        // TODO: get gps position here
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));
    }

    private void setStyle() {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    private void setIcons() {
        List<Map<String, AttributeValue>> itemList = dynamoDB.queryTable("cars");

        List<String> cars = itemList.stream()
                .filter(e -> e.get("party").getS().equals(state.party))
                .map(e -> e.get("car").getS())
                .collect(Collectors.toList());

        List<String> drivers = itemList.stream()
                .filter(e -> e.get("party").getS().equals(state.party))
                .map(e -> e.get("driver").getS())
                .collect(Collectors.toList());

        // TODO: implement this
        List<String> positions = itemList.stream()
                .filter(e -> e.get("party").getS().equals(state.party))
                .map(e -> e.get("position").getS())
                .collect(Collectors.toList());

        // TODO: implement me
        List<String> colors = itemList.stream()
                .filter(e -> e.get("party").getS().equals(state.party))
                .map(e -> e.get("color").getS())
                .collect(Collectors.toList());

        if (cars.size() != drivers.size())
            throw new RuntimeException("Error in our DynamoDB cars table. |drivers| != |cars|");

        for (int i = 0; i < cars.size(); ++i) {
            int color = getCarColor(colors.get(i));

            BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(color, null);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            Bitmap smallCar = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

            // TODO: potentially add occupants to the snippet as well
            markers.put(
                    cars.get(i),
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(-34, 150 + i / 10))
                            .title(cars.get(i))
                            .snippet("Driver: " + drivers.get(i))
                            .icon(BitmapDescriptorFactory.fromBitmap(smallCar)))
            );
        }
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
}
