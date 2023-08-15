package com.dagla.android;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeBannersAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    private final int width;

    static class ViewHolder {
        public ImageView imgPic;
    }

    public HomeBannersAdapter(Activity context, ArrayList<String> names, int width) {
        super(context, R.layout.layout_home_banner, names);
        this.context = context;
        this.names = names;
        this.width = width;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            //
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.layout_home_banner, null);
            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.imgPic = rowView.findViewById(R.id.imgPic);
            //
            rowView.setTag(viewHolder);
        }

        int w = GlobalFunctions.convertDpToPx(context, 375);
        int h = GlobalFunctions.convertDpToPx(context, 192);

        int nw = width;
        int nh = h;

        if (nw != w) {

            float ratio = (float)nw / (float)w;

            nh = (int)(h * ratio);
        }

        final ViewHolder holder = (ViewHolder)rowView.getTag();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.imgPic.getLayoutParams();

        params.height = nh;

        holder.imgPic.setLayoutParams(params);

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

            holder.imgPic.setImageDrawable(null);

            ImageLoader.getInstance().displayImage(obj.getString("image"), holder.imgPic);

        } catch (JSONException e) {

            Log.d("HomeBannersAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }

}