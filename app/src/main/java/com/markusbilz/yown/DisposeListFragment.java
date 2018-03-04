package com.markusbilz.yown;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DisposeListFragment extends Fragment {


    private RecyclerView recyclerView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dispose_list, container, false);

        recyclerView = view.findViewById(R.id.rv_dispose);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        RecyclerView.Adapter adapter = ItemWithButtonAdapter.getSingelton(getActivity());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ItemWithButtonAdapter adapter = ItemWithButtonAdapter.getSingelton(getActivity());
        adapter.reloadFiltered();
        recyclerView.setAdapter(adapter);
    }

}
