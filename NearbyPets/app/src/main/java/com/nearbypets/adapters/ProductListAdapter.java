package com.nearbypets.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nearbypets.R;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.CustomVolleyRequestQueue;
import com.nearbypets.utils.DateUtils;
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
    private int activityFlag = 0;
    private int roleId;
    private Context mContext;
    private ArrayList<ProductDataDTO> mProductList = new ArrayList<ProductDataDTO>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    CustomButtonListener customButtonListener;
    CustomItemListener customItemListener;
    CustomHideListener customHideListener;
    private ImageLoader mImageLoader;
    private LayoutInflater mInflater;

    public ProductListAdapter(Context context, int roleId) {
        this.mContext = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.roleId = roleId;
    }

    public void addItem(final ProductDataDTO item) {
        mProductList.add(item);
        notifyDataSetChanged();
    }

    public void setActivityFlag(int flag) {
        this.activityFlag = flag;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.row_product_list, null);
                    holder.imgProductImage = (NetworkImageView) convertView.findViewById(R.id.productImage);
                    holder.txtProductTitle = (RobotoMediumTextView) convertView.findViewById(R.id.txtProductTitle);
                    holder.txtProductPrice = (RobotoMediumTextView) convertView.findViewById(R.id.txtProductPrice);
                    holder.txtDesc = (RobotoRegularTextView) convertView.findViewById(R.id.txtDesc);
                    holder.txtDistance = (RobotoItalicTextView) convertView.findViewById(R.id.txtDistance);
                    holder.imgFavourite = (ImageView) convertView.findViewById(R.id.imgFavourite);
                    holder.txtDate = (RobotoMediumTextView) convertView.findViewById(R.id.txtDate);
                    holder.btnHide = (TextView) convertView.findViewById(R.id.btnHideAd);
                    holder.btnUnHide = (TextView) convertView.findViewById(R.id.btnUnHideAd);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.row_product_list_header, null);
                    //
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ProductDataDTO product = mProductList.get(position);
        switch (rowType) {
            case TYPE_ITEM:

                holder.txtProductTitle.setText(product.getProductName());
                holder.txtDesc.setText(product.getProductDesc());
                holder.txtDistance.setText(String.format("%.2f", product.getDistance()) + " km away from you");
               /* int imgResId = mContext.getResources().getIdentifier(product.getProductImage(), "drawable", "com.nearbypets");
                holder.imgProductImage.setImageResource(imgResId);*/
                holder.txtProductPrice.setText(mContext.getResources().
                        getString(R.string.str_euro_price_symbol) + " " + String.format("%.2f", product.getPrice()));
                mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                        .getImageLoader();
                final String url = product.getProductImage();
                if (url != null && !url.isEmpty()) {
                    try {
                        mImageLoader.get(url, ImageLoader.getImageListener(holder.imgProductImage,
                                R.drawable.default_pet_image, R.drawable.default_pet_image));
                        holder.imgProductImage.setImageUrl(url, mImageLoader);
                    } catch (Exception e) {
                        holder.imgProductImage.setImageResource(R.drawable.default_pet_image);
                    }
                } else {
                    holder.imgProductImage.setImageResource(R.drawable.default_pet_image);
                }
                DateUtils date = new DateUtils();
                holder.txtDate.setText(date.getLocalDateInFormat(product.getPostedDt()));

                if (activityFlag == AppConstants.POSTED_AD_FLAG_ADAPTER)
                    holder.imgFavourite.setVisibility(View.GONE);
                else
                    holder.imgFavourite.setVisibility(View.VISIBLE);
                if (product.isFavouriteFlag())
                    holder.imgFavourite.setImageResource(R.drawable.ic_favorite_red_24dp);
                else
                    holder.imgFavourite.setImageResource(R.drawable.ic_favorite_black_24dp);
                holder.imgFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customButtonListener != null)
                            customButtonListener.onButtonClickListener(v.getId(), position, product.isFavouriteFlag(), product);
                    }
                });
                holder.txtProductTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customItemListener != null)
                            customItemListener.onItemClickListener(position, product);
                    }
                });
                holder.txtDesc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customItemListener != null)
                            customItemListener.onItemClickListener(position, product);
                    }
                });
                holder.txtDistance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customItemListener != null)
                            customItemListener.onItemClickListener(position, product);
                    }
                });
                holder.imgProductImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customItemListener != null)
                            customItemListener.onItemClickListener(position, product);
                    }
                });
                if (roleId == AppConstants.ROLL_ID_USER) {

                    holder.btnHide.setVisibility(View.GONE);
                    holder.btnUnHide.setVisibility(View.GONE);
                } else {
                    if (activityFlag == AppConstants.HIDDEN_AD_FLAG_ADAPTER) {
                        holder.btnHide.setVisibility(View.GONE);
                        holder.btnUnHide.setVisibility(View.VISIBLE);
                    } else {
                        holder.btnHide.setVisibility(View.VISIBLE);
                        holder.btnUnHide.setVisibility(View.GONE);
                    }

                }
                holder.btnHide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customHideListener != null)
                            customHideListener.onHideClickListener(position, product);
                    }
                });
                holder.btnUnHide.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (customHideListener != null)
                            customHideListener.onHideClickListener(position, product);
                    }
                });
                break;
            case TYPE_SEPARATOR:

                break;

        }
        return convertView;
    }

    public void clear() {
        this.mProductList.clear();
    }

    public static class ViewHolder {
        RobotoMediumTextView txtProductTitle;
        RobotoMediumTextView txtProductPrice;
        RobotoRegularTextView txtDesc;
        RobotoItalicTextView txtDistance;
        ImageView imgFavourite;
        NetworkImageView imgProductImage;
        RobotoMediumTextView txtDate;
        TextView btnHide, btnUnHide;
    }

    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customButtonListener = listener;
    }

    public interface CustomButtonListener {
        public void onButtonClickListener(int id, int position, boolean value, ProductDataDTO productData);
    }

    public void setCustomItemListner(CustomItemListener listener) {
        this.customItemListener = listener;
    }

    public interface CustomItemListener {
        public void onItemClickListener(int position, ProductDataDTO productData);
    }

    public void setCustomHideListener(CustomHideListener listener) {
        this.customHideListener = listener;
    }

    public interface CustomHideListener {
        public void onHideClickListener(int position, ProductDataDTO productData);
    }
}
