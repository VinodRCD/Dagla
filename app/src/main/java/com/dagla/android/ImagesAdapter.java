package com.dagla.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;

public class ImagesAdapter extends PagerAdapter {

    private ArrayList<String> arrImages;
    private LayoutInflater inflater;
    private Context context;

    public ImagesAdapter(Context context, ArrayList<String> arrImages) {

        this.context = context;
        this.arrImages = arrImages;

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
    public Object instantiateItem(ViewGroup view, int position) {

        View rowView = inflater.inflate(R.layout.layout_image, view, false);

        String lang = GlobalFunctions.getLang(context);

        final ImageView img = (ImageView)rowView.findViewById(R.id.img);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        int pos = 0;

        if (lang.equalsIgnoreCase("ar")) {

            pos = arrImages.size()-1;

        }

        if (position == pos) {

            options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();

        }

        ImageLoader.getInstance().displayImage(arrImages.get(position), img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                ((ItemDetailsActivity)context).hideOverlay();

            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });


        view.addView(rowView, 0);

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

}