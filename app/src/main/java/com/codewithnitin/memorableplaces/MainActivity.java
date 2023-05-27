package com.codewithnitin.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Static ArrayLists to store place names and their coordinates
    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();

    // ArrayAdapter to populate the ListView with place names
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the SharedPreferences object
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.codewithnitin.memorableplaces", Context.MODE_PRIVATE);

        // Initialize ArrayLists to store latitude and longitude values of places
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        // Clear all ArrayLists
        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();

        try {
            // Deserialize place names, latitude and longitude values from shared preferences
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Check if there are any saved places
        if (places.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0) {

            // Check if the number of places, latitudes and longitudes are equal
            if (places.size() == latitudes.size() && places.size() == longitudes.size()) {
                // Add LatLng objects to the locations ArrayList
                for (int i = 0; i < latitudes.size(); i++) {
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                }
            }
        } else {
            // Add a default place and location
            places.add("Add a new place...");
            locations.add(new LatLng(0, 0));
        }

        // Initialize the ListView and ArrayAdapter to display place names
        ListView listView = findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);
        listView.setAdapter(arrayAdapter);

        // Launch the MapsActivity when a place is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeNumber", i);
                startActivity(intent);
            }
        });

    }
}