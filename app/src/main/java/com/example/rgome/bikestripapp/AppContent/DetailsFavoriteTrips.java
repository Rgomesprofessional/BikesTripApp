package com.example.rgome.bikestripapp.AppContent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.rgome.bikestripapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class DetailsFavoriteTrips extends AppCompatActivity {

    //Attributes
    private ArrayList<String> detailsTrip = new ArrayList<String>();
    private DatabaseReference mDatabase;
    private ListView lVDetailsTrips;
    private String completeDetails;
    private String userId;
    private Button btnDelete;
    private StorageReference mStorage;

    public static final String FB_DATABASE_PATH = "image";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_favorite_trips);

        lVDetailsTrips = (ListView) findViewById(R.id.lVDetailsTrips);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(userId);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, detailsTrip);

        lVDetailsTrips.setAdapter(arrayAdapter);

        detailsTrip.clear();

        mStorage = FirebaseStorage.getInstance().getReference(FB_DATABASE_PATH);

        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://bikes-trip-app.appspot.com/image/image/1522166709469.jpg");

        //Get data from Firebase
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getRef().getParent().equals(FavoriteTrips.storeReference.getRef())){
                        completeDetails =  ds.getKey() + ":  " + ds.getValue();
                        detailsTrip.add(ds.getKey() + ": " + ds.getValue());
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Delete data from Firebase
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {

                            if(ds.getRef().getParent().equals(FavoriteTrips.storeReference.getRef())){
                                ds.getRef().getParent().removeValue();
                                startActivity(new Intent(DetailsFavoriteTrips.this, FavoriteTrips.class));
                            }
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}
