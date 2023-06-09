package com.suuniv.afinal;

import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.suuniv.afinal.paw.PawModel;
import com.suuniv.afinal.paw.PawProfilesRecycler;
import com.suuniv.afinal.requests.RequestInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Uri imageUri=null;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("UserProfile");
    String usetype = "";



    DatabaseReference allPostsRef2 = database.getReference("Paws");
    ChildEventListener usersRefListener;
    private List<PawModel> pawModelList;

    float numberOfDogs = 0;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstaceState){
        View v = inflater.inflate(R.layout.account_summary,container,false);





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





        //tells us if the payment is setup
        String currentUserString = currentUser.getUid();

        DatabaseReference allPostsRef = FirebaseDatabase.getInstance().getReference("BankInfo/" + currentUserString);
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TextView paymentSetUp = v.findViewById(R.id.answer);
                    paymentSetUp.setText("Yes");
                } else {
                    TextView paymentSetUp = v.findViewById(R.id.answer);
                    paymentSetUp.setText("Set up payment.");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });


        //tells us if the payment is setup
        allPostsRef = FirebaseDatabase.getInstance().getReference("Users/" + currentUserString + "/displayname");
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TextView username = v.findViewById(R.id.userName);
                    username.setText(snapshot.getValue().toString());
                } else {
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserString = currentUser.getUid();


        //tells us if they are a dog owner
        allPostsRef = FirebaseDatabase.getInstance().getReference("Users/" + currentUserString);
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    User User = snapshot.getValue(User.class);
                    TextView bankNumber=v.findViewById(R.id.paymentMethod);
                    usetype = User.getUserType().toString();




                    if(usetype.equalsIgnoreCase("DOGOWNER")){
                        System.out.println("true");
                        TextView earning = v.findViewById(R.id.earnings);
                        earning.setVisibility(View.GONE);
                    }
                    else{
                        TextView dogs = v.findViewById(R.id.dogsNumber);
                        dogs.setVisibility(View.GONE);
                        RatingBar rating = v.findViewById(R.id.movie_rating);
                        rating.setVisibility(View.GONE);

                    }
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });





        //hopefully send notification
        final FirebaseDatabase fireBaseData = FirebaseDatabase.getInstance();
        final DatabaseReference ref = fireBaseData.getReference();
        String user = currentUser.getUid();
        ref.child("WalkerRequests").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //filter so we only see our user id being added

                final Iterable<DataSnapshot> children = snapshot.getChildren();

                int money = 0;
                for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                    RequestInfo requestInfo = imageSnapshot.getValue(RequestInfo.class);
                    String userid = requestInfo.getDogwalkerUserId().toString();

                    if(userid.equalsIgnoreCase(user) &&  requestInfo.getRequestStatus().equalsIgnoreCase("Completed")){
                        money = money +40;
                        System.out.println("money");
                    }

                }
                TextView earning = v.findViewById(R.id.earnings);
                earning.setText("$" + money);
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


}
