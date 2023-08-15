package com.dagla.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.CartFragmentNew;
import com.dagla.android.fragments.ProductDescriptionFromHomeFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartAdapterNew extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
//    CartFragment fragment;
    CartFragmentNew fragment2;

    FragmentActivity activity;
    FragmentTransaction ft;

    static class ViewHolder {
        public ImageView imgPic, imgDelete,imgArrow;
        public TextView lblName, lblPrice, lblQty, lblBrand,lblColor,lblSize;
//        public Button btnMinus, btnPlus;
        public RelativeLayout itemLayout;
        public LinearLayout wishlistLayout;
        public RelativeLayout qtyLayout,sizeLayout;
    }


    public CartAdapterNew(Activity context, ArrayList<String> names, CartFragmentNew fragment2) {
        super(context, R.layout.layout_cart_new, names);
        this.context = context;
        this.names = names;
        this.fragment2 = fragment2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            //
            LayoutInflater inflater = context.getLayoutInflater();
            if(GlobalFunctions.getLang(context).equals("ar")){
                rowView = inflater.inflate(R.layout.layout_cart_ar_new, null);
            }else {
                rowView = inflater.inflate(R.layout.layout_cart_new, null);
            }

            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.imgPic = (ImageView)rowView.findViewById(R.id.imgPic);
            viewHolder.imgDelete = (ImageView)rowView.findViewById(R.id.imgDelete);
            viewHolder.lblName = (TextView)rowView.findViewById(R.id.lblName);
            viewHolder.lblPrice = (TextView)rowView.findViewById(R.id.lblPrice);
//            viewHolder.btnMinus = (Button)rowView.findViewById(R.id.btnMinus);
            viewHolder.lblQty = (TextView)rowView.findViewById(R.id.lblQty);
//            viewHolder.btnPlus = (Button)rowView.findViewById(R.id.btnPlus);
            viewHolder.imgArrow = (ImageView) rowView.findViewById(R.id.imgArrow);

            viewHolder.lblBrand = (TextView) rowView.findViewById(R.id.lblBrand);

            viewHolder.itemLayout = (RelativeLayout) rowView.findViewById(R.id.itemLayout);

            viewHolder.wishlistLayout = (LinearLayout) rowView.findViewById(R.id.wishlistLayout);
            viewHolder.qtyLayout = (RelativeLayout) rowView.findViewById(R.id.qtyLayout);
            viewHolder.sizeLayout = (RelativeLayout) rowView.findViewById(R.id.sizeLayout);

            viewHolder.lblColor = (TextView) rowView.findViewById(R.id.lblColor);
            viewHolder.lblSize = (TextView) rowView.findViewById(R.id.lblSize);


            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder)rowView.getTag();

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);
            //
            holder.imgPic.setImageDrawable(null);
            //
            ImageLoader.getInstance().displayImage(obj.getString("pic"), holder.imgPic);
            //

//            holder.lblName.setTypeface(custom_fontnormal);
//            holder.lblPrice.setTypeface(custom_fontnormal);
//            holder.lblQty.setTypeface(custom_fontnormal);

            holder.lblName.setText(obj.getString("name"));
            holder.lblPrice.setText(obj.getString("price"));

            if(GlobalFunctions.getLang(context).equals("ar")){
                holder.lblQty.setText("الكمية : "+obj.getString("quantity"));
                holder.lblColor.setText(context.getResources().getString(R.string.color_ar)+": "+obj.getString("color_name"));
                holder.lblSize.setText(context.getResources().getString(R.string.size_ar)+": "+obj.getString("size"));
            }else {

                holder.lblQty.setText("Qty : "+obj.getString("quantity"));
                holder.lblColor.setText(context.getResources().getString(R.string.color)+": "+obj.getString("color_name"));
                holder.lblSize.setText(context.getResources().getString(R.string.size)+": "+obj.getString("size"));
            }


            if(obj.getString("color_name").equals("")){
                holder.lblColor.setVisibility(View.GONE);
            }else {
                holder.lblColor.setVisibility(View.VISIBLE);
            }

            if(obj.getString("size").equals("")){
                holder.sizeLayout.setVisibility(View.GONE);
            }else {
                holder.sizeLayout.setVisibility(View.VISIBLE);
            }


            holder.lblBrand.setText(obj.getString("brand_name"));


            if (!GlobalFunctions.getPrefrences(context, "user_id").equalsIgnoreCase("")) {
                holder.wishlistLayout.setVisibility(View.VISIBLE);
            }else {
                holder.wishlistLayout.setVisibility(View.GONE);
            }



            holder.wishlistLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragment2.moveToWishlist(position);

                }
            });
            //

//            holder.imgArrow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    fragment2.quantityChange(position);
//
//                }
//            });

            holder.qtyLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment2.quantityChange(position);
                }
            });

            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragment2.delCartItem(position);

                }
            });

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


//                    shareIntent();

                }
            });

        } catch (JSONException e) {

            Log.d("CartAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }

    public void shareIntent(){

        Intent sharingIntent1 = new Intent(Intent.ACTION_SEND);
        sharingIntent1.setType("text/plain");
        String shareBody1 = "\nDownload Application\n\n";
        shareBody1 = shareBody1 +"https://play.google.com/store/apps/details?id=com.dagla.android";
        sharingIntent1.putExtra(Intent.EXTRA_SUBJECT, "Marca");
        sharingIntent1.putExtra(Intent.EXTRA_TEXT, shareBody1);
        context.startActivity(Intent.createChooser(sharingIntent1, "Share via"));

    }
}
