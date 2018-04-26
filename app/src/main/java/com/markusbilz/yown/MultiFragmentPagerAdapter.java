package com.markusbilz.yown;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class MultiFragmentPagerAdapter extends FragmentPagerAdapter {

    MultiFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    // This determines the fragment for each tab
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new KeepListFragment();
        } else {
            return new DisposeListFragment();
        }

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Keep";
            case 1:
                return "Let go";
            default:
                return null;
        }
    }

}