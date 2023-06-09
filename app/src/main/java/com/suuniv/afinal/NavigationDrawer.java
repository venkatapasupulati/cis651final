package com.suuniv.afinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.suuniv.afinal.paw.PawModel;
import com.suuniv.afinal.paw.PawProfile;
import com.suuniv.afinal.paw.PawProfilesRecycler;
import com.suuniv.afinal.walker.WalkerPagerActivity;

import java.util.HashMap;

public class NavigationDrawer extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private Uri imageUri=null;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    String usertype="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar=(Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_baseline_exit_to_app_24);

        usertype=  getIntent().getStringExtra("userType");
        mAuth = FirebaseAuth.getInstance();
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(this);

        // handle the toggle between the action bar and the navigation drawer
        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,
                drawerLayout,toolbar,
                R.string.openD,R.string.closeD){
            @Override
            public void onDrawerClosed(View drawerView){
                super.onDrawerClosed(drawerView);
//                Toast toast=Toast.makeText(NavigationDrawer.this,"NavigationDrawer Closed",Toast.LENGTH_SHORT);
//                toast.show();
            }
            @Override
            public void onDrawerOpened(View drawerView){
                super.onDrawerOpened(drawerView);
//                Toast toast=Toast.makeText(NavigationDrawer.this,"NavigationDrawer Opened",Toast.LENGTH_SHORT);
//                toast.show();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String currentUserString = currentUser.getUid();

        DatabaseReference allPostsRef = FirebaseDatabase.getInstance().getReference("UserProfile/" + currentUserString);
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //add some toast

                } else {
                    Intent  intent =new Intent(getApplicationContext(),Profile.class);
                    intent.putExtra("userType",usertype);
                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });

    getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();






    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        Fragment fragment = null;
        System.out.println("iddd "+id);
        switch (id)
        {
            case R.id.item1:
                Toast toast=Toast.makeText(this,"Profile Clicked",Toast.LENGTH_SHORT);
                toast.show();
                Intent  intent =new Intent(this,Profile.class);
                intent.putExtra("userType",usertype);
                startActivity(intent);
                break;
            case R.id.item2:
                toast=Toast.makeText(this,"Requests Clicked",Toast.LENGTH_SHORT);
                toast.show();

                startActivity(new Intent(this, PawProfile.class));
                break;

            case R.id.item5:
                toast=Toast.makeText(this,"Payment Method Setup",Toast.LENGTH_SHORT);
                toast.show();

                startActivity(new Intent(this, PaymentMethod.class));

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}