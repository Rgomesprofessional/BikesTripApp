package com.example.rgome.bikestripapp.AppContent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rgome.bikestripapp.MainActivity;
import com.example.rgome.bikestripapp.R;

import java.util.ArrayList;

public class Stations extends AppCompatActivity {
    public static ArrayList<String> weatherInfo = new ArrayList<String>();
    public static Integer posLatLng;
    private ListView lvStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);

        //Get widgets
        lvStations = (ListView) findViewById(R.id.lvStations);

        //Create list with bike stations
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.moreInfo);

        lvStations.setAdapter(arrayAdapter);

        //When item from list is clicked weather info is displayed
        lvStations.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                weatherInfo.clear();
                posLatLng = position;

                FetchDataWeather process = new FetchDataWeather();
                process.execute();

                new CountDownTimer(2000, 2000) {
                    public void onTick(long millisecondsUntilDone){
                        Toast.makeText(Stations.this, "Wait a second", Toast.LENGTH_SHORT).show();
                    }

                    @SuppressLint("SetTextI18n")
                    public void onFinish() {
                        Intent intent = new Intent(Stations.this, Weather.class);
                        startActivity(intent);
                    }
                }.start();
            }
        });
    }
}

