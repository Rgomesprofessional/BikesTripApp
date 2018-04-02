package com.example.rgome.bikestripapp.AppContent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rgome.bikestripapp.R;

/*
JOEL CARLOS ID 15464
 */

public class Weather extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Get widgets
        ListView lvWeatherStations = (ListView) findViewById(R.id.go);

        //Create list with Weather in Bike Station
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Stations.weatherInfo);

        lvWeatherStations.setAdapter(arrayAdapter);
    }
}
