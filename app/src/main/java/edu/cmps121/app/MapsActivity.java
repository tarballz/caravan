package edu.cmps121.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.cmps121.app.api.State;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class MapsActivity extends AppCompatActivity {
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        state = new State(this);
    }

    public void notifyFood(View view) {
        /** Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?q=restaurants"); searches all
         * restaurants near those coordinates
         */
    }

    public void notifyGas(View view) {

    }

    public void notifyRest(View view) {

    }

    public void notifyEmergency(View view) {

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
}
