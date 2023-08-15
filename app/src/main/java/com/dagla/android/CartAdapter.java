package com.dagla.android;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;

    static class ViewHolder {
        public ImageView imgPic, imgDelete;
        public TextView lblName, lblPrice, lblQty;
        public Button btnMinus, btnPlus;
    }

    public CartAdapter(Activity context, ArrayList<String> names) {
        super(context, R.layout.layout_cart, names);
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
            rowView = inflater.inflate(R.layout.layout_cart, null);
            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.imgPic = (ImageView)rowView.findViewById(R.id.imgPic);
            viewHolder.imgDelete = (ImageView)rowView.findViewById(R.id.imgDelete);
            viewHolder.lblName = (TextView)rowView.findViewById(R.id.lblName);
            viewHolder.lblPrice = (TextView)rowView.findViewById(R.id.lblPrice);
            viewHolder.btnMinus = (Button)rowView.findViewById(R.id.btnMinus);
            viewHolder.lblQty = (TextView)rowView.findViewById(R.id.lblQty);
            viewHolder.btnPlus = (Button)rowView.findViewById(R.id.btnPlus);
            //
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder)rowView.getTag();

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);
            //
            holder.imgPic.setImageDrawable(null);
            //
            ImageLoader.getInstance().displayImage(obj.getString("pic"), holder.imgPic);
            //
            holder.lblName.setText(obj.getString("name"));
            holder.lblPrice.setText(obj.getString("price"));
            holder.lblQty.setText(obj.getString("quantity"));
            //
            holder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((CartActivity)context).quantityMinus(position);

                }
            });
            //
            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((CartActivity)context).quantityPlus(position);

                }
            });
            //
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((CartActivity)context).delCartItem(position);

                }
            });

        } catch (JSONException e) {

            Log.d("CartAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }

}