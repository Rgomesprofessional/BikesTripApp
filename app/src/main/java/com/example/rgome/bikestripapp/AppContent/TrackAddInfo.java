package com.example.rgome.bikestripapp.AppContent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rgome.bikestripapp.MainActivity;
import com.example.rgome.bikestripapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TrackAddInfo extends AppCompatActivity {

    private TextView txtLocation;
    private EditText eTxtTitle;
    private TextView txtDate;
    private EditText eTxtDescription;
    private Button btnSendDataFirebase;
    private DatabaseReference mDatabase;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_add_info);

        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtDate = (TextView) findViewById(R.id.txtDate);
        eTxtTitle = (EditText) findViewById(R.id.eTxtTitle);
        eTxtDescription = (EditText) findViewById(R.id.eTxtDescription);
        btnSendDataFirebase = (Button) findViewById(R.id.btnSendDataFirebase);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(userId);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        txtLocation.setText("bla lba");
        txtDate.setText(formattedDate);

        btnSendDataFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String location = txtLocation.getText().toString().trim();
                String date = txtDate.getText().toString().trim();
                String title = eTxtTitle.getText().toString().trim();
                String description = eTxtDescription.getText().toString().trim();

                HashMap<String, String> dataMap = new HashMap<String, String>();

                dataMap.put("Location", location);
                dataMap.put("Date", date);
                dataMap.put("Title", title);
                dataMap.put("Description", description);

                String info = "\n" + location + "\n" + date + "\n\n" + title + "\n" + description + "\n";
                System.out.println("rrrrrrrrrrrr" + location + date + title + description);

              /*  mDatabase.push().setValue(location);
                mDatabase.push().setValue(date);
                mDatabase.push().setValue(title);
                mDatabase.push().setValue(description);
*/
                mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(TrackAddInfo.this, "Stored..", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(TrackAddInfo.this, FavoriteTrips.class));
                        } else {
                            Toast.makeText(TrackAddInfo.this, "Error..", Toast.LENGTH_LONG).show();
                        }
                    }
                });
               /* mDatabase.addListenerForSingleValueEvent(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(TrackAddInfo.this, "Stored..", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TrackAddInfo.this, "Error..", Toast.LENGTH_LONG).show();
                        }
                    }
                });*/
            }
        });
    }
}
