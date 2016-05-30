package com.nearbypets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nearbypets.R;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.views.RobotoItalicTextView;
import com.nearbypets.views.RobotoMediumTextView;
import com.nearbypets.views.RobotoRegularTextView;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by akshay on 30-05-2016.
 */
public class ProductListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private Context mContext;
    private ArrayList<ProductDataDTO> mProductList = new ArrayList<ProductDataDTO>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public ProductListAdapter(Context context) {
        this.mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final ProductDataDTO item) {
        mProductList.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final ProductDataDTO item) {
        mProductList.add(item);
        sectionHeader.add(mProductList.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public ProductDataDTO getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_product_list, null);
                    holder.txtProductTitle = (RobotoMediumTextView) convertView.findViewById(R.id.txtProductTitle);
                    holder.txtProductPrice = (RobotoMediumTextView) convertView.findViewById(R.id.txtProductPrice);
                    holder.txtDesc = (RobotoRegularTextView) convertView.findViewById(R.id.txtDesc);
                    holder.txtDistance = (RobotoItalicTextView) convertView.findViewById(R.id.txtDistance);
                    holder.imgFavourite = (ImageView) convertView.findViewById(R.id.imgFavourite);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.row_product_list_header, null);
                    holder.txtDate = (RobotoMediumTextView) convertView.findViewById(R.id.txtDate);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ProductDataDTO product = mProductList.get(position);
        switch (rowType) {
            case TYPE_ITEM:
                holder.txtProductTitle.setText(product.getProductName());
                holder.txtDesc.setText(product.getProductDesc());
                holder.txtDistance.setText(product.getDistance());
                holder.txtProductPrice.setText(mContext.getResources().
                        getString(R.string.str_euro_price_symbol) + " " + product.getPrice());
                if (product.isFavouriteFlag())
                    holder.imgFavourite.setImageResource(R.drawable.ic_favorite_red_24dp);
                else
                    holder.imgFavourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                break;
            case TYPE_SEPARATOR:
                holder.txtDate.setText(product.getDate());
                break;

        }
        return convertView;
    }

    public static class ViewHolder {
        RobotoMediumTextView txtProductTitle;
        RobotoMediumTextView txtProductPrice;
        RobotoRegularTextView txtDesc;
        RobotoItalicTextView txtDistance;
        ImageView imgFavourite;
        RobotoMediumTextView txtDate;

    }
}
