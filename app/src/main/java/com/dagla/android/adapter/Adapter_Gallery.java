package com.dagla.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.dagla.android.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_Gallery extends PagerAdapter {

    Context context;
    LayoutInflater mLayoutInflater;
    String[] resources;
    private ArrayList<String> arrImages;
    FragmentManager fm;

    public Adapter_Gallery(Context context, String[] resources) {
        this.context = context;
        this.resources = resources;
        this.fm = fm;
        mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public Adapter_Gallery(Context context, ArrayList<String> arrImages) {
        this.context = context;
        this.arrImages = arrImages;
        this.fm = fm;
        mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        TouchImageView product_image;

        View itemView = mLayoutInflater.inflate(R.layout.dialog_layout_for_zoom_image, container, false);
//        product_image = (TouchImageView) itemView.findViewById(R.id.iv_gallery);

        PhotoView product_image = (PhotoView) itemView.findViewById(R.id.imageView);

//        String lang = GlobalFunctions.getLang(context);
//
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.bg_img)
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .build();
//
//        int pos = 0;
//
//        if (lang.equalsIgnoreCase("ar")) {
//
//            pos = arrImages.size()-1;
//
//        }
//
//        if (position == pos) {
//
//            options = new DisplayImageOptions.Builder()
//                    .cacheInMemory(true)
//                    .cacheOnDisk(true)
//                    .build();
//
//        }
//
//        ImageLoader.getInstance().displayImage(arrImages.get(position), product_image, options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//            }
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
////                fragment.hideOverlay();
//
//            }
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        }, new ImageLoadingProgressListener() {
//            @Override
//            public void onProgressUpdate(String imageUri, View view, int current, int total) {
//
//            }
//        });


        Picasso.with(context)
                .load(arrImages.get(position))
                .into(product_image);

//        Picasso.with(context)
//                .load(resources[position])
//                .into(product_image);

        container.addView(itemView);



        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
