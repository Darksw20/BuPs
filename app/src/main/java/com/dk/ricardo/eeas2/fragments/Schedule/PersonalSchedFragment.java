package com.dk.ricardo.eeas2.fragments.Schedule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dk.ricardo.eeas2.Adapters.RecyclerAdapter;
import com.dk.ricardo.eeas2.R;
import com.dk.ricardo.eeas2.utilidades.ItemData;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalSchedFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_sched, container, false);

        recyclerView=view.findViewById(R.id.recyclerPSched);
        ItemData itemData[] ={
                new ItemData("Noticia 1",R.drawable.ic_afuera),
                new ItemData("Noticia 2",R.drawable.ic_afuera),
                new ItemData("Noticia 3",R.drawable.ic_afuera),
                new ItemData("Noticia 4",R.drawable.ic_afuera),
                new ItemData("Noticia 5",R.drawable.ic_afuera),
                new ItemData("Noticia 6",R.drawable.ic_afuera),
                new ItemData("Noticia 7",R.drawable.ic_afuera),
                new ItemData("Noticia 8",R.drawable.ic_afuera)
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(itemData);

        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

}