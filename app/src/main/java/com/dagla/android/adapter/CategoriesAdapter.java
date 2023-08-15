package com.dagla.android.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sys on 21-Jan-19.
 */

public class CategoriesAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;


    static class ViewHolder {
        public TextView subCategoryName;
    }

    public CategoriesAdapter(Activity context, ArrayList<String> names) {
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
            SubCategoriesAdapter.ViewHolder viewHolder = new SubCategoriesAdapter.ViewHolder();
            //
            viewHolder.subCategoryName = rowView.findViewById(R.id.subCategoryName);
            //
            rowView.setTag(viewHolder);
        }



        final SubCategoriesAdapter.ViewHolder holder = (SubCategoriesAdapter.ViewHolder)rowView.getTag();

//        holder.subCategoryName.setTypeface(custom_fontnormal);

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);


            if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
                holder.subCategoryName.setText(obj.getString("banner_name_ar"));
            }else {
                holder.subCategoryName.setText(obj.getString("banner_name"));
            }


        } catch (JSONException e) {

            Log.d("CategoriesAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }
}
