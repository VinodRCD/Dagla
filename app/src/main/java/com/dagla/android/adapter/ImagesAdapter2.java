package com.dagla.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.ProductDescriptionFromHomeFragment;
import com.google.android.material.tabs.TabLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;

public class ImagesAdapter2 extends PagerAdapter {

    private ArrayList<String> arrImages;
    private LayoutInflater inflater;
    private Context context;
    ProductDescriptionFromHomeFragment fragment;


    public ImagesAdapter2(Context context, ArrayList<String> arrImages, ProductDescriptionFromHomeFragment fragment) {

        this.context = context;
        this.arrImages = arrImages;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return arrImages.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View rowView = inflater.inflate(R.layout.layout_image, view, false);

        String lang = GlobalFunctions.getLang(context);

        ImageView img = (ImageView)rowView.findViewById(R.id.img);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

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

        Log.v("Images",arrImages.get(position));

        ImageLoader.getInstance().displayImage(arrImages.get(position), img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                fragment.hideOverlay();

            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });

//        img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                showGallery(position);
//            }
//        });


        view.addView(rowView);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showGallery(position);
            }
        });

        return rowView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    public void showGallery(int position) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_for_gallery_image);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        ViewPager pager_post_photo = (ViewPager) dialog.findViewById(R.id.pager_post_photo);

//        String[] photosArr = arrImages.toArray(new String[arrImages.size()]);

        Adapter_Gallery adapter_gallery = new Adapter_Gallery(context, arrImages);
        pager_post_photo.setAdapter(adapter_gallery);
        pager_post_photo.setCurrentItem(position);

        TabLayout tabDots = dialog.findViewById(R.id.tabDots);
        tabDots.setupWithViewPager(pager_post_photo, true);

        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.show();

    }
}
