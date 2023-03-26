package com.suuniv.afinal.walker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        ((TextView) rootView.findViewById(R.id.city)).setText(args.getString("city"));
        //((TextView) rootView.findViewById(R.id.price)).setText(args.getString("price"));


        Button geo = rootView.findViewById(R.id.geoLocation);

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri ma = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway%2C+CA");
                showMap(ma);
            }
        });



        Button walk = rootView.findViewById(R.id.requestWalk);

        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



        return rootView;
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}