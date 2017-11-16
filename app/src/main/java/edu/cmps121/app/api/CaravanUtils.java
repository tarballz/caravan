package edu.cmps121.app.api;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import edu.cmps121.app.R;

public class CaravanUtils {

    public static int PROXIMITY_RADIUS = 10000;

    public static void shortToast(AppCompatActivity activity, String msg) {
        Toast.makeText(
                activity,
                msg,
                Toast.LENGTH_SHORT
        ).show();
    }

    public static String strAppend(String inStr, String appStr) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(inStr);
        stringBuilder.append(appStr);

        return stringBuilder.toString();
    }

    /**
     * Return the current state of the permissions needed.
     */
    public static boolean checkPermissions(AppCompatActivity activity) {
        int permissionState = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static String getPlaceUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyDaj0E9W6fqT2mN0PiHOYKze3hVNuoVbBY");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }
}
