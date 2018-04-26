package com.markusbilz.yown;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DisposeFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dispose, container, false);
        ViewPager vpDispose = view.findViewById(R.id.vp_dispose);
        TabLayout tlDispose = view.findViewById(R.id.tl_dispose);

        MultiFragmentPagerAdapter adapter = new MultiFragmentPagerAdapter(getChildFragmentManager());
        vpDispose.setAdapter(adapter);
        tlDispose.setupWithViewPager(vpDispose);
        return view;
    }

}
