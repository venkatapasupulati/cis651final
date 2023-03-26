package com.suuniv.afinal.requests;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suuniv.afinal.R;
import com.suuniv.afinal.paw.PawProfilesRecycler;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestListFragment#} factory method to
 * create an instance of this fragment.
 */
public class RequestListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    public RequestListFragment() {
        // Required empty public constructor
    }

   RequestsRecycler adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_list, container, false);

     RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerVieww);

        adapter = new RequestsRecycler(recyclerView, new RequestsRecycler.OnItemClickListener() {
            @Override
            public void onListItemSelected(View sharedView, String imageResourceID, String title, String year, String rating, String description) {

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }
}