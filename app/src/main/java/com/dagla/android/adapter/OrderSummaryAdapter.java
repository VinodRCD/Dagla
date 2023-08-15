package com.dagla.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class OrderSummaryAdapter extends RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder> {

    Context mContext;
    private final ArrayList<String> names;


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout pnlLine;
        public TextView lblBrand, lblName, lblPrice, lblDeliveryDateTime;

        public ViewHolder(View itemView) {
            super(itemView);

            lblBrand = itemView.findViewById(R.id.lblBrand);
            lblName = itemView.findViewById(R.id.lblName);
            lblPrice = itemView.findViewById(R.id.lblPrice);
            lblDeliveryDateTime = itemView.findViewById(R.id.lblDeliveryDateTime);


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
    public OrderSummaryAdapter(Activity mActivity, ArrayList<String> names) {
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
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_summary_ar, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_summary, parent, false);
        }


        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder1, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element holder1.imgLike.setTag(position);


        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

            holder1.lblBrand.setText(obj.getString("brand_name"));

            holder1.lblName.setText(obj.getString("name"));

            if (obj.getInt("quantity") > 1) {

                holder1.lblName.setText(String.format(Locale.US, "%s x %s", obj.getString("name"), obj.getString("quantity")));
            }

            holder1.lblPrice.setText(obj.getString("price"));

            holder1.lblDeliveryDateTime.setText(String.format(Locale.US, "%s %s",
                    mContext.getString(R.string.delivery_date_), obj.getString("delivery_date")));

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