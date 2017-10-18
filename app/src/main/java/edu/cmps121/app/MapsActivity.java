package edu.cmps121.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import static edu.cmps121.app.api.CaravanUtils.shortToast;

public class MapsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_party);
    }

    public void notifyFood(View view) {

    }

    public void notifyGas(View view) {

    }

    public void notifyRest(View view) {

    }

    public void notifyEmergency(View view) {

    }

    public void mapsRedirect(View view) {
//        String destination = "com.google.android.apps.maps";
//        Intent intent = getPackageManager().getLaunchIntentForPackage(destination);
//        if (intent != null) {
//            // We found the activity now start the activity
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        } else {
//            // Bring user to the market or let them choose an app?
//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setData(Uri.parse("market://details?id=" + destination));
//            startActivity(intent);
//        }

        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=46.414382,10.013988");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        if (mapIntent.resolveActivity(getPackageManager()) != null)
            startActivity(mapIntent);
        else
            shortToast(MapsActivity.this, "Please install Google Maps on your device");
    }
}
