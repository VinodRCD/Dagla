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

public class ColorsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;

    static class ViewHolder {
        public TextView lblColor;
    }

    public ColorsAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_select_color, names);
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
            rowView = inflater.inflate(R.layout.layout_select_color, null);
            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.lblColor = rowView.findViewById(R.id.lblColor);
            //
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        String s = names.get(position);

        try {

            JSONObject obj = new JSONObject(s);

            holder.lblColor.setText(obj.getString("color_name"));

        } catch (JSONException e) {

            Log.d("SizesAdapter", "JSONException:" + e.getMessage());
        }

        return rowView;
    }
}
