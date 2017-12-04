package edu.cmps121.app.utilities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class CaravanUtils {

    public enum JsonOptions {
        GREYSCALE, NIGHT, RETRO
    }

    public static void shortToast(AppCompatActivity activity, String msg) {
        Toast.makeText(
                activity,
                msg,
                Toast.LENGTH_SHORT
        ).show();
    }

    public static boolean isValidString(String str) {
        return str != null && !str.isEmpty();
    }

    public static void startDriverService(String car, Context context) {
        Intent serviceIntent = new Intent(context, DriverService.class);
        serviceIntent.putExtra("car", car);
        context.startService(serviceIntent);
    }

    public static void stopDriverService(Context context) {
        Intent serviceIntent = new Intent(context, DriverService.class);
        context.stopService(serviceIntent);
    }

    public static boolean trackingEnabled(Context context) {
        return ((ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED));
    }
}
