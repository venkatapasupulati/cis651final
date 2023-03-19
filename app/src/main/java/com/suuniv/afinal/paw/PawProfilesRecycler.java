package com.suuniv.afinal.paw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.suuniv.afinal.R;

import java.util.List;

public class PawProfilesRecycler extends RecyclerView.Adapter<PawProfilesRecycler.ViewHolder>
        implements Filterable {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("Movies");
    ChildEventListener usersRefListener;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<PawModel> pawModelList;


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


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name_v;
        public TextView age;

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

        }
    }
}
