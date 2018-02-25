package com.markusbilz.yown;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KeepListFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keep_list, container, false);

        recyclerView = view.findViewById(R.id.rv_keep);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = ItemAdapter.getSingelton(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ItemAdapter adapter = ItemAdapter.getSingelton(getActivity());
        adapter.reloadFiltered(true);
        recyclerView.setAdapter(adapter);
    }
}
