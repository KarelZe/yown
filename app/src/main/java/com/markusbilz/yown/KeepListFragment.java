package com.markusbilz.yown;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class KeepListFragment extends Fragment {


    private RecyclerView rvKeep;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keep_list, container, false);

        rvKeep = view.findViewById(R.id.rv_keep);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvKeep.setLayoutManager(layoutManager);
        UpdateListTask updateListTask = new UpdateListTask();
        updateListTask.execute();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateListTask updateListTask = new UpdateListTask();
        updateListTask.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private class UpdateListTask extends AsyncTask<Void, Void, ArrayList<Item>> {

        @Override
        protected ArrayList<Item> doInBackground(Void... voids) {
            ItemDB itemDB = ItemDB.getInstance(getContext());
            return (ArrayList<Item>) itemDB.getAllFiltered(ItemDB.FILTER_KEEP);
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            ItemAdapter adapter = ItemAdapter.getInstance(items);
            rvKeep.setAdapter(adapter);
        }
    }
}
