package com.suuniv.afinal.walker;

import android.app.NotificationChannel;
import static com.suuniv.afinal.HomeFragment.openSMS;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.suuniv.afinal.Destination;
import com.suuniv.afinal.R;
import com.suuniv.afinal.requests.RequestInfo;

import java.util.IdentityHashMap;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalkerInfoFragment#} factory method to
 * create an instance of this fragment.
 */
public class WalkerInfoFragment extends Fragment {

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_walker_info, container, false);
        Bundle args = getArguments();
        System.out.println("in walker info "+args.getString("name"));

        ImageView imageView =rootView.findViewById(R.id.profileImage);
          Picasso.get().load(args.getString("image")).into(imageView);
        ((TextView) rootView.findViewById(R.id.name)).setText(args.getString("name"));
        ((TextView) rootView.findViewById(R.id.state)).setText(args.getString("state"));
        ((TextView) rootView.findViewById(R.id.zip)).setText(args.getString("zip"));
        ((TextView) rootView.findViewById(R.id.city)).setText(args.getString("city"));
        ((TextView) rootView.findViewById(R.id.price)).setText("$40.00");

        String walkId = args.getString("walkId");
        String pawId = args.getString("pawId");

        Button geo = rootView.findViewById(R.id.geoLocation);

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri ma = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway%2C+CA");
                showMap(ma);
            }
        });

        Button sms = rootView.findViewById(R.id.sms);
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSMS(getContext(),"Are you avaiable for a walk.","1234321234");

            }
        });



        Button walk = rootView.findViewById(R.id.requestWalk);

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestInfo requestInfo = new RequestInfo();

                String req = UUID.randomUUID().toString();
                requestInfo.setRequestId(req);
                requestInfo.setDogwalkerUserId(walkId);
                requestInfo.setPawId(pawId);
                requestInfo.setRequesterUid(currentUser.getUid());

                requestInfo.setRequestStatus("Request Sent");
                requestInfo.setTimestamp(ServerValue.TIMESTAMP);
                DatabaseReference mDatabase;
// ...
                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("WalkerRequests").child(req).setValue(requestInfo);

               Toast toast=Toast.makeText(getContext(),"Request sent to Walker",Toast.LENGTH_SHORT);
                toast.show();


                Intent resultIntent = new Intent(getContext(), Destination.class);
                // Create pending intent and wrap our intent
                PendingIntent resultPendingIntent = PendingIntent.getActivity(getContext(), 1, resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT); // flag: cancel any existing  pending intent

                // create  Notification Builder
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_stat_name)
                                .setContentTitle("New Notification")
                                .setContentText("You have been asked to walk a dog.").setAutoCancel(true); // set auto cancel force a notification to be automatically dismissed
                mBuilder.setContentIntent(resultPendingIntent);

                // create  Notification Manager
                NotificationManager mNotificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(0, mBuilder.build()); //notification ID, actual notification object.


                //hopefully send notification
//                final FirebaseDatabase fireBaseData = FirebaseDatabase.getInstance();
//                final DatabaseReference ref = fireBaseData.getReference();
//                ref.child("WalkerRequests").addValueEventListener(new ValueEventListener(){
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        //filter so we only see our user id being added
//
//                        final Iterable<DataSnapshot> children = snapshot.getChildren();
//
//                        for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
//
//
//                            //Put this in a wrapper so they only get their
//
//                            if(true){
//                                Intent resultIntent = new Intent(getContext(), Destination.class);
//                                // Create pending intent and wrap our intent
//                                PendingIntent resultPendingIntent = PendingIntent.getActivity(getContext(), 1, resultIntent,
//                                        PendingIntent.FLAG_CANCEL_CURRENT); // flag: cancel any existing  pending intent
//
//                                // create  Notification Builder
//                                NotificationCompat.Builder mBuilder =
//                                        new NotificationCompat.Builder(getContext(), CHANNEL_ID)
//                                                .setSmallIcon(R.drawable.ic_stat_name)
//                                                .setContentTitle("New Notification")
//                                                .setContentText("You have been asked to walk a dog.").setAutoCancel(true); // set auto cancel force a notification to be automatically dismissed
//                                mBuilder.setContentIntent(resultPendingIntent);
//
//                                // create  Notification Manager
//                                NotificationManager mNotificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                                mNotificationManager.notify(0, mBuilder.build()); //notification ID, actual notification object.
//
//                            }
//
//                        }
//                    }
//
//
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });


            }
        });



        return rootView;
    }
    public static final String CHANNEL_ID ="4" ;
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
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
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}