package edu.cmps121.app.utilities;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import edu.cmps121.app.dynamo.Car;
import edu.cmps121.app.dynamo.DynamoDB;

import static edu.cmps121.app.utilities.CaravanUtils.trackingEnabled;

public class DriverService extends Service {

    private LocationManager locationManager;
    private LocationListener gpsLocationListener;
    private LocationListener networkLocationListener;
    private DynamoDB dynamoDB;
    private String car;

    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 1f;
    private static final String TAG = DriverService.class.getSimpleName();

    private class LocationListener implements android.location.LocationListener {
        Location lastLocation;

        LocationListener(String provider) {
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

            dynamoDB.updateItem(Car.class, car, (obj) -> {
                Car carItem = (Car) obj;
                carItem.setLat(lastLocation.getLatitude());
                carItem.setLng(lastLocation.getLongitude());
                carItem.setBearing(lastLocation.getBearing());

                dynamoDB.saveItem(carItem);
            });
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

        if (intent == null)
            this.stopSelf();
        else
            car = intent.getStringExtra("car");

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        if (!trackingEnabled(this))
            throw new RuntimeException("Location permissions not yet granted");

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        networkLocationListener = new LocationListener(LocationManager.NETWORK_PROVIDER);
        gpsLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);
        dynamoDB = new DynamoDB(this);

        instantiateListener(networkLocationListener, LocationManager.NETWORK_PROVIDER);
        instantiateListener(gpsLocationListener, LocationManager.GPS_PROVIDER);
    }

    private void instantiateListener(LocationListener listener, String provider) {
        try {
            locationManager.requestLocationUpdates(
                    provider, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    listener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, provider + " provider does not exist, " + ex.getMessage());
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

    }
}
