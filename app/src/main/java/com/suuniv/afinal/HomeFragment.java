package com.suuniv.afinal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.suuniv.afinal.paw.PawModel;
import com.suuniv.afinal.paw.PawProfilesRecycler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Uri imageUri=null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("UserProfile");



    DatabaseReference allPostsRef2 = database.getReference("Paws");
    ChildEventListener usersRefListener;
    private List<PawModel> pawModelList;

    float numberOfDogs = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstaceState){
        View v = inflater.inflate(R.layout.account_summary,null,false);

        Button sms = v.findViewById(R.id.sms);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSMS(getContext(),"Hey there","1234321234");

            }
        });

        Button geo = v.findViewById(R.id.geoLocation);

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri ma = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway%2C+CA");
                showMap(ma);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();




        allPostsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(currentUser.getUid().equalsIgnoreCase(snapshot.child("userId").getValue().toString())) {
                    UserProfile userProfiles = snapshot.getValue(UserProfile.class);
                    HashMap userProfile = (HashMap) snapshot.getValue();
                    imageUri= Uri.parse(userProfiles.profileImage);
                    ImageView imageView = v.findViewById(R.id.image);
                    Picasso.get().load(userProfiles.getProfileImage()).into(imageView);
                }

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



        //gets the rating to tell us how many dogs there are
        pawModelList =new ArrayList<>();
        allPostsRef2.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                RatingBar rating = v.findViewById(R.id.movie_rating);
                if(currentUser.getUid().equalsIgnoreCase(snapshot.child("userId").getValue().toString())) {
                    numberOfDogs = numberOfDogs +1;
                    rating.setRating(numberOfDogs);
                }
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

        return v;
    }


    public static void openSMS(Context context, String smsBody, String tel) {
        Uri uri = Uri.parse("smsto:" + tel);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", smsBody);
        context.startActivity(it);
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
