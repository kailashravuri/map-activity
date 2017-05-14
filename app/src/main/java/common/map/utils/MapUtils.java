package common.map.utils;

import android.Manifest;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by RavuriV on 14/05/2017.
 */

public class MapUtils {
    private final int _requestCode = 1;
    public void askForPermission(Activity activity) {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(activity, permissions, _requestCode);
    }

    public void moveCamera(GoogleMap googleMap, double lat, double lon){
        LatLng latLng = new LatLng(lat,lon);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17.0f));
    }
    public void drawLine(GoogleMap googleMap,double lat, double lon, ArrayList<LatLng> routePoints, Polyline line){
        LatLng latLng = new LatLng(lat,lon);
        PolylineOptions pOptions = new PolylineOptions()
                .width(5)
                .color(Color.BLUE)
                .geodesic(true);
        for (int z = 0; z < routePoints.size(); z++) {
            LatLng point = routePoints.get(z);
            pOptions.add(point);
        }
        line = googleMap.addPolyline(pOptions);
        routePoints.add(latLng);

    }
}
