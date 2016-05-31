package com.nearbypets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nearbypets.fragments.SwipeFragment;

/**
 * Created by akshay on 31-05-2016.
 */
public class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
    public ImageFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int position) {
        SwipeFragment fragment = new SwipeFragment();
        return SwipeFragment.newInstance(position);
    }
}
