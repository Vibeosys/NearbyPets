package com.nearbypets.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nearbypets.R;
import com.nearbypets.data.CategoryDTO;
import com.nearbypets.utils.CustomVolleyRequestQueue;
import com.nearbypets.views.MyriadProRegularTextView;
import com.nearbypets.views.RobotoMediumTextView;
import com.nearbypets.views.RobotoRegularTextView;

import java.util.ArrayList;

/**
 * Created by akshay on 28-05-2016.
 */
public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CategoryDTO> mCategories = new ArrayList<CategoryDTO>();
    private ImageLoader mImageLoader;

    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;

        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_category_list, null);
            viewHolder = new ViewHolder();
            viewHolder.categoryName = (RobotoMediumTextView) row.findViewById(R.id.txtCategory);
            viewHolder.categoryCount = (RobotoRegularTextView) row.findViewById(R.id.txtProductCount);
            viewHolder.imageView = (NetworkImageView) row.findViewById(R.id.categoryImage);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        CategoryDTO category = mCategories.get(position);
        //Log.d(TAG, friend.toString());
        viewHolder.categoryName.setText(category.getCategoryName());
        viewHolder.categoryCount.setText(category.getProductCount() + " products");
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();
        final String url = category.getCategoryImage();
        if (url != null && !url.isEmpty()) {
            try {
                mImageLoader.get(url, ImageLoader.getImageListener(viewHolder.imageView,
                        R.drawable.default_pet_image, R.drawable.default_pet_image));
                viewHolder.imageView.setImageUrl(url, mImageLoader);
            } catch (Exception e) {
                viewHolder.imageView.setImageResource(R.drawable.default_pet_image);
            }
        } else {
            viewHolder.imageView.setImageResource(R.drawable.default_pet_image);
        }
        return row;
    }

    private class ViewHolder {
        RobotoMediumTextView categoryName;
        RobotoRegularTextView categoryCount;
        NetworkImageView imageView;
    }

    public void clear() {
        mCategories.clear();
    }

    public void addItem(final CategoryDTO item) {
        mCategories.add(item);
        notifyDataSetChanged();
    }
}
