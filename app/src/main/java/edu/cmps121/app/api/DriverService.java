package edu.cmps121.app.api;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import edu.cmps121.app.model.Car;

public class DriverService extends Service {
    private LocationManager locationManager;
//    LocationListener[] locationListeners = new LocationListener[]{
//            new LocationListener(LocationManager.GPS_PROVIDER),
//            new LocationListener(LocationManager.NETWORK_PROVIDER)
//    };

    private LocationListener gpsLocationListener;
    private LocationListener networkLocationListener;
    private DynamoDB dynamoDB;
    private String car;

    private static final int LOCATION_INTERVAL = 5000;
    private static final float LOCATION_DISTANCE = 10f;
    private static final String TAG = DriverService.class.getSimpleName();

    private class LocationListener implements android.location.LocationListener {
        Location lastLocation;

        public LocationListener(String provider) {
            Log.i(TAG, "New LocationListener: " + provider);
            lastLocation = new Location(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "Location updated - Lat: "
                    + location.getLatitude()
                    + " Lon: "
                    + location.getLongitude());
            lastLocation.set(location);
            Car carItem = (Car) dynamoDB.getItem(Car.class, car);

            carItem.setLat(lastLocation.getLatitude());
            carItem.setLng(lastLocation.getLongitude());

            dynamoDB.saveItem(carItem);
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        car = intent.getStringExtra("car");
        Log.i(TAG, "onStartCommand intent car: " + car);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        if (!(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED))
            throw new RuntimeException("Location permissions not yet granted");

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        networkLocationListener = new LocationListener(LocationManager.NETWORK_PROVIDER);
        gpsLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);
        dynamoDB = new DynamoDB(this);

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    gpsLocationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    networkLocationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(gpsLocationListener);
                locationManager.removeUpdates(networkLocationListener);
            }
        } catch (Exception e) {
            Log.i(TAG, "Failed to removed location listeners");
        }


//        if (locationManager != null) {
//            for (int i = 0; i < locationListeners.length; i++) {
//                try {
//                    locationManager.removeUpdates(locationListeners[i]);
//                } catch (Exception ex) {
//                    Log.i(TAG, "fail to remove location listners, ignore", ex);
//                }
//            }
//        }
    }
}
