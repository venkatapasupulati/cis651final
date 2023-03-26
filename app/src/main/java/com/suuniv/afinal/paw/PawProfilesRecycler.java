package com.suuniv.afinal.paw;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.suuniv.afinal.R;
import com.suuniv.afinal.walker.WalkerPagerActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PawProfilesRecycler extends RecyclerView.Adapter<PawProfilesRecycler.ViewHolder>
        implements Filterable {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("Paws");
    ChildEventListener usersRefListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<PawModel> pawModelList;

    public interface OnItemClickListener {
        void onListItemSelected(View sharedView, String imageResourceID, String title,
                                String year,String rating, String description);
    }

    private OnItemClickListener onItemClickListener;
    private RecyclerView r;

    public  PawProfilesRecycler(RecyclerView recyclerView, OnItemClickListener onItemClickListener){
        pawModelList =new ArrayList<>();
        r=recyclerView;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        System.out.println("in MOVIE RECYCLER");

        allPostsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                PawModel pawModel = new PawModel();
                if(currentUser.getUid().equalsIgnoreCase(snapshot.child("userId").getValue().toString())) {
                    pawModel.setUserId(snapshot.child("userId").getValue().toString());
                    pawModel.setPaw_name(snapshot.child("paw_name").getValue().toString());
                    pawModel.setVaccinations(snapshot.child("vaccinations").getValue().toString());
                    pawModel.setBreed(snapshot.child("breed").getValue().toString());
                    pawModel.setAge(snapshot.child("age").getValue().toString());
                    pawModel.setQuirks(snapshot.child("quirks").getValue().toString());
                    pawModel.setPid(snapshot.child("pid").getValue().toString());
                    pawModelList.add(pawModel);
                }

                PawProfilesRecycler.this.notifyDataSetChanged();

                r.scrollToPosition(pawModelList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("on child removed "+snapshot.getKey());
                for (int i = 0; i < pawModelList.size(); i++) {
                    if(pawModelList.get(i).getPid().equals(snapshot.getKey())) {
                        pawModelList.remove(i);
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
    public PawProfilesRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.paw_profile_view, parent,false);
        final ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PawProfilesRecycler.ViewHolder holder, int position) {

        System.out.println("In ONBIND"+pawModelList.size());
        PawModel u = pawModelList.get(position);

        if(u.getUserId().equalsIgnoreCase(currentUser.getUid())){

            holder.age.setText(u.getAge());
            System.out.println("onBINdholder "+u.getPaw_name());
            holder.name_v.setText(u.getPaw_name());
            holder.breed.setText(u.getBreed());
            holder.quirks.setText(u.getQuirks());
            holder.vacinations.setText(u.getVaccinations());

            holder.extras_v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("extras clicked");
                    PopupMenu popup = new PopupMenu(view.getContext(), view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.extras, popup.getMenu());
                    popup.show();

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {


                            System.out.println("itemId "+item.getItemId());

                            System.out.println("R.id.update "+R.id.update);

                            switch (item.getItemId()) {
                                case R.id.delete:


                                    AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                                    alert.setTitle("Delete Paw");
                                    alert.setMessage("Are you sure you want to delete?");
                                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {


                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            System.out.println("CLICKED YES "+u.getPid());
                                            allPostsRef.orderByChild("pid").equalTo(u.getPid())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            for(DataSnapshot data: dataSnapshot.getChildren()){
                                                                data.getRef().removeValue();

                                                            }

                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                        }
                                    });
                                    alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // close dialog
                                            dialog.cancel();
                                        }
                                    });
                                    alert.show();

                                    return true;
                                case R.id.update:

                                    Intent intent = (new Intent(view.getContext(), WalkerPagerActivity.class));
                                    intent.putExtra("pawId",u.getPid());
                                    r.getContext().startActivity(intent);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return pawModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name_v;
        public TextView age;
        public TextView quirks;
        public TextView breed;
        public TextView vacinations;
        public ImageView imageView;

        public ImageView extras_v;

        DatabaseReference uref;
        public TextView stars_v;

        public ViewHolder(View v){
            super(v);
            name_v = (TextView) v.findViewById(R.id.paw_name);

            age=v.findViewById(R.id.age);
            breed=v.findViewById(R.id.breed);
            vacinations = v.findViewById(R.id.vaccinations);

            quirks = v.findViewById(R.id.quirks);

            extras_v = v.findViewById(R.id.extras);

        }
    }
}
