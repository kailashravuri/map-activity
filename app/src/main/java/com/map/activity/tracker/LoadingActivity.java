package com.map.activity.tracker;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        Log.i("Check Log", "oncreate");

        if (MapService.getMapService() == null) {
            Log.i("Check Log", "oncreate if");
            Intent intent = new Intent(this, MapService.class);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            Log.i("Check Log", "oncreate else");
            Intent intent = new Intent(LoadingActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }
    }

    protected ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("Check Log", "onserviceconnected");
            Intent intent = new Intent(LoadingActivity.this, MapsActivity.class);
            startActivity(intent);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
