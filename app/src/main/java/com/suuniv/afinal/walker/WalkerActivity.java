package com.suuniv.afinal.walker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.suuniv.afinal.R;
import com.suuniv.afinal.UserProfile;

import java.util.HashMap;
import java.util.UUID;

public class WalkerActivity extends AppCompatActivity {

    private static final int REQUEST_FOR_CAMERA=0011;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int OPEN_FILE=0012;
    private Uri imageUri=null;
    EditText uname;
    EditText dogPref ;
    EditText price ;
    EditText phone ;
    EditText city ;
    EditText state;
    EditText zip ;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }
        System.out.println("In new activity");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference allPostsRef = database.getReference("UserProfile");
        uname =(EditText)findViewById(R.id.dg_name);

        price = (EditText)findViewById(R.id.price);
        dogPref = findViewById(R.id.dogPref);
        phone = findViewById(R.id.phone);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        zip = findViewById(R.id.zip);


        allPostsRef.orderByChild("userId").equalTo(currentUser.getUid()).addChildEventListener(new ChildEventListener()  {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                WalkerProfile userProfiles = dataSnapshot.getValue(WalkerProfile.class);
                HashMap userProfile = (HashMap) dataSnapshot.getValue();
                System.out.println("usss "+userProfile.get("name"));
                System.out.println("USP "+userProfiles.name);
                uname.setText(userProfiles.name);
                price.setText(userProfiles.price);
                dogPref.setText(userProfiles.dogPreferences);
                phone.setText(userProfiles.phone);
                city.setText(userProfiles.city);
                state.setText(userProfiles.state);
                zip.setText(userProfiles.zip);
//                imageUri=Uri.parse(userProfiles.profileImage);
                ImageView imageView = findViewById(R.id.previewImage);
                Picasso.get().load(userProfiles.getProfileImage()).into(imageView);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void Publish(View view){

        System.out.println("in change photo");
        checkPermissions();
    }
    private static final int PICK_IMAGE = 100;

    public void openGallery(View view){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "This app needs permission to access your camera and photos.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_FOR_CAMERA);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    OPEN_FILE);
        } else {
            takePhoto();
            System.out.println("in check perms else");
//            startCamera();
        }
    }
    private void takePhoto(){
        //https://developer.android.com/training/camera-deprecated/photobasics
        System.out.println("in take photo");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Intent chooser=Intent.createChooser(intent,"Select a Camera App.");
        if (intent.resolveActivity(getPackageManager()) != null) {
            System.out.println("in reolsve activity");
            startActivityForResult(chooser, REQUEST_FOR_CAMERA);}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // The Android Camera application encodes the photo in the return Intent delivered to onActivityResult()
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_CAMERA && resultCode == RESULT_OK) {
            if(imageUri==null)
            {
                Toast.makeText(this, "Error taking photo.", Toast.LENGTH_SHORT).show();
                return;
            }
//            Intent intent=new Intent(this, NewMovieActivity.class);
            ImageView imageView = findViewById(R.id.previewImage);
            Picasso.get().load(imageUri.toString()).into(imageView);
//            intent.putExtra("uri",imageUri.toString());
//            startActivity(intent);

            return;
        } else if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            ImageView imageView = findViewById(R.id.previewImage);
            Picasso.get().load(imageUri.toString()).into(imageView);
        }
    }

    public void uploadProfile(View view){


//        Intent intent = new Intent(NewMovieActivity.this, MovieMasterActivity.class);
        System.out.println("uname.getText().toString() "+uname.getText().toString());

        WalkerProfile userProfile = new WalkerProfile();
        userProfile.setName(uname.getText().toString());
        userProfile.setPrice(price.getText().toString());
        userProfile.setDogPreferences(dogPref.getText().toString());
                userProfile.setPhone(phone.getText().toString());
                userProfile.setCity(city.getText().toString());
                        userProfile.setState(state.getText().toString());
                                userProfile.setZip(zip.getText().toString());
        uploadImage(userProfile);

        Toast toast=Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT);
        toast.show();

    }
    private void uploadImage(WalkerProfile userProfile) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        final String fileNameInStorage= UUID.randomUUID().toString();
        String path="images/"+ fileNameInStorage+".jpg";
        final StorageReference imageRef=storage.getReference(path);
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(final Uri uri) {
                        System.out.println("uri la "+uri.toString());

                        String id = fileNameInStorage;

                        userProfile.setProfileImage(uri.toString());
                        userProfile.setUserId(currentUser.getUid());

                        DatabaseReference mDatabase;
// ...
                        mDatabase = FirebaseDatabase.getInstance().getReference();

                        mDatabase.child("UserProfile").child(currentUser.getUid()).setValue(userProfile);
                    }
                });
            }
        });
    }

}