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

import com.nearbypets.R;
import com.nearbypets.data.CategoryDTO;
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
            viewHolder.imageBackImg = (RelativeLayout) row.findViewById(R.id.imageBackImg);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        CategoryDTO category = mCategories.get(position);
        //Log.d(TAG, friend.toString());
        viewHolder.categoryName.setText(category.getCategoryName());
        viewHolder.categoryCount.setText(category.getProductCount() + " products");
        int imgResId = mContext.getResources().getIdentifier(category.getCategoryImage(), "drawable", "com.nearbypets");
        viewHolder.imageBackImg.setBackgroundResource(imgResId);
        return row;
    }

    private class ViewHolder {
        RobotoMediumTextView categoryName;
        RobotoRegularTextView categoryCount;
        RelativeLayout imageBackImg;
    }

    public void clear() {
        mCategories.clear();
    }

    public void addItem(final CategoryDTO item) {
        mCategories.add(item);
        notifyDataSetChanged();
    }
}
