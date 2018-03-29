package com.example.rgome.bikestripapp.AppContent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rgome.bikestripapp.MainActivity;
import com.example.rgome.bikestripapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Attributes
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lasKnowLocation;
    private Location lasKnowLocationStart;
    private Location lasKnowLocationFinish;
    private static final float ZOOM = 15f;
    private Button btnStartTrack;
    private Button btnFinishTrack;
    public static String location;
    public static String locStart;
    public static String locFinish;
    public static double startLat;
    public static double startLng;
    public static double finishLat;
    public static double finishLng;
    private TextView txtReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        btnStartTrack = (Button) findViewById(R.id.btnStartTack);
        btnFinishTrack = (Button) findViewById(R.id.btnFinishTack);
        txtReady = (TextView) findViewById(R.id.txtReady);

        showStartTrack();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //Makes Start track button invisible
    public void showStartTrack() {
        btnStartTrack.setVisibility(View.INVISIBLE);
        btnFinishTrack.setVisibility(View.INVISIBLE);
    }

    //Makes Finish track button Visible
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

                btnStartTrack.setVisibility(View.VISIBLE);

                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                lasKnowLocationStart = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                lasKnowLocationFinish = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //Set user's location start and finish track
                startLat = lasKnowLocationStart.getLatitude();
                startLng = lasKnowLocationStart.getLongitude();
                finishLat = lasKnowLocationFinish.getLatitude();
                finishLng = lasKnowLocationFinish.getLongitude();

                LatLng locStartFinish = new LatLng(lasKnowLocation.getLatitude(), lasKnowLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(locStartFinish));

                txtReady.setText("Ready to Go!!!");

                //Draw Polyline between start and finish track
                mMap.addPolyline(new PolylineOptions().add(
                        new LatLng(startLat, startLng),
                        new LatLng(finishLat, finishLng)
                ).width(10).color(Color.BLACK));

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

        //Start track
        btnStartTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideStartTrack();
                getAddress(startLat, startLng);
                locStart = location;
            }
        });

        //Finish Track
        btnFinishTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartTrack();
                getAddress(finishLat, finishLng);
                locFinish = location;

                new CountDownTimer(2000, 2000){
                    public void onTick(long millisecondsUntilDone){
                        Toast.makeText(MapsActivity.this, "Wait a second", Toast.LENGTH_SHORT).show();
                    }

                    @SuppressLint("SetTextI18n")
                    public void onFinish() {
                        startActivity(new Intent(MapsActivity.this, TrackAddInfo.class));
                    }
                }.start();
            }
        });
    }

    //Take latitude and longitude and transform in address
    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String   add = obj.getAddressLine(0);
            String  currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();

            double   latitude = obj.getLatitude();
            double longitude = obj.getLongitude();
            String currentCity= obj.getSubAdminArea();
            String currentState= obj.getAdminArea();
            /*add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();*/

            location = add;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
