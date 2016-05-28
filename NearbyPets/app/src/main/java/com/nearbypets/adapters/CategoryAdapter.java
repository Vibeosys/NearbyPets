package com.nearbypets.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private ArrayList<CategoryDTO> mCategories;

    public CategoryAdapter(Context mContext, ArrayList<CategoryDTO> mCategories) {
        this.mContext = mContext;
        this.mCategories = mCategories;
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
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        CategoryDTO category = mCategories.get(position);
        //Log.d(TAG, friend.toString());
        viewHolder.categoryName.setText(category.getCategoryName());
        viewHolder.categoryCount.setText(category.getProductCount() + " products");
        return row;
    }

    private class ViewHolder {
        RobotoMediumTextView categoryName;
        RobotoRegularTextView categoryCount;
    }
}
