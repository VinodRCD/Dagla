package com.dagla.android;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class M3nuAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;

    static class ViewHolder {
        public LinearLayout pnlHolder;
        public TextView lblName;
    }

    public M3nuAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_menu, names);
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
            rowView = inflater.inflate(R.layout.layout_menu, null);
            //
            M3nuAdapter.ViewHolder viewHolder = new M3nuAdapter.ViewHolder();
            //
            viewHolder.pnlHolder = rowView.findViewById(R.id.pnlHolder);
            viewHolder.lblName = rowView.findViewById(R.id.lblName);
            //
            rowView.setTag(viewHolder);
        }

        final M3nuAdapter.ViewHolder holder = (M3nuAdapter.ViewHolder)rowView.getTag();

        String s = names.get(position);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)holder.lblName.getLayoutParams();

        params.setMarginStart(0);

        int ten = GlobalFunctions.convertDpToPx(context, 10);
        int fifteen = GlobalFunctions.convertDpToPx(context, 15);

        try {

            final JSONObject obj = new JSONObject(s);

            if (obj.getString("is_heading").equalsIgnoreCase("1")) {

                holder.lblName.setText(obj.getString("name").toUpperCase());

                holder.lblName.setTextSize(14f);

            } else {

                if (obj.getString("type").equalsIgnoreCase("shop")) {

                    holder.lblName.setText(obj.getString("name").toUpperCase());

                    holder.lblName.setTextSize(12f);

                    holder.lblName.setTextColor(Color.parseColor("#505050"));

                    params.setMarginStart(fifteen);

                } else {

                    holder.lblName.setText(obj.getString("name").toUpperCase());

                    holder.lblName.setTextSize(13f);

                    holder.lblName.setTextColor(Color.parseColor("#404040"));

                    params.setMarginStart(ten);
                }

            }

        } catch (JSONException e) {

            Log.d("M3nuAdapter", "JSONException:" + e.getMessage());

        }

        holder.lblName.setLayoutParams(params);

        return rowView;
    }

}
