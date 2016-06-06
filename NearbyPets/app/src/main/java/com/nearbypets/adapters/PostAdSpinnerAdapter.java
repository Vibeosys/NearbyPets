package com.nearbypets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nearbypets.R;
import com.nearbypets.data.TypeDataDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-06-2016.
 */
public class PostAdSpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<TypeDataDTO> mCategories = new ArrayList<>();

    public PostAdSpinnerAdapter(Context mContext) {
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
            row = theLayoutInflator.inflate(R.layout.category_type_post_ad, null);
            viewHolder = new ViewHolder();
            viewHolder.categoryType = (TextView) row.findViewById(R.id.category_Type);
            row.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();
            TypeDataDTO category = mCategories.get(position);
            viewHolder.categoryType.setText(category.getTypeTitle());

        return row;
    }

    private class ViewHolder {
        TextView categoryType;
    }

    public void addItem(final TypeDataDTO item) {
        mCategories.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mCategories.clear();
    }



}
