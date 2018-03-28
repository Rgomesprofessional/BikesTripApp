package com.example.rgome.bikestripapp.AppContent;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rgome.bikestripapp.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private ArrayList<String> detailsTrip = new ArrayList<String>();
    private DatabaseReference mDatabase;
    private ListView lVDetailsTrips;
    private String completeDetails;
    private String userId;
    private Button btnDelete;
    //private ImageView imgDisplay;
    private Uri imgUri;
    private StorageReference mStorage;


    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_favorite_trips);

        lVDetailsTrips = (ListView) findViewById(R.id.lVDetailsTrips);
        btnDelete = (Button) findViewById(R.id.btnDelete);
       // ImageView imgDisplay = (ImageView) findViewById(R.id.imgDisplay);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(userId);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, detailsTrip);

        lVDetailsTrips.setAdapter(arrayAdapter);

        detailsTrip.clear();

        mStorage = FirebaseStorage.getInstance().getReference(FB_DATABASE_PATH);
//String url = "gs://bikes-trip-app.appspot.com/image/image/1522166709469.jpg";
//Glide.with(getApplicationContext().load(url).into(imgDisplay));
        StorageReference httpsReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://bikes-trip-app.appspot.com/image/image/1522166709469.jpg");

        System.out.println(httpsReference+"ppppppppppppppppppppppppppppppppppppppp");

        // ImageView in your Activity
        //ImageView imageView = (ImageView) findViewById(R.id.imgDisplay);

// Load the image using Glide
     //   Glide.with(DetailsFavoriteTrips.this).using(new FirebaseImageLoader()).load(httpsReference).into(imgDisplay);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                   // ImageUpload img = ds.getValue(ImageUpload.class);

                    if(ds.getRef().getParent().equals(FavoriteTrips.storeReference.getRef())){
                        completeDetails =  ds.getKey() + ":  " + ds.getValue();
                        detailsTrip.add(ds.getKey() + ": " + ds.getValue());
         //   }
                      //  titleTrip =  ds.getKey() + ":  " + ds.getValue();
                  //      dataTrip.add(titleTrip);
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


    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
