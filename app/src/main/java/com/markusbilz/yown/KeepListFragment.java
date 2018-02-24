package com.markusbilz.yown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class KeepListFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<Item> items;

    public KeepListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keep_list, container, false);


        recyclerView = view.findViewById(R.id.rv_keep);
        layoutManager = new LinearLayoutManager(getActivity());
        loadItems();
        adapter = new ItemAdapter(items);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadItems(){
        items = new ArrayList<>();
        items.add(new Item("Mac Book", "This is my laptop."));

    }
}
