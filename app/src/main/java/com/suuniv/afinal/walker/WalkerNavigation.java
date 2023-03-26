package com.suuniv.afinal.walker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
import com.suuniv.afinal.Destination;
import com.suuniv.afinal.HomeFragment;
import com.suuniv.afinal.PaymentMethod;
import com.suuniv.afinal.R;
import com.suuniv.afinal.requests.RequestListActivity;

public class WalkerNavigation extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    public static final String CHANNEL_ID ="4" ;
    public static final String N_ID ="12" ;

    private Uri imageUri=null;
    Toolbar toolbar;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    private FirebaseAuth mAuth;

    private FirebaseUser currentUser;

    String usertype="";
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();


        setContentView(R.layout.activity_walker_navigation);
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
                    startActivity(new Intent(getApplicationContext(), WalkerActivity.class));

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new HomeFragment()).commit();


        //hopefully send notification
        final FirebaseDatabase fireBaseData = FirebaseDatabase.getInstance();
        final DatabaseReference ref = fireBaseData.getReference();
        ref.child("Request").addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //filter so we only see our user id being added

                final Iterable<DataSnapshot> children = snapshot.getChildren();

                for (DataSnapshot imageSnapshot : snapshot.getChildren()) {


                    //Put this in a wrapper so they only get their

                    if(true){
                    Intent resultIntent = new Intent(getApplicationContext(), Destination.class);
                    // Create pending intent and wrap our intent
                    PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, resultIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT); // flag: cancel any existing  pending intent

                    // create  Notification Builder
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_stat_name)
                                    .setContentTitle("New Notification")
                                    .setContentText("You have been asked to walk a dog.").setAutoCancel(true); // set auto cancel force a notification to be automatically dismissed
                    mBuilder.setContentIntent(resultPendingIntent);

                    // create  Notification Manager
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mNotificationManager.notify(i, mBuilder.build()); //notification ID, actual notification object.
                    i++;
                    }

                }
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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
                Intent intent = new Intent(this,WalkerActivity.class);
                intent.putExtra("userType",usertype);
                startActivity(intent);
                break;
            case R.id.item2:
                toast=Toast.makeText(this,"Myrequests",Toast.LENGTH_SHORT);
                toast.show();

                startActivity(new Intent(this, RequestListActivity.class));
                break;
            case R.id.item3:
                toast=Toast.makeText(this,"Item 3 Clicked",Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(this, PaymentMethod.class));
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


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "CIS651";
            String description = "Week5 Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}