package com.dagla.android.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrdersAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;


    static class ViewHolder {
        public TextView lblOrderID, lblPrice, lblDate, lblItemsCount, lblStatus;
    }

    public OrdersAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_order, names);
        this.context = context;
        this.names = names;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();

            if(GlobalFunctions.getLang(context).equals("ar")){
                rowView = inflater.inflate(R.layout.layout_order_ar, null);
            }else {
                rowView = inflater.inflate(R.layout.layout_order, null);
            }

            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.lblOrderID = (TextView)rowView.findViewById(R.id.lblOrderID);
            viewHolder.lblPrice = (TextView)rowView.findViewById(R.id.lblPrice);
            viewHolder.lblDate = (TextView)rowView.findViewById(R.id.lblDate);
            viewHolder.lblItemsCount = (TextView)rowView.findViewById(R.id.lblItemsCount);
            viewHolder.lblStatus = (TextView)rowView.findViewById(R.id.lblStatus);
            //
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        String s = names.get(position);

        try {

            JSONObject obj = new JSONObject(s);

//            holder.lblOrderID.setTypeface(custom_fontmedium);
//            holder.lblPrice.setTypeface(custom_fontbold);
//            holder.lblDate.setTypeface(custom_fontmedium);
//            holder.lblItemsCount.setTypeface(custom_fontmedium);
//            holder.lblStatus.setTypeface(custom_fontmedium);

            holder.lblOrderID.setText(obj.getString("order_id_custom"));
            holder.lblPrice.setText(obj.getString("price"));
            holder.lblDate.setText(obj.getString("date"));
            holder.lblItemsCount.setText(obj.getString("items_count"));
            holder.lblStatus.setText(obj.getString("status"));
            holder.lblStatus.setTextColor(Color.parseColor(obj.getString("status_color")));

        } catch (JSONException e) {

            Log.d("OrdersAdapter", "JSONException:" + e.getMessage());
        }

        return rowView;
    }

}