package com.suuniv.afinal.walker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suuniv.afinal.MainActivity;
import com.suuniv.afinal.NavigationDrawer;
import com.suuniv.afinal.R;
import com.suuniv.afinal.User;
import com.suuniv.afinal.paw.PawModel;
import com.suuniv.afinal.paw.PawProfilesRecycler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WalkerPagerActivity extends AppCompatActivity {

    WalkerViewPagerAdapter walkerViewPagerAdapter;
    ViewPager2 mViewPager;
    Toolbar toolbar;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userProfileRef = database.getReference("UserProfile");

    private List<WalkerProfile> md_list;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_pager);

        toolbar=(Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        System.out.println("in MOVIE RECYCLER");
        md_list = new ArrayList<>();



        userProfileRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                WalkerProfile walkerProfile = new WalkerProfile();

                System.out.println("in userprofiles walker "+snapshot.child("name").getValue().toString());
                walkerProfile.setName(snapshot.child("name").getValue().toString());
                walkerProfile.setState(snapshot.child("state").getValue().toString());
                walkerProfile.setProfileImage(snapshot.child("profileImage").getValue().toString());
                walkerProfile.setZip(snapshot.child("zip").getValue().toString());
                walkerProfile.setCity(snapshot.child("city").getValue().toString());
                if(snapshot.child("price").getValue()!=null){
                    walkerProfile.setPrice(snapshot.child("price").getValue().toString());
                }
                if(snapshot.child("phone").getValue()!=null){
                    walkerProfile.setPhone(snapshot.child("phone").getValue().toString());
                }

                String utype="";
               if(snapshot.child("userType").getValue()!=null){
                   utype=snapshot.child("userType").getValue().toString();
               }
               if(utype.equalsIgnoreCase("DOGWALKER")){
                   md_list.add(walkerProfile);
               }



                System.out.println("md_list "+md_list.size());
                walkerViewPagerAdapter =new WalkerViewPagerAdapter(WalkerPagerActivity.this,md_list);
                mViewPager = (ViewPager2) findViewById(R.id.pager);
                mViewPager.setAdapter(walkerViewPagerAdapter);

                //connect the tab layout with the view pager https://developer.android.com/guide/navigation/navigation-swipe-view-2
                TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
                new TabLayoutMediator(tabLayout, mViewPager,
                        (tab, position) -> tab.setText(md_list.get(position).getName())
                ).attach();


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("on child removed "+snapshot.getKey());
//                for (int i = 0; i < md_list.size(); i++) {
//                    if(md_list.get(i).getPid().equals(snapshot.getKey())) {
//                        pawModelList.remove(i);
//                        notifyItemRemoved(i);
//                        break;
//                    }
//                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        md_list = new ArrayList<>();

        userProfileRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                WalkerProfile walkerProfile = new WalkerProfile();

                System.out.println("in userprofiles walker start "+snapshot.child("name").getValue().toString());
                walkerProfile.setName(snapshot.child("name").getValue().toString());
                walkerProfile.setState(snapshot.child("state").getValue().toString());
                walkerProfile.setProfileImage(snapshot.child("profileImage").getValue().toString());
                walkerProfile.setZip(snapshot.child("zip").getValue().toString());

                md_list.add(walkerProfile);



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("on child removed "+snapshot.getKey());
//                for (int i = 0; i < md_list.size(); i++) {
//                    if(md_list.get(i).getPid().equals(snapshot.getKey())) {
//                        pawModelList.remove(i);
//                        notifyItemRemoved(i);
//                        break;
//                    }
//                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}