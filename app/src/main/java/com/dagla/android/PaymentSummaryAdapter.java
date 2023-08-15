package com.dagla.android;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentSummaryAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;

    static class ViewHolder {
        public TextView lblName, lblPrice;
    }

    public PaymentSummaryAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_payment_summary, names);
        this.context = context;
        this.names = names;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            //
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.layout_payment_summary, null);
            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.lblName = (TextView)rowView.findViewById(R.id.lblName);
            viewHolder.lblPrice = (TextView)rowView.findViewById(R.id.lblPrice);
            //
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder)rowView.getTag();

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

            holder.lblName.setText(obj.getString("name"));
            holder.lblPrice.setText(obj.getString("value"));

        } catch (JSONException e) {

            Log.d("PaymentSummaryAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }

}