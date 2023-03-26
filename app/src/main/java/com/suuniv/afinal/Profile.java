package com.suuniv.afinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.suuniv.afinal.paw.PawProfile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Profile extends AppCompatActivity {

    private static final int REQUEST_FOR_CAMERA=0011;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int OPEN_FILE=0012;
    private Uri imageUri=null;
    EditText uname;
    EditText dob ;
    EditText addressline1 ;
    EditText addressline2 ;
    EditText city ;
    EditText state;
    EditText zip ;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Executor executor = Executors.newSingleThreadExecutor();
    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    PreviewView mPreviewView;
    ImageView captureImage;
    Button clic ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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

        uname =(EditText)findViewById(R.id.u_name);

        dob = (EditText)findViewById(R.id.dob);
        addressline1 = findViewById(R.id.addressLine1);
        addressline2 = findViewById(R.id.addressLine2);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        zip = findViewById(R.id.zip);


        allPostsRef.orderByChild("userId").equalTo(currentUser.getUid()).addChildEventListener(new ChildEventListener()  {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                UserProfile userProfiles = dataSnapshot.getValue(UserProfile.class);
                HashMap userProfile = (HashMap) dataSnapshot.getValue();
                System.out.println("usss "+userProfile.get("name"));
                System.out.println("USP "+userProfiles.name);
                uname.setText(userProfiles.name);
                dob.setText(userProfiles.dob);
                addressline1.setText(userProfiles.addressline1);
                addressline2.setText(userProfiles.addressline2);
                city.setText(userProfiles.city);
                state.setText(userProfiles.state);
                zip.setText(userProfiles.zip);
                imageUri=Uri.parse(userProfiles.profileImage);
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

        UserProfile userProfile = new UserProfile(uname.getText().toString(),dob.getText().toString(),
                addressline1.getText().toString(),addressline2.getText().toString(),
                city.getText().toString(),state.getText().toString()
                ,zip.getText().toString());
        uploadImage(userProfile);

        Toast toast=Toast.makeText(this,"Profile Updated",Toast.LENGTH_SHORT);
        toast.show();

    }
    private void uploadImage(UserProfile userProfile) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        final String fileNameInStorage= UUID.randomUUID().toString();
        String path="images/"+ fileNameInStorage+".jpg";
        final StorageReference imageRef=storage.getReference(path);
        if(imageUri==null){
            Toast toast=Toast.makeText(this,"Please add Photo",Toast.LENGTH_SHORT);
            toast.show();
        }else{
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
//                            userProfile.setUserType(currentUser.get);

                            DatabaseReference mDatabase;
// ...
                            mDatabase = FirebaseDatabase.getInstance().getReference();

                            mDatabase.child("UserProfile").child(currentUser.getUid()).setValue(userProfile);
                            startActivity(new Intent(getApplicationContext(),PawProfile.class));
                        }
                    });
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_drawer, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("onoption "+item.getItemId());
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signout:
                mAuth.signOut();
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    private void startCamera() {
//
//        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//
//        cameraProviderFuture.addListener(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
//                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                    bindPreview(cameraProvider);
//
//                } catch (ExecutionException | InterruptedException e) {
//                    // No errors need to be handled for this Future.
//                    // This should never be reached.
//                }
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }
//
//    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
//
//        Preview preview = new Preview.Builder()
//                .build();
//
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .build();
//
////        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
////                .build();
////
//        ImageCapture.Builder builder = new ImageCapture.Builder();
////
////        //Vendor-Extensions (The CameraX extensions dependency in build.gradle)
////        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
////
////        // Query if extension is available (optional).
////        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
////            // Enable the extension if available.
////            hdrImageCaptureExtender.enableExtension(cameraSelector);
////        }
////
//        final ImageCapture imageCapture = builder
//                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
//                .build();
//        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());
//        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);
//
//        clic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
//                File file = new File(getBatchDirectoryName(), mDateFormat.format(new Date())+ ".jpg");
//
//                ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
//                imageCapture.takePicture(outputFileOptions, executor, new ImageCapture.OnImageSavedCallback () {
//                    @Override
//                    public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
//                        new Handler().post(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(Profile.this, "Image Saved successfully", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                    @Override
//                    public void onError(@NonNull ImageCaptureException error) {
//                        error.printStackTrace();
//                    }
//                });
//            }
//        });
//    }
//
//    public String getBatchDirectoryName() {
//
//        String app_folder_path = "";
//        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/images";
//        File dir = new File(app_folder_path);
//        if (!dir.exists() && !dir.mkdirs()) {
//
//        }
//
//        return app_folder_path;
//    }

//    private boolean allPermissionsGranted(){
//
//        for(String permission : REQUIRED_PERMISSIONS){
//            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
//                return false;
//            }
//        }
//        return true;
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if(requestCode == REQUEST_CODE_PERMISSIONS){
//            if(allPermissionsGranted()){
//                startCamera();
//            } else{
//                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
//                this.finish();
//            }
//        }
//    }
}