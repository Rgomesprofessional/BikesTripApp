package com.example.rgome.bikestripapp.AppContent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.rgome.bikestripapp.MainActivity;
import com.example.rgome.bikestripapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteTrips extends AppCompatActivity {

    public static ArrayList<DatabaseReference> dataReference = new ArrayList<DatabaseReference>();
    public static ArrayList<DatabaseReference> bu = new ArrayList<DatabaseReference>();
    public static DatabaseReference storeReference;
    public static DatabaseReference re;
    private int dataPosition;
    private ListView lVFavorites;
    private DatabaseReference mDatabase;
    private ArrayList<String> dataTrip = new ArrayList<String>();
    private String titleTrip;
    private String userId;
    private Button btnMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_trips);

        lVFavorites = (ListView) findViewById(R.id.lVFavorites);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(userId);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataTrip);

        lVFavorites.setAdapter(arrayAdapter);

        dataReference.clear();

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //System.out.println("qqqqqqqqqqqqqqqqqqqq" + storeReference +"tt" +ds.get);

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals("Title")){
                        titleTrip =  ds.getKey() + ":  " + ds.getValue();
                        dataTrip.add(titleTrip);
                        dataReference.add(dataSnapshot.getRef());
                      //  bu.add(dataSnapshot.getRef());
                    }
                   // test =  ds.getKey();
                 //   test2.add(test);
                //    System.out.println(test+"rrrrrrrrrrr");

                    //String name = ds.child("name").getValue(String.class);
                  //  Log.d("TAG", address + " / " + name);
                }

                arrayAdapter.notifyDataSetChanged();
               // for(DataSnapshot items : dataSnapshot.getChildren()){

                //    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                   // test = dataSnapshot.getValue(String.class).toString();
                // test = dataSnapshot.child(userId).child("Title").getValue(String.class);
               //     test2.add(test);
                 //   System.out.println(test+"rrrrrrrrrrr");
               //     arrayAdapter.notifyDataSetChanged();
              //  }
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

        System.out.println(dataReference.size());

        lVFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
              //  dataReference = ds.getRef();

                storeReference = dataReference.get(position);
                startActivity(new Intent(FavoriteTrips.this, DetailsFavoriteTrips.class));

            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FavoriteTrips.this, MainActivity.class));
            }
        });
    }
}
