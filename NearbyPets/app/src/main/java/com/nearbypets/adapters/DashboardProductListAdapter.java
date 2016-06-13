package com.nearbypets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.nearbypets.R;
import com.nearbypets.data.ProductDataDTO;
import com.nearbypets.utils.AppConstants;
import com.nearbypets.utils.CustomVolleyRequestQueue;
import com.nearbypets.utils.DateUtils;
import com.nearbypets.views.RobotoItalicTextView;
import com.nearbypets.views.RobotoMediumTextView;
import com.nearbypets.views.RobotoRegularTextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by akshay on 30-05-2016.
 */
public class DashboardProductListAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_Ad = 2;

    private Context mContext;
    private ArrayList<ProductDataDTO> mProductList = new ArrayList<ProductDataDTO>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private TreeSet<Integer> sectionAd = new TreeSet<Integer>();
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private int roleId;
    CustomButtonListener customButtonListener;
    CustomItemListener customItemListener;
    CustomHideListener customHideListener;

    public void clear() {
        mProductList.clear();
        sectionHeader.clear();
        sectionAd.clear();
    }

    public DashboardProductListAdapter(Context context, int roleId) {
        this.mContext = context;
        this.roleId = roleId;
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

    public void addSectionAdItem(final ProductDataDTO item) {
        mProductList.add(item);
        sectionAd.add(mProductList.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        int id;
        if (sectionHeader.contains(position)) {
            id = TYPE_SEPARATOR;
        } else if (sectionAd.contains(position)) {
            id = TYPE_Ad;
        } else {
            id = TYPE_ITEM;
        }
        return id;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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
                    convertView = mInflater.inflate(R.layout.row_dashboard_product_list, null);
                    holder.imgProductImage = (NetworkImageView) convertView.findViewById(R.id.productImage);
                    holder.txtProductTitle = (RobotoMediumTextView) convertView.findViewById(R.id.txtProductTitle);
                    holder.txtProductPrice = (RobotoMediumTextView) convertView.findViewById(R.id.txtProductPrice);
                    holder.txtDesc = (RobotoRegularTextView) convertView.findViewById(R.id.txtDesc);
                    holder.txtDistance = (RobotoItalicTextView) convertView.findViewById(R.id.txtDistance);
                    holder.imgFavourite = (ImageView) convertView.findViewById(R.id.imgFavourite);
                    holder.btnHide = (TextView) convertView.findViewById(R.id.btnHideAd);
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.row_dashboard_product_list_header, null);
                    holder.txtDate = (RobotoMediumTextView) convertView.findViewById(R.id.txtDate);
                    break;
                case TYPE_Ad:
                    convertView = mInflater.inflate(R.layout.row_product_list_header, null);
                    holder.adViewContainer = (RelativeLayout) convertView.findViewById(R.id.adViewContainer);
                    //holder.txtDate = (RobotoMediumTextView) convertView.findViewById(R.id.txtDate);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            final ProductDataDTO product = mProductList.get(position);
            switch (rowType) {
                case TYPE_ITEM:
                    holder.txtProductTitle.setText(product.getProductName());
                    holder.txtDesc.setText(product.getProductDesc());
                    holder.txtDistance.setText(String.format("%.2f", product.getDistance()) + " km away from you");
                /*int imgResId = mContext.getResources().getIdentifier(product.getProductImage(), "drawable", "com.nearbypets");
                holder.imgProductImage.setImageResource(imgResId);*/
                    holder.imgProductImage.setImageResource(R.drawable.default_pet_image);
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
                    if (roleId == AppConstants.ROLL_ID_ADMIN) {
                        holder.imgFavourite.setVisibility(View.GONE);
                    }
                    if (roleId == AppConstants.ROLL_ID_USER) {
                        holder.btnHide.setVisibility(View.GONE);
                    } else {
                        holder.btnHide.setVisibility(View.VISIBLE);
                    }
                    holder.btnHide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (customHideListener != null)
                                customHideListener.onHideClickListener(position, product);
                        }
                    });
                    break;
                case TYPE_SEPARATOR:
                    DateUtils date = new DateUtils();
                    holder.txtDate.setText("Posted On " + date.getLocalDateInFormat(product.getPostedDt()));
                    //holder.txtDate.setText("Posted On " + product.getDate());
                    break;
                case TYPE_Ad:

                    AdView adView = new AdView(mContext, "1715459422041023_1715460668707565", AdSize.BANNER_320_50);
                    holder.adViewContainer.addView(adView);
                    AdSettings.addTestDevice("HASHED ID");
                    adView.loadAd();
                    break;

            }
            return convertView;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }


    }

    public static class ViewHolder {
        RobotoMediumTextView txtProductTitle;
        RobotoMediumTextView txtProductPrice;
        RobotoRegularTextView txtDesc;
        RobotoItalicTextView txtDistance;
        ImageView imgFavourite;
        NetworkImageView imgProductImage;
        RobotoMediumTextView txtDate;
        RelativeLayout adViewContainer;
        TextView btnHide;
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
