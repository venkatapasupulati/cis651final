package com.suuniv.afinal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.suuniv.afinal.walker.WalkerNavigation;


//add account overview. May need to add some more fields to the user







public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Uri imageUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if(currentUser==null){
                    Toast.makeText(MainActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, SignupLogin.class));
                    finish();
                }
                else{
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference usersRef = database.getReference("Users");
                    System.out.println("uid "+currentUser.getUid());
                    usersRef.child(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            System.out.println("snk "+snapshot.getKey());
                            System.out.println("val "+snapshot.getValue());
                            if("userType".equalsIgnoreCase(snapshot.getKey())){
                                System.out.println("val2 "+snapshot.getValue());
                                if("DOGOWNER".equalsIgnoreCase((String) snapshot.getValue())){
                                    startActivity(new Intent(MainActivity.this, NavigationDrawer.class));
                                    finish();
                                }else{
                                    startActivity(new Intent(MainActivity.this, WalkerNavigation.class));
                                    finish();
                                }
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

                }
            }

            //}
        }.start();
    }

    private String userType="";
    private String checkUserType(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");



        System.out.println("uid "+currentUser.getUid());
        usersRef.child(currentUser.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                System.out.println("snk "+snapshot.getKey());
                System.out.println("val "+snapshot.getValue());
                if("userType".equalsIgnoreCase(snapshot.getKey())){
                    System.out.println("val2 "+snapshot.getValue());
                    if("DOGOWNER".equalsIgnoreCase((String) snapshot.getValue())){
                        userType = "DOGOWNER";
                    }else{
                        userType="DOGWALKER";
                    }
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
        System.out.println("usertype after val "+userType);
        return  userType;
    }
}