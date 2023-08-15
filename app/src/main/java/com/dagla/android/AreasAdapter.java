package com.dagla.android;

import android.app.Activity;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AreasAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;

    static class ViewHolder {
        public TextView lblArea;
    }

    public AreasAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_select_area, names);
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
            rowView = inflater.inflate(R.layout.layout_select_area, null);
            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.lblArea = (TextView)rowView.findViewById(R.id.lblArea);
            //
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        String s = names.get(position);

        try {

            JSONObject obj = new JSONObject(s);

            String area = obj.getString("area");

            if (obj.getInt("area_id") == 0) {
                //
                holder.lblArea.setTypeface(null, Typeface.BOLD);
                holder.lblArea.setTextSize(18);
                //
                holder.lblArea.setText(area);
                //
            } else {
                //
                holder.lblArea.setTypeface(null, Typeface.NORMAL);
                holder.lblArea.setTextSize(16);
                //
                holder.lblArea.setText(String.format(Locale.US, "  %s", area));
                //
            }

        } catch (JSONException e) {

            Log.d("AreasAdapter", "JSONException:" + e.getMessage());
        }

        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {

        String s = names.get(position);

        int areaId = 0;

        try {

            JSONObject obj = new JSONObject(s);

            areaId = obj.getInt("area_id");

        } catch (JSONException e) {

            Log.d("AreasAdapter", "JSONException:" + e.getMessage());
        }

        if (areaId == 0) {

            return false;

        }

        return true;
    }

}