package com.example.rgome.bikestripapp.AppContent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.rgome.bikestripapp.R;

public class About extends AppCompatActivity {

    //Attributes
    private TextView txtAbout1;
    private TextView txtAbout2;
    private TextView txtAbout3;
    private TextView txtAbout4;
    private TextView txtAbout5;
    private TextView txtAbout6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //Get widgets
        txtAbout1 = (TextView) findViewById(R.id.txtAbout1);
        txtAbout2 = (TextView) findViewById(R.id.txtAbout2);
        txtAbout3 = (TextView) findViewById(R.id.txtAbout3);
        txtAbout4 = (TextView) findViewById(R.id.txtAbout4);
        txtAbout5 = (TextView) findViewById(R.id.txtAbout5);
        txtAbout6 = (TextView) findViewById(R.id.txtAbout6);
    }
}
