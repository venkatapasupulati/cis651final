package com.suuniv.afinal.walker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suuniv.afinal.HomeFragment;
import com.suuniv.afinal.PaymentMethod;
import com.suuniv.afinal.R;
import com.suuniv.afinal.paw.PawProfile;

public class WalkerNavigation extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private Uri imageUri=null;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_navigation);
        toolbar=(Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_baseline_exit_to_app_24);

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
                    startActivity(new Intent(getApplicationContext(), WalkerActivity.class));

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
                Toast toast=Toast.makeText(this,"Item 1 Clicked",Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(this,WalkerActivity.class));
                break;
            case R.id.item2:
                toast=Toast.makeText(this,"ViewPager Clicked",Toast.LENGTH_SHORT);
                toast.show();

                startActivity(new Intent(this, WalkerPagerActivity.class));
                break;
            case R.id.item3:
                toast=Toast.makeText(this,"Item 3 Clicked",Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(this, PaymentMethod.class));
                break;
            case R.id.item4:
                break;
            case R.id.item5:
                break;
//            case R.id.item6:
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.main_container, new TrainingFragment())
//                        .commit();
//                break;
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