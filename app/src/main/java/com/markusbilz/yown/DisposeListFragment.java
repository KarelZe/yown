package com.markusbilz.yown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class DisposeListFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<Item> items;

    public DisposeListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dispose_list, container, false);


        recyclerView = view.findViewById(R.id.rv_dispose);
        layoutManager = new LinearLayoutManager(getActivity());
        loadItems();
        adapter = new ItemWithButtonAdapter(items);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadItems(){
        items = new ArrayList<>();
        items.add(new Item("Mac Book II", "This is my 2. laptop."));

    }
}
