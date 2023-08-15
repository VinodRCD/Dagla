package com.dagla.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {

    private final Activity context;
    private final ArrayList<String> names;
    private final int width;

    public ItemsAdapter(Activity context, ArrayList<String> names, int width) {
        this.context = context;
        this.names = names;
        this.width = width;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout pnlHolder;
        ImageView imgPic;
        TextView lblNew, lblSale, lblBrand, lblName, lblPrice, lblOldPrice;

        public MyViewHolder(View view) {

            super(view);

            pnlHolder = view.findViewById(R.id.pnlHolder);
            imgPic = view.findViewById(R.id.imgPic);
            lblNew = view.findViewById(R.id.lblNew);
            lblSale = view.findViewById(R.id.lblSale);
            lblBrand = view.findViewById(R.id.lblBrand);
            lblName = view.findViewById(R.id.lblName);
            lblPrice = view.findViewById(R.id.lblPrice);
            lblOldPrice = view.findViewById(R.id.lblOldPrice);

            lblOldPrice.setPaintFlags(lblOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        if(GlobalFunctions.getLang(context).equals("ar")){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_ar, parent, false);
        }else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)holder.pnlHolder.getLayoutParams();
        ViewGroup.LayoutParams paramsImg = holder.imgPic.getLayoutParams();

        int w = GlobalFunctions.convertDpToPx(context, 180);
        int h = GlobalFunctions.convertDpToPx(context, 370);

        int nw = (width - GlobalFunctions.convertDpToPx(context, 45)) / 2;
        int nh = h;

        if (nw != w) {

            float ratio = (float)nw / (float)w;

            nh = (int)(h * ratio);
        }

        params.width = nw;
        params.height = nh;

        holder.pnlHolder.setLayoutParams(params);

        h = GlobalFunctions.convertDpToPx(context, 226);

        if (nw != w) {

            float ratio = (float)nw / (float)w;

            nh = (int)(h * ratio);
        }

        paramsImg.height = nh;

        holder.imgPic.setLayoutParams(paramsImg);

        final String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

            holder.imgPic.setImageDrawable(null);

            String pic = obj.getString("pic");

            if (!pic.equalsIgnoreCase("")) {

                ImageLoader.getInstance().displayImage(pic, holder.imgPic);

            }

            if (obj.has("brand_name")) {

                holder.lblBrand.setText(obj.getString("brand_name"));

            }

            holder.lblName.setText(obj.getString("name"));
            holder.lblPrice.setText(obj.getString("price"));
            holder.lblOldPrice.setText(obj.getString("old_price"));

            holder.lblNew.setVisibility(View.GONE);
            holder.lblSale.setVisibility(View.GONE);

            if (!holder.lblOldPrice.getText().toString().equalsIgnoreCase("")) {

                holder.lblSale.setVisibility(View.VISIBLE);

            } else if (obj.getBoolean("is_new")) {

                holder.lblNew.setVisibility(View.VISIBLE);
            }

            holder.pnlHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ItemDetailsActivity.class);

                    intent.putExtra("obj", s);

                    context.startActivity(intent);

                }
            });

        } catch (JSONException e) {

            Log.d("JSONException", e.getMessage());

        }

    }

    @Override
    public int getItemCount() {

        return names.size();

    }

}