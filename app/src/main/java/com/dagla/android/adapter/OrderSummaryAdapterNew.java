package com.dagla.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.CheckoutPaymentFragmentNew;
import com.dagla.android.fragments.OrdersListFragment;
import com.dagla.android.fragments.ProductDescriptionFromHomeFragment;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class OrderSummaryAdapterNew extends RecyclerView.Adapter<OrderSummaryAdapterNew.ViewHolder> {

    Context mContext;
    private final ArrayList<String> names;

    FragmentActivity activity;
    FragmentTransaction ft;

    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgPic;
        public TextView lblName, lblPrice, lblQty, lblColor;
        public RelativeLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);

//            lblBrand = itemView.findViewById(R.id.lblBrand);
            lblName = itemView.findViewById(R.id.lblName);
            lblPrice = itemView.findViewById(R.id.lblPrice);
//            lblDeliveryDateTime = itemView.findViewById(R.id.lblDeliveryDateTime);


            imgPic = (ImageView)itemView.findViewById(R.id.imgPic);
            lblName = (TextView)itemView.findViewById(R.id.lblName);
            lblPrice = (TextView)itemView.findViewById(R.id.lblPrice);
            lblQty = (TextView)itemView.findViewById(R.id.lblQty);
            lblColor = (TextView)itemView.findViewById(R.id.lblColor);

            itemLayout = (RelativeLayout) itemView.findViewById(R.id.itemLayout);

        }


    }

//    public void add(int position, String item) {
//        mDataset.add(position, item);
//        notifyItemInserted(position);
//    }
//
//    public void remove(String item) {
//        int position = mDataset.indexOf(item);
//        mDataset.remove(position);
//        notifyItemRemoved(position);
//    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderSummaryAdapterNew(Activity mActivity, ArrayList<String> names) {
        super();
        this.mContext = mActivity;
        this.names = names;


    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v;

        if(GlobalFunctions.getLang(mContext).equals("ar")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_list_item_ar, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_list_item, parent, false);
        }


        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder1, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element holder1.imgLike.setTag(position);


        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

            holder1.imgPic.setImageDrawable(null);

            ImageLoader.getInstance().displayImage(obj.getString("pic"), holder1.imgPic);

            holder1.lblName.setText(obj.getString("name"));
            holder1.lblPrice.setText(obj.getString("price"));

            holder1.lblQty.setText("x"+obj.getString("quantity"));

            if(GlobalFunctions.getLang(mContext).equals("ar")){
                holder1.lblColor.setText(mContext.getResources().getString(R.string.color_ar)+": "+obj.getString("color_name"));
            }else {
                holder1.lblColor.setText(mContext.getResources().getString(R.string.color)+": "+obj.getString("color_name"));
            }

            if(obj.getString("color_name").equals("")){
                holder1.lblColor.setVisibility(View.GONE);
            }else {
                holder1.lblColor.setVisibility(View.VISIBLE);
            }


            if (!obj.getString("color_name").equals("")){
                holder1.lblColor.setText(obj.getString("color_name"));
            }else {
                holder1.lblColor.setText(obj.getString("color_name"));
            }


            holder1.imgPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


//                    ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
//                    Bundle b = new Bundle();
//                    try {
//                        b.putString("product_id", ""+obj.getString("product_id"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    b.putString("title", ""+holder1.lblName.getText().toString());
//                    fragment.setArguments(b);
//
//
//                    activity = ((FragmentActivity) mContext);
//                    ft = activity.getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
//                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                    ft.addToBackStack(null);
//                    ft.commitAllowingStateLoss();


                }
            });

        } catch (JSONException e) {

            Log.d("OrderSummaryAdapter", "JSONException:" + e.getMessage());

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return names.size();
    }
}
