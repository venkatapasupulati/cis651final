package com.suuniv.afinal.walker;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.firebase.database.ChildEventListener;

import java.util.ArrayList;
import java.util.List;

public class WalkerViewPagerAdapter  extends FragmentStateAdapter {

    List<WalkerProfile> walkerProfiles;

    String pawId;
    public WalkerViewPagerAdapter(@NonNull FragmentActivity fa, List<WalkerProfile> walkerProfiles) {
        super(fa);
        this.walkerProfiles = walkerProfiles;
        pawId= fa.getIntent().getStringExtra("pawId");

    }


    @NonNull
    @Override
    public Fragment createFragment(int i) {
        Fragment fragment = new WalkerInfoFragment();
        Bundle args = new Bundle();
        args.putString("name",walkerProfiles.get(i).getName());
        args.putString("state",walkerProfiles.get(i).getState());
        args.putString("zip",walkerProfiles.get(i).getZip());
        args.putString("city",walkerProfiles.get(i).getCity());
        args.putString("image",walkerProfiles.get(i).getProfileImage());
        args.putString("walkId",walkerProfiles.get(i).getUserId());
        args.putString("pawId",pawId);
        System.out.println("pawdid "+pawId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return walkerProfiles.size();
    }
}
