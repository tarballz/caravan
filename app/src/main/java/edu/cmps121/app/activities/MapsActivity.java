package edu.cmps121.app.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.cmps121.app.R;
import edu.cmps121.app.utilities.State;

import static edu.cmps121.app.utilities.CaravanUtils.shortToast;

public class MapsActivity extends AppCompatActivity {

    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        state = new State(this);
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

    public void mapsPage(View view) {
        state.nextActivity(this, MapsOverlayActivity.class);
    }
}
