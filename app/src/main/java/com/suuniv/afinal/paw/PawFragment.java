package com.suuniv.afinal.paw;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.suuniv.afinal.R;
import com.suuniv.afinal.UserProfile;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PawFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private static final int REQUEST_FOR_CAMERA=0011;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int OPEN_FILE=0012;
    private Uri imageUri=null;

    public PawFragment() {
        // Required empty public constructor
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PawFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PawFragment newInstance(String param1, String param2) {
        PawFragment fragment = new PawFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    ImageView imageView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_paw, container, false);

         imageView = view.findViewById(R.id.dogpreviewImage);


        Button add = view.findViewById(R.id.addPaw);

      EditText vac =  view.findViewById(R.id.vaccinations);
      EditText breed=  view.findViewById(R.id.breed);
       EditText  quirks= view.findViewById(R.id.quirks);

      EditText name=  view.findViewById(R.id.paw_name);
      EditText age = view.findViewById(R.id.age);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ADD AWWW");
                PawModel profile = new PawModel();
                profile.setAge(age.getText().toString());
                profile.setBreed(breed.getText().toString());
                profile.setPaw_name(name.getText().toString());
                profile.setQuirks(quirks.getText().toString());
                profile.setVaccinations(vac.getText().toString());
                profile.setUserId(currentUser.getUid());
                uploadImage(profile);
            }
        });

        Button takepwpic = view.findViewById(R.id.takepwpic);

        takepwpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePawPicture(v);
            }
        });

        Button openega = view.findViewById(R.id.openGallery);

        openega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPawGallery(v);
            }
        });


       return view;

    }

    public void takePawPicture(View view){

        System.out.println("in change photo");
        checkPermissions();
    }
    private static final int PICK_IMAGE = 100;

    public void openPawGallery(View view){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void checkPermissions(){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "This app needs permission to access your camera and photos.", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_FOR_CAMERA);
            ActivityCompat.requestPermissions(getActivity(),
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
        imageUri =getContext().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Intent chooser=Intent.createChooser(intent,"Select a Camera App.");
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            System.out.println("in reolsve activity");
            startActivityForResult(chooser, REQUEST_FOR_CAMERA);}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // The Android Camera application encodes the photo in the return Intent delivered to onActivityResult()
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_CAMERA && resultCode == -1) {
            if(imageUri==null)
            {
                Toast.makeText(getContext(), "Error taking photo.", Toast.LENGTH_SHORT).show();
                return;
            }

//            Intent intent=new Intent(this, NewMovieActivity.class);
//            ImageView imageView = findViewById(R.id.previewImage);
            Picasso.get().load(imageUri.toString()).into(imageView);
//            intent.putExtra("uri",imageUri.toString());
//            startActivity(intent);

            return;
        } else if (resultCode == -1 && requestCode == PICK_IMAGE){
            imageUri = data.getData();

            Picasso.get().load(imageUri.toString()).into(imageView);
        }
    }

    private void uploadImage(PawModel userProfile) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        final String fileNameInStorage= UUID.randomUUID().toString();
        String path="images/"+ fileNameInStorage+".jpg";
        final StorageReference imageRef=storage.getReference(path);
//        DatabaseReference mDatabase;
//// ...
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//        mDatabase.child("Paws").child(fileNameInStorage).setValue(userProfile);


        if(imageUri==null){
            Toast toast=Toast.makeText(getContext(),"Please add Photo",Toast.LENGTH_SHORT);
            toast.show();
        }else {

            imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(final Uri uri) {
                            System.out.println("uri la " + uri.toString());

                            String id = fileNameInStorage;

                            userProfile.setUrl(uri.toString());

                            userProfile.setPid(fileNameInStorage);

                            DatabaseReference mDatabase;
// ...
                            mDatabase = FirebaseDatabase.getInstance().getReference();

                            mDatabase.child("Paws").child(fileNameInStorage).setValue(userProfile);
                            Toast toast = Toast.makeText(getContext(), "Profile Added", Toast.LENGTH_SHORT);
                            toast.show();

                            startActivity(new Intent(getContext(), PawProfile.class));


                        }
                    });
                }
            });
        }
    }
}