package com.nearbypets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nearbypets.R;

/**
 * Created by akshay on 31-05-2016.
 */
public class SwipeFragment extends Fragment {
    public static final String[] IMAGE_NAME = {"sliderbird", "sliderreptile", "sliderdogs"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
        ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        String imageFileName = IMAGE_NAME[position];
        int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.nearbypets");
        imageView.setImageResource(imgResId);
        return swipeView;
    }

    public static SwipeFragment newInstance(int position) {
        SwipeFragment swipeFragment = new SwipeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }
}
