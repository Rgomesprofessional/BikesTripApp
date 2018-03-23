package com.example.rgome.bikestripapp.AppContent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rgome.bikestripapp.R;

public class TrackAddInfo extends AppCompatActivity {
    private TextView txtTest1;
    private TextView txtTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_add_info);

        txtTest1 = (TextView) findViewById(R.id.txtTest1);
        txtTest2 = (TextView) findViewById(R.id.txtTest2);

        txtTest1.setText(MapsActivity.startLat + "sssssss" + MapsActivity.startLng);
        txtTest2.setText(MapsActivity.finishLat + "fffffff" + MapsActivity.finishLng);
    }
}
