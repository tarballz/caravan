package edu.cmps121.app;

import android.support.v7.app.AppCompatActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.cmps121.app.api.State;

/**
 * A styled map using JSON styles from a raw resource.
 */
public class MapsActivityRaw extends AppCompatActivity implements OnMapReadyCallback {
    State state;

    private static final String TAG = MapsActivityRaw.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps_raw);

        state = new State(this);

        // Get the SupportMapFragment and register for the callback
        // when the map is ready for use.
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready for use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

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
        // Position the map's camera near Sydney, Australia.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-34, 151)));

        // Attempt at marker
        Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(-34, 150))
                .title("hello world!"));

        // TODO: fix this. Make it stop after 20 seconds of alternating
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean blah = false;

                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!blah)
                        marker.setPosition(new LatLng(-34, 151));
                    else
                        marker.setPosition(new LatLng(-34, 150));
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.run();
    }
}
