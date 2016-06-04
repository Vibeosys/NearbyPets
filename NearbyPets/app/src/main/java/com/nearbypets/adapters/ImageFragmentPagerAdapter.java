package com.nearbypets.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nearbypets.fragments.SwipeFragment;

import java.util.ArrayList;

/**
 * Created by akshay on 31-05-2016.
 */
public class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
    public static ArrayList<String> IMAGE_NAME;//= {"sliderbird", "sliderreptile", "sliderdogs"};


    public ImageFragmentPagerAdapter(FragmentManager fm, ArrayList<String> name) {
        super(fm);
        this.IMAGE_NAME = name;
    }

    @Override
    public int getCount() {
        return IMAGE_NAME.size();
    }

    @Override
    public Fragment getItem(int position) {
        SwipeFragment fragment = new SwipeFragment();
        return SwipeFragment.newInstance(position, IMAGE_NAME);
    }


}
