package com.dagla.android.adapter;

import android.app.Activity;
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

public class SubCategoriesAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;


    static class ViewHolder {
        public TextView subCategoryName;
    }

    public SubCategoriesAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_sub_categories_item, names);
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

            if(GlobalFunctions.getLang(context).equals("ar")){
                rowView = inflater.inflate(R.layout.layout_sub_categories_item_ar, null);
            }else {
                rowView = inflater.inflate(R.layout.layout_sub_categories_item, null);
            }

            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.subCategoryName = rowView.findViewById(R.id.subCategoryName);
            //
            rowView.setTag(viewHolder);
        }



        final ViewHolder holder = (ViewHolder)rowView.getTag();

//        holder.subCategoryName.setTypeface(custom_fontnormal);

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);


            if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
                holder.subCategoryName.setText(obj.getString("cat_ar"));
            }else {
                holder.subCategoryName.setText(obj.getString("cat_en"));
            }


        } catch (JSONException e) {

            Log.d("SubCategoriesAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }
}
