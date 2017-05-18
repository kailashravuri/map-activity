package com.map.activity.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapListActivity extends AppCompatActivity implements RecyclerViewAdapter.ClickListener {
    MapUtils mapUtils;
    ArrayList<ListData> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list);
        mapUtils = MapUtils.getInstance();
        //ListView listView = (ListView) findViewById(R.id.maplist);
        RecyclerView listView = (RecyclerView) findViewById(R.id.recyclerview);
        TextView textView = (TextView) findViewById(R.id.listinfo);

        arrayList = mapUtils.getAllRouteList();
        if (arrayList != null) {
            textView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            // ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
            RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(arrayList);
            recyclerViewAdapter.setOnItemClickListener(this);
            listView.setHasFixedSize(true);
            listView.setAdapter(recyclerViewAdapter);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            listView.setLayoutManager(llm);
            /*listView.addO
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<LatLng> list;
                    list = mapUtils.getRouteValues(arrayList.get(position));
                    Log.i("Check Log", "in list activity" + list.size());

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("routevalues", list);
                    Intent intent = new Intent(MapListActivity.this, MapsActivity.class);
                    intent.putExtra("extras", bundle);
                    startActivity(intent);
                }
            });*/
        } else {
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(int position, View v) {
        ArrayList<LatLng> list;
        list = mapUtils.getRouteValues(arrayList.get(position).getRouteName());
        Log.i("Check Log", "in list activity" + list.size());

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("routevalues", list);
        Intent intent = new Intent(MapListActivity.this, MapsActivity.class);
        intent.putExtra("extras", bundle);
        startActivity(intent);

    }
}
