package com.example.rgome.bikestripapp.AppContent;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rgome.bikestripapp.MainActivity;
import com.example.rgome.bikestripapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class FetchDataWeather extends AsyncTask<Void, Void, Void> {
    //Variables
    String data = "";

    @Override
    protected Void doInBackground(Void... voids) {
        //Connect JSON to fetch data
        try {
            //Get data using Latitude and Longitude
            String stationLat =  (String) MainActivity.posLatitude.get(Stations.posLatLng).toString();
            String stationLng =  (String) MainActivity.posLongitude.get(Stations.posLatLng).toString();

            String encodedLatitude = URLEncoder.encode((String) stationLat, "UTF-8");
            String encodedLongitude = URLEncoder.encode((String) stationLng, "UTF-8");

            ////Connect with JSON
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + encodedLatitude + "&lon=" + encodedLongitude + "&APPID=4803ebf1908b90e03d4a3d8306cd7ff3&units=metric");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line = "";

            while(line != null){
                line = bufferedReader.readLine();
                data = data + line;
            }

            //Get everything from JSON
            JSONObject JO = new JSONObject(data);

            //Get info about weather
            JSONArray JAWeather = (JSONArray) JO.getJSONArray("weather");
            JSONObject JOMain = (JSONObject) JO.getJSONObject("main");
            JSONObject JOWind = (JSONObject) JO.getJSONObject("wind");

            JSONObject JOWeather = (JSONObject) JAWeather.getJSONObject(0);

            Stations.weatherInfo.add("\n" + MainActivity.locations.get(Stations.posLatLng) + "\n\nWeather: " + JOWeather.getString("main") + "\nDescription: " + JOWeather.getString("description") + "\nTemperature: "+ JOMain.get("temp") + "°C" + "\nTemperature Min: "+ JOMain.get("temp_min") + "°C"  + "\nTemperature Max: "+ JOMain.get("temp_max") + "°C"  + "\nWind Speed: "+ JOWind.get("speed") + "m/s\n");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    //Send fetched data to MainActivity
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
