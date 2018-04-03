package com.example.rgome.bikestripapp.AppContent;

//15761 David Kenny//


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class TrackAddInfo extends AppCompatActivity {

    //Attributes
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
    private String mCurrentPhotoPath;
    private Boolean picSwitch;

    static final int REQUEST_TAKE_PHOTO = 1;
    public static final String FB_STORAGE_PATH = "image/";
    public static final String FB_DATABASE_PATH = "image";
    public static final int REQUEST_CODE = 1234;

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

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        userId = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(userId);
        mStorage = FirebaseStorage.getInstance().getReference(FB_DATABASE_PATH);

        //Format date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);

        txtLocStart.setText(MapsActivity.locStart);
        txtLocFinish.setText(MapsActivity.locFinish);
        txtDate.setText(formattedDate);

        //Send info to Firebase
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

        //Open gallery to select picture
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picSwitch = false;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select image"), REQUEST_CODE);
            }
        });

        //Take picture
        btnTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picSwitch = true;
                dispatchTakePictureIntent();
            }
        });
    }

    //Create file to picture taken
    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir =  getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println("xxxxxxxxx" + mCurrentPhotoPath);
        return image;
    }

    //Take picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileproviderBikesTripApp",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //Add picture to gallery
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(picSwitch){
            galleryAddPic();
        } else {

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imgUri = data.getData();
            }
        }
    }

    public String getImageExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}
