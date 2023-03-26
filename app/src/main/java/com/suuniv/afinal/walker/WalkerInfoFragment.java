package com.suuniv.afinal.walker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suuniv.afinal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalkerInfoFragment#} factory method to
 * create an instance of this fragment.
 */
public class WalkerInfoFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_walker_info, container, false);
        Bundle args = getArguments();
        System.out.println("in walker info "+args.getString("name"));

        ImageView imageView =rootView.findViewById(R.id.profileImage);
          Picasso.get().load(args.getString("image")).into(imageView);
        ((TextView) rootView.findViewById(R.id.name)).setText(args.getString("name"));
        ((TextView) rootView.findViewById(R.id.state)).setText(args.getString("state"));
        ((TextView) rootView.findViewById(R.id.zip)).setText(args.getString("zip"));

        return rootView;
    }
}