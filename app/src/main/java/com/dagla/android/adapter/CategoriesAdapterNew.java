package com.dagla.android.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.CategoriesFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesAdapterNew extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
//    private final int width;
    CategoriesFragment fragment;

    static class ViewHolder {
        public ImageView categoryImg;
        public TextView categoryName;
        public ProgressBar progress;
    }

//    public CategoriesAdapterNew(Activity context, ArrayList<String> names, int width) {
//        super(context, R.layout.layout_categories_item, names);
//        this.context = context;
//        this.names = names;
//        this.width = width;
//
//    }

    public CategoriesAdapterNew(Activity context, ArrayList<String> names, CategoriesFragment fragment) {
        super(context, R.layout.layout_categories_item, names);
        this.context = context;
        this.names = names;
//        this.width = width;
        this.fragment = fragment;

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
                rowView = inflater.inflate(R.layout.layout_categories_item_ar, null);
            }else {
                rowView = inflater.inflate(R.layout.layout_categories_item, null);
            }

            //
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.categoryImg = rowView.findViewById(R.id.categoryImg);
            viewHolder.categoryName = rowView.findViewById(R.id.categoryName);
            viewHolder.progress = rowView.findViewById(R.id.progress);
            //
            rowView.setTag(viewHolder);
        }

//        int w = GlobalFunctions.convertDpToPx(context, 375);
//        int h = GlobalFunctions.convertDpToPx(context, 192);
//
//        int nw = width;
//        int nh = h;
//
//        if (nw != w) {
//
//            float ratio = (float)nw / (float)w;
//
//            nh = (int)(h * ratio);
//        }
//
        final ViewHolder holder = (ViewHolder)rowView.getTag();
//
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.categoryImg.getLayoutParams();
//
//        params.height = nh;
//
//        holder.categoryImg.setLayoutParams(params);


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();


        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);

            holder.categoryImg.setImageDrawable(null);

//            holder.categoryName.setTypeface(custom_fontnormal);

            if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
//                ImageLoader.getInstance().displayImage(obj.getString("image_ar"), holder.categoryImg);
                holder.categoryName.setText(obj.getString("banner_name_ar"));

                ImageLoader.getInstance().displayImage(obj.getString("image_ar"), holder.categoryImg, options, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
//                        fragment.showLoading();
                        holder.progress.setVisibility(View.VISIBLE);
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

//                        fragment.hideLoading();

                        holder.progress.setVisibility(View.GONE);

                    }
                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {

                    }
                });


            }else {
//                ImageLoader.getInstance().displayImage(obj.getString("image"), holder.categoryImg);
                holder.categoryName.setText(obj.getString("banner_name"));

//                ImageLoader.getInstance().displayImage(obj.getString("image"), holder.categoryImg, options, new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
////                        fragment.showLoading();
//                        holder.progress.setVisibility(View.VISIBLE);
//                    }
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//                    }
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
////                        fragment.hideLoading();
//                        holder.progress.setVisibility(View.GONE);
//
//                    }
//                    @Override
//                    public void onLoadingCancelled(String imageUri, View view) {
//
//                    }
//                }, new ImageLoadingProgressListener() {
//                    @Override
//                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
//
//                    }
//                });

                holder.progress.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(obj.getString("image"))
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                holder.progress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.progress.setVisibility(View.GONE);
                                return false;
                            }


                        })
                        .into(holder.categoryImg);
            }


        } catch (JSONException e) {

            Log.d("CategoriesAdapterNew", "JSONException:" + e.getMessage());

        }




        return rowView;
    }
}
