package com.nearbypets.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nearbypets.R;
import com.nearbypets.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by akshay on 31-05-2016.
 */
public class SwipeFragment extends Fragment {
    public static ArrayList<String> IMAGE_NAME;// = {"sliderbird", "sliderreptile", "sliderdogs"};
    static CustomCall customCall;
    private ImageLoader mImageLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
        NetworkImageView imageView = (NetworkImageView) swipeView.findViewById(R.id.imageView);

        Bundle bundle = getArguments();
        final int position = bundle.getInt("position");
        String imageFileName = IMAGE_NAME.get(position);
        mImageLoader = CustomVolleyRequestQueue.getInstance(getContext())
                .getImageLoader();
       /* int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.nearbypets");
        imageView.setImageResource(imgResId);*/
        final String url = imageFileName;
        if (url != null && !url.isEmpty()) {
            try {
                mImageLoader.get(url, ImageLoader.getImageListener(imageView,
                        R.drawable.default_pet_image, R.drawable.default_pet_image));
                imageView.setImageUrl(url, mImageLoader);
            } catch (Exception e) {
                imageView.setImageResource(R.drawable.default_pet_image);
            }
        } else {
            imageView.setImageResource(R.drawable.default_pet_image);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customCall != null)
                    customCall.onButtonClickListener(position, position);
            }
        });
        return swipeView;
    }

    public static SwipeFragment newInstance(int position, ArrayList<String> names) {
        IMAGE_NAME = names;
        SwipeFragment swipeFragment = new SwipeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        swipeFragment.setArguments(bundle);
        return swipeFragment;
    }

    public static void setCustomButtonListner(CustomCall listener) {
        customCall = listener;
    }

    public interface CustomCall {
        public void onButtonClickListener(int id, int position);
    }
}
