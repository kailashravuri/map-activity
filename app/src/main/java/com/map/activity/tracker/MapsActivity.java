package com.map.activity.tracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import common.map.utils.MapUtils;

import static com.map.activity.tracker.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MapService mapService;
    private boolean isMaplLoaded = false;
    private Polyline line;
    private ArrayList<LatLng> routePoints;
    boolean isActivityPaused = false;
    private static final String LOG_TAG = "map";
    MapUtils _mapUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _mapUtils = new MapUtils();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        _mapUtils.askForPermission(this);
        isActivityPaused = false;
        mapService = MapService.getMapService();
        /*if(mapService.isTrackingOn){
            mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(mapService.prevLatitude,mapService.prevLongitude),new LatLng(mapService.latitude,mapService.longitude))
                    .width(5)
                    .color(Color.BLUE));
        }*/
        // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapService.latitude,mapService.longitude),17.0f));
        //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapService.latitude,mapService.longitude),17.0f));

    }
    @Override
    protected void onPause() {
        super.onPause();
        isActivityPaused = true;
    }

    /**
 * Manipulates the map once available.
 * This callback is triggered when the map is ready to be used.
 * This is where we can add markers or lines, add listeners or move the camera. In this case,
 * we just add a marker near Sydney, Australia.
 * If Google Play services is not installed on the device, the user will be prompted to install
 * it inside the SupportMapFragment. This method will only be triggered once the user has
 * installed Google Play services and returned to the app.
 */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            _mapUtils.askForPermission(this);
            //askForPermission();
        }

        mMap.setMyLocationEnabled(true);
        Location location = null;
        if(mapService.canGetLocation) {
            location = mapService.getLocation();
            Log.i("Check Log","can get location");

        }
        final LatLng latLng;
        if(location !=null) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MapsInitializer.initialize(MapsActivity.this);
            mMap.addMarker(new MarkerOptions().position(latLng));
            moveCamera(latLng);
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    isMaplLoaded = true;
                    moveCamera(latLng);
                }
            });
        }
    }

    public void moveCamera(LatLng latLng) {
        if (mMap == null || !isMaplLoaded) {
            return;
        }
        MapsInitializer.initialize(MapsActivity.this);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
    }
    public void drawLine(double lat, double lon){
        if (mMap == null) {
            return;
        }
        LatLng latLng = new LatLng(lat,lon);
        PolylineOptions pOptions = new PolylineOptions()
                .width(5)
                .color(Color.BLUE)
                .geodesic(true);
        for (int z = 0; z < routePoints.size(); z++) {
            LatLng point = routePoints.get(z);
            pOptions.add(point);
        }
        line = mMap.addPolyline(pOptions);
        routePoints.add(latLng);

    }
     // Codde Moved into Maputils Class
//    private void askForPermission() {
//        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//        ActivityCompat.requestPermissions(this, permissions,1);
//    }


}
