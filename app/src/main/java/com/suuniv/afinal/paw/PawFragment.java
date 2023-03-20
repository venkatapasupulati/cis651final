package com.suuniv.afinal.paw;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_paw, container, false);

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


       return view;

    }

    private void uploadImage(PawModel userProfile) {
        FirebaseStorage storage= FirebaseStorage.getInstance();
        final String fileNameInStorage= UUID.randomUUID().toString();
        String path="images/"+ fileNameInStorage+".jpg";
        final StorageReference imageRef=storage.getReference(path);
        DatabaseReference mDatabase;
// ...
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Paws").child(fileNameInStorage).setValue(userProfile);

        Toast toast=Toast.makeText(getContext(),"Profile Added",Toast.LENGTH_SHORT);
        toast.show();



//        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(final Uri uri) {
//                        System.out.println("uri la "+uri.toString());
//
//                        String id = fileNameInStorage;
//
//                        userProfile.setProfileImage(uri.toString());
//                        userProfile.setUserId(currentUser.getUid());
//
//                        DatabaseReference mDatabase;
//// ...
//                        mDatabase = FirebaseDatabase.getInstance().getReference();
//
//                        mDatabase.child("Paws").child(currentUser.getUid()).setValue(userProfile);
//                    }
//                });
//            }
//        });
    }
}