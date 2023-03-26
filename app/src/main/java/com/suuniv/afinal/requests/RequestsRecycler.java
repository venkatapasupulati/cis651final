package com.suuniv.afinal.requests;

import android.app.DownloadManager;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suuniv.afinal.Profile;
import com.suuniv.afinal.R;
import com.suuniv.afinal.UserProfile;
import com.suuniv.afinal.paw.PawModel;
import com.suuniv.afinal.paw.PawProfile;
import com.suuniv.afinal.paw.PawProfilesRecycler;
import com.suuniv.afinal.walker.WalkerProfile;

import java.util.ArrayList;
import java.util.List;

public class RequestsRecycler extends RecyclerView.Adapter<RequestsRecycler.ViewHolder>
        implements Filterable {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("WalkerRequests");
    ChildEventListener usersRefListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<RequestInfo> requestInfoList;

    public interface OnItemClickListener {
        void onListItemSelected(View sharedView, String imageResourceID, String title,
                                String year, String rating, String description);
    }

    private RequestsRecycler.OnItemClickListener onItemClickListener;
    private RecyclerView r;

    public  RequestsRecycler(RecyclerView recyclerView, RequestsRecycler.OnItemClickListener onItemClickListener){
        requestInfoList =new ArrayList<>();
        r=recyclerView;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        System.out.println("in requestInfoList RECYCLER");

        allPostsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                RequestInfo requestInfo = new RequestInfo();
                if(currentUser.getUid().equalsIgnoreCase(snapshot.child("dogwalkerUserId").getValue().toString())) {

                    requestInfo.setRequestStatus(snapshot.child("requestStatus").getValue().toString());
                    requestInfo.setRequestId(snapshot.child("requestId").getValue().toString());
                    requestInfo.setPawId(snapshot.child("pawId").getValue().toString());
                    requestInfo.setRequesterUid(snapshot.child("requesterUid").getValue().toString());
                    requestInfoList.add(requestInfo);
                }

                RequestsRecycler.this.notifyDataSetChanged();

                r.scrollToPosition(requestInfoList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("on child removed "+snapshot.getKey());
                for (int i = 0; i < requestInfoList.size(); i++) {
                    if(requestInfoList.get(i).getRequestId().equals(snapshot.getKey())) {
                        requestInfoList.remove(i);
                        notifyItemRemoved(i);
                        break;
                    }
                }

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
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public RequestsRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_view_recycler, parent,false);
        final RequestsRecycler.ViewHolder vh = new RequestsRecycler.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsRecycler.ViewHolder holder, int position) {

        RequestInfo requestInfo =requestInfoList.get(position);

        DatabaseReference userreq = database.getReference("UserProfile");

        DatabaseReference allPostsRef = FirebaseDatabase.getInstance().getReference("UserProfile/" + requestInfo.getRequesterUid());
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //add some toast

                   UserProfile prof = snapshot.getValue(UserProfile.class);

                    holder.requester_name.setText(prof.name);

                } else {


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });

        DatabaseReference apawsRef = FirebaseDatabase.getInstance().getReference("Paws/" + requestInfo.getPawId());
        apawsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    //add some toast

                    PawModel prof = snapshot.getValue(PawModel.class);

                    holder.breed.setText(prof.getBreed());

                } else {


                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });



            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("in recycle click");

// ...
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                    mDatabase.child("WalkerRequests").child(requestInfo.getRequestId()+"/requestStatus").setValue("Accepted");

                    holder.accept.setVisibility(View.INVISIBLE);
                    holder.reject.setVisibility(View.INVISIBLE);

                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("in recycle click");

// ...
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


                    mDatabase.child("WalkerRequests").child(requestInfo.getRequestId()+"/requestStatus").setValue("Rejected");
                    holder.accept.setVisibility(View.INVISIBLE);
                    holder.reject.setVisibility(View.INVISIBLE);

                }
            });




        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child("WalkerRequests").child(requestInfo.getRequestId()+"/requestStatus").setValue("Completed");


            }
        });

    }

    @Override
    public int getItemCount() {
        return requestInfoList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView requester_name;


        public TextView breed;

        public ImageView imageView;

        public ImageView extras_v;

        DatabaseReference uref;
        public TextView stars_v;

        public Button accept;
        public  Button reject;

        public  Button done;

        public ViewHolder(View v){
            super(v);

            requester_name= v.findViewById( R.id.requester_name);

            breed=v.findViewById(R.id.breed);

            accept = v.findViewById(R.id.accept);

            extras_v = v.findViewById(R.id.extras);

            reject = v.findViewById(R.id.reject);

            done = v.findViewById(R.id.done);

        }
    }
}
