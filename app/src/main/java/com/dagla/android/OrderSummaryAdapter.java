package com.dagla.android;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class OrderSummaryAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;

    Typeface custom_fontbold, custom_fontnormal;

    static class ViewHolder {
        public RelativeLayout pnlLine;
        public TextView lblBrand, lblName, lblPrice, lblDeliveryDateTime;
    }

    public OrderSummaryAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_order_summary, names);
        this.context = context;
        this.names = names;
        custom_fontbold = Typeface.createFromAsset(context.getAssets(), "fonts/avenir-next-bold.ttf");
        custom_fontnormal = Typeface.createFromAsset(context.getAssets(), "fonts/avenir-next-regular.ttf");
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            //
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.layout_order_summary, null);
            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.pnlLine = rowView.findViewById(R.id.pnlLine);
            viewHolder.lblBrand = rowView.findViewById(R.id.lblBrand);
            viewHolder.lblName = rowView.findViewById(R.id.lblName);
            viewHolder.lblPrice = rowView.findViewById(R.id.lblPrice);
            viewHolder.lblDeliveryDateTime = rowView.findViewById(R.id.lblDeliveryDateTime);

//            viewHolder.lblBrand.setTypeface(custom_fontnormal);
//            viewHolder.lblName.setTypeface(custom_fontnormal);
//            viewHolder.lblPrice.setTypeface(custom_fontbold);
//            viewHolder.lblDeliveryDateTime.setTypeface(custom_fontnormal);

            //
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder)rowView.getTag();

        if (names.size()-1 == position) {

            holder.pnlLine.setVisibility(View.GONE);

        } else {

            holder.pnlLine.setVisibility(View.VISIBLE);
        }

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

            holder.lblBrand.setText(obj.getString("brand_name"));

            holder.lblName.setText(obj.getString("name"));

            if (obj.getInt("quantity") > 1) {

                holder.lblName.setText(String.format(Locale.US, "%s x %s", obj.getString("name"), obj.getString("quantity")));
            }

            holder.lblPrice.setText(obj.getString("price"));

            holder.lblDeliveryDateTime.setText(String.format(Locale.US, "%s %s",
                    context.getString(R.string.delivery_date_), obj.getString("delivery_date")));

        } catch (JSONException e) {

            Log.d("OrderSummaryAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }
}