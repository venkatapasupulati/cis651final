package com.suuniv.afinal.paw;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suuniv.afinal.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PawDetailsFragment#} factory method to
 * create an instance of this fragment.
 */
public class PawDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public PawDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_paw_details, container, false);
        ImageView imageView = v.findViewById(R.id.large_image);
        System.out.println("arguments.getStrin "+ arguments.getString("image"));
        Picasso.get().load(arguments.getString("image")).into(imageView);
        EditText editText = v.findViewById(R.id.title_text);
        editText.setText(arguments.getString("name"));
        EditText yearText = v.findViewById(R.id.age_text);
        yearText.setText(arguments.getString("age"));

        EditText breed = v.findViewById(R.id.breed_text);
        breed.setText(arguments.getString("breed"));

        EditText quriks = v.findViewById(R.id.quirks_text);
        quriks.setText(arguments.getString("quirks"));

        EditText vac = v.findViewById(R.id.vaccinations_text);
        vac.setText(arguments.getString("vaccinations"));


        return  v;
    }
}