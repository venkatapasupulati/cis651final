package com.suuniv.afinal.paw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.suuniv.afinal.R;

public class PawProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paw_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new PawListFragment();
            fragmentManager.beginTransaction().replace(R.id.pawframe, fragment).commit();
        }


    }

    public void AddPawProfile(View view){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new PawFragment();
        fragmentManager.beginTransaction().replace(R.id.pawframe, fragment).commit();
    }
}