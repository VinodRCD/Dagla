package com.dagla.android.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.ProductDescriptionFromHomeFragment;
import com.dagla.android.fragments.WishListFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WishlistAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;

    WishListFragment fragment;

    FragmentActivity activity;
    FragmentTransaction ft;

    static class ViewHolder {
        public TextView lblName, lblPrice,lblBrand;
        public ImageView imgPic,imgDelete;
        public Button btnAddToCart;
    }

    public WishlistAdapter(Activity context, ArrayList<String> names, WishListFragment fragment) {
        super(context, R.layout.layout_wishlist, names);
        this.context = context;
        this.names = names;
        this.fragment = fragment;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();

            if(GlobalFunctions.getLang(context).equals("ar")){
                rowView = inflater.inflate(R.layout.layout_wishlist_ar, null);
            }else {
                rowView = inflater.inflate(R.layout.layout_wishlist, null);
            }

            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.lblBrand = (TextView)rowView.findViewById(R.id.lblBrand);
            viewHolder.lblName = (TextView)rowView.findViewById(R.id.lblName);
            viewHolder.lblPrice = (TextView)rowView.findViewById(R.id.lblPrice);

            viewHolder.imgPic = (ImageView) rowView.findViewById(R.id.imgPic);
            viewHolder.imgDelete = (ImageView) rowView.findViewById(R.id.imgDelete);

            viewHolder.btnAddToCart = (Button) rowView.findViewById(R.id.btnAddToCart);
            //
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder) rowView.getTag();

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

//            holder.lblName.setTypeface(custom_fontmedium);
//            holder.lblPrice.setTypeface(custom_fontmedium);
//            holder.btnAddToCart.setTypeface(custom_fontmedium);

            holder.lblBrand.setText(obj.getString("brand_name"));
            holder.lblName.setText(obj.getString("name"));
            holder.lblPrice.setText(obj.getString("price"));

            holder.imgPic.setImageDrawable(null);
            //
            ImageLoader.getInstance().displayImage(obj.getString("pic"), holder.imgPic);


            holder.imgPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                    Bundle b = new Bundle();
                    try {
                        b.putString("product_id", ""+obj.getString("product_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    b.putString("title", ""+holder.lblName.getText().toString());
                    fragment.setArguments(b);


                    activity = ((FragmentActivity) context);
                    ft = activity.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commitAllowingStateLoss();


                }
            });

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragment.delWishlistItem(position);

                }
            });


            holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragment.delFromWishlistItem(position);

                }
            });

        } catch (JSONException e) {

            Log.d("WishlistAdapter", "JSONException:" + e.getMessage());
        }



        return rowView;
    }
}
