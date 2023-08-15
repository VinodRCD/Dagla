package com.dagla.android.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.CartFragmentNew;
import com.dagla.android.fragments.OrdersListFragment;
import com.dagla.android.fragments.ProductDescriptionFromHomeFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    //    CartFragment fragment;
    OrdersListFragment fragment2;

    FragmentActivity activity;
    FragmentTransaction ft;

    static class ViewHolder {
        public ImageView imgPic;
        public TextView lblName, lblPrice, lblQty, lblExchangedDate,lblSize,lblExchangedDesc;
        //        public Button btnMinus, btnPlus;
        public RelativeLayout itemLayout;
        public Button btnReturns,btnExchange;
        public LinearLayout buttonsLayout1,buttonsLayout2;
    }


    public OrderListAdapter(Activity context, ArrayList<String> names, OrdersListFragment fragment2) {
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
                rowView = inflater.inflate(R.layout.layout_order_list_item_ar_new, null);
            }else {
                rowView = inflater.inflate(R.layout.layout_order_list_item_new, null);
            }


            ViewHolder viewHolder = new ViewHolder();

            viewHolder.imgPic = (ImageView)rowView.findViewById(R.id.imgPic);
            viewHolder.lblName = (TextView)rowView.findViewById(R.id.lblName);
            viewHolder.lblPrice = (TextView)rowView.findViewById(R.id.lblPrice);
            viewHolder.lblQty = (TextView)rowView.findViewById(R.id.lblQty);
            viewHolder.lblSize = (TextView)rowView.findViewById(R.id.lblSize);
            viewHolder.lblExchangedDesc = (TextView)rowView.findViewById(R.id.lblExchangedDesc);

            viewHolder.itemLayout = (RelativeLayout) rowView.findViewById(R.id.itemLayout);


            viewHolder.btnReturns = (Button) rowView.findViewById(R.id.btnReturns);
            viewHolder.btnExchange = (Button) rowView.findViewById(R.id.btnExchange);

            viewHolder.buttonsLayout1 = (LinearLayout) rowView.findViewById(R.id.buttonsLayout1);
            viewHolder.buttonsLayout2 = (LinearLayout) rowView.findViewById(R.id.buttonsLayout2);
            viewHolder.lblExchangedDate = (TextView) rowView.findViewById(R.id.lblExchangedDate);

            //
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder)rowView.getTag();

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);
            //
            holder.imgPic.setImageDrawable(null);

            ImageLoader.getInstance().displayImage(obj.getString("pic"), holder.imgPic);

            holder.lblName.setText(obj.getString("item_name"));
            holder.lblPrice.setText(obj.getString("price"));

            holder.lblQty.setText("x"+obj.getString("quantity"));

            holder.lblExchangedDate.setText(obj.getString("exchangedate"));
            if(GlobalFunctions.getLang(context).equals("ar")){
                holder.lblSize.setText(obj.getString("size")+" : "+context.getResources().getString(R.string.size_ar));
            }else {
                holder.lblSize.setText(context.getResources().getString(R.string.size)+" : "+obj.getString("size"));
            }

            holder.lblExchangedDesc.setText(obj.getString("exchangedesc"));

//            if(obj.getString("exchangestatus").equals("0")){
//                holder.buttonsLayout1.setVisibility(View.VISIBLE);
//                holder.buttonsLayout2.setVisibility(View.GONE);
//            }else {
//                holder.buttonsLayout1.setVisibility(View.GONE);
//                holder.buttonsLayout2.setVisibility(View.VISIBLE);
//            }


            if(obj.optBoolean("showRetExchButton")){
//                if(obj.getString("exchangestatus").equals("1")){
//                    holder.buttonsLayout1.setVisibility(View.GONE);
//                    holder.buttonsLayout2.setVisibility(View.VISIBLE);
//                }else if(obj.getString("exchangestatus").equals("0")){
//                    holder.buttonsLayout1.setVisibility(View.VISIBLE);
//                    holder.buttonsLayout2.setVisibility(View.GONE);
//                }

                holder.buttonsLayout1.setVisibility(View.VISIBLE);
                holder.buttonsLayout2.setVisibility(View.GONE);

            }else {
                if(obj.getString("exchangestatus").equals("1")){
                    holder.buttonsLayout1.setVisibility(View.GONE);
                    holder.buttonsLayout2.setVisibility(View.VISIBLE);
                }else if(obj.getString("exchangestatus").equals("0")){
                    holder.buttonsLayout1.setVisibility(View.GONE);
                    holder.buttonsLayout2.setVisibility(View.GONE);
                }
            }


            holder.btnReturns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        fragment2.returnsDialog(context,""+obj.getString("detailed_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            holder.btnExchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        fragment2.exchangeDialog(context,""+obj.getString("detailed_id"),""+obj.getString("product_id"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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


                }
            });

        } catch (JSONException e) {

            Log.d("CartAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }

}
