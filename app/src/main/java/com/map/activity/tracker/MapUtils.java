package com.map.activity.tracker;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class MapUtils {
    private boolean isTrackingOn = false;
    private boolean isActivityPaused = false;
    private static MapSqliteHelper mapSqliteHelper;
    private Location location, startTrackLoc, endTrackLoc;
    private String routeName;
    private static MapUtils mapUtils;
    private JSONObject jsonObject;
    private Context context;
    private MapService mapService;
    private String startTime;
    private String endTime;

    public void initializeDB(Context con) {
        context = con;
        mapSqliteHelper = new MapSqliteHelper(con);
    }

    public static MapUtils getInstance() {
        if (mapUtils == null) {
            mapUtils = new MapUtils();
        }
        return mapUtils;
    }

    public void setTrackingOn(boolean value) {
        isTrackingOn = value;
    }

    public boolean getTrackingOn() {
        return isTrackingOn;
    }

    public ArrayList<ListData> getAllRouteList() {
        Cursor cursor = mapSqliteHelper.getAllRouteNames();
        ArrayList<ListData> list = new ArrayList<>();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ListData data = new ListData(cursor.getString(cursor.getColumnIndex(MapSqliteHelper.COLUMN_ROUTENAME)),
                        cursor.getString(cursor.getColumnIndex(MapSqliteHelper.COLUMN_STARTTIME)),
                        cursor.getString(cursor.getColumnIndex(MapSqliteHelper.COLUMN_ENDTIME)));
                list.add(data);
                cursor.moveToNext();
            }
            return list;
        }

        return null;
    }

    public boolean isStartEndLocationSame() {
        return (getStartTrackLoc().getLatitude() == getEndTrackLoc().getLatitude()) && getStartTrackLoc().getLongitude() == getEndTrackLoc().getLongitude();
    }

    public void setCurrentLocation(Location l) {
        this.location = l;
    }

    public Location getLocation() {
        return location;
    }

    public void setStartTrackLocation(Location l) {
        startTrackLoc = l;
    }

    public void setEndTrackLocation(Location endLoc) {
        endTrackLoc = endLoc;
    }

    private Location getEndTrackLoc() {
        return endTrackLoc;
    }

    private Location getStartTrackLoc() {
        return startTrackLoc;
    }

    public void setStartTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        startTime = dateFormat.format(cal.getTime());
    }

    private String getStartTime() {
        return startTime;
    }

    public void setEndTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        endTime = dateFormat.format(cal.getTime());
    }

    private String getEndTime() {
        return endTime;
    }


    public void setActivityPaused(boolean value) {
        isActivityPaused = value;
    }

    public boolean isActivityPaused() {
        return isActivityPaused;
    }

    public void setRoutename(String s) {
        routeName = s;
    }

    private String getRouteName() {
        return routeName;
    }

    public void insertValuestoDB() {
        ContentValues values = new ContentValues();
        values.put(MapSqliteHelper.COLUMN_ROUTENAME, getRouteName());
        values.put(MapSqliteHelper.COLUMN_PATHVALUES, getPathValuesString());
        values.put(MapSqliteHelper.COLUMN_STARTTIME, getStartTime());
        values.put(MapSqliteHelper.COLUMN_ENDTIME, getEndTime());
        mapSqliteHelper.insert(values);
        setStartTrackLocation(null);
        setEndTrackLocation(null);
    }

    public ArrayList<LatLng> getRouteValues(String s) {
        Bundle bundle = new Bundle();
        ArrayList<LatLng> latLngs = new ArrayList<>();
        String jsonString;
        Cursor cursor = mapSqliteHelper.getRouteVales(s);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            jsonString = cursor.getString(cursor.getColumnIndex(MapSqliteHelper.COLUMN_PATHVALUES));

            try {
                JSONObject json = new JSONObject(jsonString);
                JSONArray jsonArray = json.getJSONArray("pathvalues");
                for (int i = 0; i < jsonArray.length(); i++) {
                    String latLngStr = jsonArray.getString(i);
                    latLngs.add(stringToLatlng(latLngStr));

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return latLngs;
    }

    public void savePathValuesToJson(ArrayList<LatLng> points) throws JSONException {
        jsonObject = new JSONObject();
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < points.size(); i++) {
            list.add(latlngToString(points.get(i)));
        }
        jsonObject.put("pathvalues", new JSONArray(list));
    }

    private String getPathValuesString() {
        String s = jsonObject.toString();
        jsonObject = null;
        return s;
    }

    public String latlngToString(LatLng latLng) {
        String s = latLng.latitude + "/" + latLng.longitude;
        return s;
    }

    private LatLng stringToLatlng(String s) {
        String[] str = s.split("/");
        double latitude = Double.parseDouble(str[0]);
        double longitude = Double.parseDouble(str[1]);
        return new LatLng(latitude, longitude);
    }

    public void askForPermission(MapsActivity context) {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(context, permissions, 1);
    }

    public void setService(MapService service) {
        mapService = service;
    }

    public MapService getService() {
        return mapService;
    }
}
