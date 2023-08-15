package com.dagla.android.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;

import java.util.ArrayList;

/**
 * Created by sys on 22-Jan-19.
 */

public class BrandsAdapter extends BaseAdapter {

    private final Activity context;
    private final ArrayList<String> arrNames;


    static class ViewHolder {
        public TextView brandName;

    }

    public BrandsAdapter(Activity context, ArrayList<String> arrNames) {
//        super(context, R.layout.layout_categories_item, arrNames);
        this.context = context;
        this.arrNames = arrNames;

    }

    @Override
    public int getCount() {
        return arrNames.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            //
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.layout_brands_item, null);
            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.brandName = rowView.findViewById(R.id.brandName);


            rowView.setTag(viewHolder);
        }

        int w = GlobalFunctions.convertDpToPx(context, 375);
        int h = GlobalFunctions.convertDpToPx(context, 150);


        final ViewHolder holder = (ViewHolder)rowView.getTag();

        holder.brandName.setText(arrNames.get(position).toString());


//        String s = arrNames.get(position);
//
//        try {
//
//            final JSONObject obj = new JSONObject(s);
//
//            holder.categoryImg.setImageDrawable(null);
//
//            ImageLoader.getInstance().displayImage(obj.getString("image"), holder.categoryImg);
//
//        } catch (JSONException e) {
//
//            Log.d("CategoriesAdapter", "JSONException:" + e.getMessage());
//
//        }

        return rowView;
    }
}
