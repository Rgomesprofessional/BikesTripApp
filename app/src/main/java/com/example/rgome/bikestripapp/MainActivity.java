package com.example.rgome.bikestripapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rgome.bikestripapp.AccountActivity.AccountSettings;
import com.example.rgome.bikestripapp.AccountActivity.Login;
import com.example.rgome.bikestripapp.AccountActivity.SignUp;
import com.example.rgome.bikestripapp.AppContent.About;
import com.example.rgome.bikestripapp.AppContent.FavoriteTrips;
import com.example.rgome.bikestripapp.AppContent.FetchDataStations;
import com.example.rgome.bikestripapp.AppContent.MapsActivity;
import com.example.rgome.bikestripapp.AppContent.Stations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Attributes
    public static Spinner selectCity;
    public static ArrayList<String> locations = new ArrayList<String>();
    public static ArrayList<String> moreInfo = new ArrayList<String>();
    public static ArrayList<Double> posLatitude = new ArrayList<Double>();
    public static ArrayList<Double> posLongitude = new ArrayList<Double>();
    private Button btnStations;
    private Button btnMap;
    private Button btnLogout;
    private Button btnAbout;
    private Button btnFavoriteTrips;

    String[] cities = {"Select City...", "Brisbane", "Bruxelles-Capitale", "Namur", "Santander", "Seville", "Valence", "Amiens", "Besancon", "Cergy-Pontoise", "Creteil", "Lyon", "Marseille", "Mulhouse", "Nancy", "Nantes", "Rouen", "Toulouse", "Dublin", "Toyama", "Vilnius", "Luxembourg", "Lillestrom", "Kazan", "Goteborg", "Lund", "Stockholm", "Ljubljana"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get widgets
        selectCity = (Spinner) findViewById(R.id.selectCity);
        btnStations = (Button) findViewById(R.id.btnStations);
        btnMap = (Button) findViewById(R.id.btnMap);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnFavoriteTrips = (Button) findViewById(R.id.btnFavoriteTrips);

        //Set cities in the spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, cities);
        selectCity.setAdapter(dataAdapter);

        //Get data from website with about the bikes (Fecth data in background)
        selectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    if(locations.size() > 0){
                        locations.clear();
                        moreInfo.clear();
                        posLatitude.clear();
                        posLongitude.clear();
                    }

                    FetchDataStations process = new FetchDataStations();
                    process.execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Show list of bike
        btnStations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, Stations.class);
            startActivity(intent);
            }
        });

        //Show map of bike locations
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(intent);
            }
        });

        //Show about
        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           Intent intent = new Intent(MainActivity.this, About.class);
           startActivity(intent);
            }
        });

        //Show about
        btnFavoriteTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FavoriteTrips.class);
                startActivity(intent);
            }
        });

        //Show about
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AccountSettings.class));
            }
        });
    }
}