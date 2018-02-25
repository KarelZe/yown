package com.markusbilz.yown;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ListFragment extends Fragment {

    public static final int RESULT_OK = 200;
    public static final int REQUEST_ADD_ITEM = 1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    FloatingActionButton fabAdd;

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
                startActivityForResult(intent, REQUEST_ADD_ITEM);
            }
        });

        recyclerView = view.findViewById(R.id.rv_list);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = ItemWithCheckboxAdapter.getSingelton(this.getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && REQUEST_ADD_ITEM == requestCode) {
            // swap adapter and update list with the containing items
            ItemWithCheckboxAdapter adapter = ItemWithCheckboxAdapter.getSingelton(getActivity());
            adapter.reload();
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // update recyclerView in every onResume
        ItemWithCheckboxAdapter adapter = ItemWithCheckboxAdapter.getSingelton(getActivity());
        adapter.reload();
        recyclerView.setAdapter(adapter);

    }
}
