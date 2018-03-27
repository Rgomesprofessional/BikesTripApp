package com.example.rgome.bikestripapp.AppContent;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rgome.bikestripapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TrackAddInfo extends AppCompatActivity {

    private TextView txtLocStart;
    private TextView txtLocFinish;
    private EditText eTxtTitle;
    private TextView txtDate;
    private EditText eTxtDescription;
    private Button btnSendDataFirebase;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private String userId;
    private Button btnGallery;
    private Button btnTakePicture;
    private ImageView imgPic;
    private Uri imgUri;

    private static final int GALLERY_INTENT = 2;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;

  //  @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_add_info);

        txtLocStart = (TextView) findViewById(R.id.txtLocStart);
        txtLocFinish = (TextView) findViewById(R.id.txtLocFinish);
        txtDate = (TextView) findViewById(R.id.txtDate);
        eTxtTitle = (EditText) findViewById(R.id.eTxtTitle);
        eTxtDescription = (EditText) findViewById(R.id.eTxtDescription);
        btnSendDataFirebase = (Button) findViewById(R.id.btnSendDataFirebase);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
      //  imgPic = (ImageView) findViewById(R.id.imgPic);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(userId);
        mStorage = FirebaseStorage.getInstance().getReference(FB_DATABASE_PATH);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        //txtLocation.setText("bla lba");
        txtLocStart.setText(MapsActivity.locStart);
        txtLocFinish.setText(MapsActivity.locFinish);
        txtDate.setText(formattedDate);


        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Intent intent = new Intent(Intent.ACTION_PICK);
             //   intent.setType("image/*");
             //   startActivityForResult(intent, GALLERY_INTENT);

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });

        btnSendDataFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!eTxtTitle.getText().toString().equals("") && !eTxtDescription.getText().toString().equals("") && imgUri != null){
                    final ProgressDialog dialog = new ProgressDialog(TrackAddInfo.this);
                    dialog.setTitle("uploading image");
                    dialog.show();

                    //Get storage reference
                    StorageReference ref = mStorage.child(FB_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

                    //Add file to reference

                    ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT).show();

                            ImageUpload imageUpload = new ImageUpload(txtLocStart.getText().toString(), txtLocFinish.getText().toString(), txtDate.getText().toString(), eTxtTitle.getText().toString().trim(), eTxtDescription.getText().toString().trim(),taskSnapshot.getDownloadUrl().toString());

                            //Save image info in to firebase database
                            String uploadId = mDatabase.push().getKey();
                            mDatabase.child(uploadId).setValue(imageUpload);

                            startActivity(new Intent(TrackAddInfo.this, FavoriteTrips.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();

                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Upload " + (int)progress + "%");
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Please you need a title, description and image", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /*
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

            }
        });

        btnGallery = (Button) findViewById(R.id.btnGallery);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);

        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            mProgressDialog.setMessage("Uploading...");
            mProgressDialog.show();

            Uri uri = data.getData();
            System.out.println("fffffffffffffffffffffffffff" + uri);
            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(TrackAddInfo.this, "Upload done.", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            });
        }
*/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            imgUri = data.getData();

           /* try{
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                imgPic.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }*/
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
