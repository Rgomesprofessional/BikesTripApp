package com.example.rgome.bikestripapp.AppContent;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.example.rgome.bikestripapp.MainActivity;
import com.example.rgome.bikestripapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lasKnowLocation;
    private static final float ZOOM = 15f;
    private Button btnStartTrack;
    private Button btnFinishTrack;
    public static double startLat;
    public static double startLng;
    public static double finishLat;
    public static double finishLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnStartTrack = (Button) findViewById(R.id.btnStartTack);
        btnFinishTrack = (Button) findViewById(R.id.btnFinishTack);


        showStartTrack();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void showStartTrack() {
        btnStartTrack.setVisibility(View.VISIBLE);
        btnFinishTrack.setVisibility(View.INVISIBLE);
    }

    public void hideStartTrack() {
        btnStartTrack.setVisibility(View.INVISIBLE);
        btnFinishTrack.setVisibility(View.VISIBLE);
    }
    //Request permission from user
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //Keep updating user's location
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
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
        };

        //Request permission from user if Android version is old or not
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                lasKnowLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //Set user's location
                LatLng userLocation = new LatLng(lasKnowLocation.getLatitude(), lasKnowLocation.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, ZOOM));
            }
        }


        // Add a marker in bike stations
        for(int i = 0; i < MainActivity.locations.size(); i++){
            LatLng bikeStations = new LatLng(MainActivity.posLatitude.get(i), MainActivity.posLongitude.get(i));
            mMap.addMarker(new MarkerOptions().position(bikeStations).title(MainActivity.locations.get(i)));
        }

        btnStartTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLat = lasKnowLocation.getLatitude();
                startLng = lasKnowLocation.getLongitude();

                hideStartTrack();
            }
        });

        btnFinishTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishLat = lasKnowLocation.getLatitude();
                finishLng = lasKnowLocation.getLongitude();

                showStartTrack();
                startActivity(new Intent(MapsActivity.this, TrackAddInfo.class));
            }
        });

    }
}
