package com.dk.ricardo.eeas2.fragments.ControlPanel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dk.ricardo.eeas2.Adapters.RecyclerAdapter;
import com.dk.ricardo.eeas2.R;
import com.dk.ricardo.eeas2.utilidades.Items.ItemData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaffGestorFragment extends Fragment implements RecyclerAdapter.ItemClickListener{

    RecyclerView staffGestor;
    RecyclerAdapter recyclerAdapter;
    ArrayList<ItemData> itemData=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staff_gestor, container, false);

        staffGestor=view.findViewById(R.id.staffGestorView);
        itemData.add(new ItemData("Juan Miguel CUM:JAL0202020202",R.drawable.ic_afuera));

        staffGestor.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapter = new RecyclerAdapter(getContext(),itemData);
        recyclerAdapter.setClickListener(this);

        staffGestor.setAdapter(recyclerAdapter);

        staffGestor.setItemAnimator(new DefaultItemAnimator());
        return view;
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(getContext(), "You clicked " + recyclerAdapter.getItem(position).getTitle().toString() + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

}
