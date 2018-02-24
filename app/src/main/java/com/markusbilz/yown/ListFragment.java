package com.markusbilz.yown;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ListFragment extends Fragment {

    // TODO: Rename and change types of parameters
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<Item> items;
    FloatingActionButton fabAdd;


    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

      fabAdd = view.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = view.findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(getActivity());
        loadItems();
        adapter = new ItemWithCheckboxAdapter(items);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void loadItems(){
        items = new ArrayList<>();
        items.add(new Item("Mac Book", "This is my laptop."));
        items.add(new Item("Mac Book II", "Because I love Apple."));

    }
}
