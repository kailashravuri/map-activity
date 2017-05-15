package com.map.activity.tracker;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class MapService extends Service implements LocationListener {
    public IBinder mBinder = new MapBinder();
    private static MapService mMapService;
    private Context mContext;
    LocationManager mLocationManager;
    // Flag for GPS status
    boolean isGPSEnabled = false;
    boolean isTrackingOn = true;
    // Flag for network status
    boolean isNetworkEnabled = false;
    // Flag for GPS status
    boolean canGetLocation = false;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10; // 1 minute
    double latitude, prevLatitude;
    double longitude, prevLongitude;
    Location mLocation;
    MapsActivity mapsActivity;

    public MapService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMapService = this;
        mContext = this;
        getLocation();
        mapsActivity = new MapsActivity();
    }
    public Location getLocation() {
        try {
            mLocationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = mLocationManager
                    .isProviderEnabled(NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // TODO: No network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return null;
                    }
                    mLocationManager.requestLocationUpdates(
                            NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (mLocationManager != null) {
                        mLocation = mLocationManager
                                .getLastKnownLocation(NETWORK_PROVIDER);
                        if (mLocation != null) {
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();
                        }
                    }
                }
                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    if (mLocation == null) {
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mLocationManager != null) {
                            mLocation = mLocationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLocation != null) {
                                latitude = mLocation.getLatitude();
                                longitude = mLocation.getLongitude();
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return mLocation;
    }
    public static MapService getMapService(){
        return mMapService;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onLocationChanged(Location location) {
        if(!mapsActivity.isActivityPaused){
            prevLatitude = latitude;
            prevLongitude = longitude;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        mapsActivity.moveCamera(new LatLng(latitude, longitude));
        if(isTrackingOn && !mapsActivity.isActivityPaused){
            mapsActivity.drawLine(latitude,longitude);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class MapBinder extends Binder {
        MapService getService() {
            return MapService.this;
        }
    }
}
