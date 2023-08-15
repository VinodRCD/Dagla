package com.dagla.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.dagla.android.activity.IntroductionActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;

/**
 * Created by sys on 20-Jan-19.
 */

public class IntroductionImagesAdapter extends PagerAdapter {

    private ArrayList<Integer> arrImages;
    private ArrayList<String> arrNames;
    private ArrayList<String> arrDescription;
    private LayoutInflater inflater;
    private Context context;

    public IntroductionImagesAdapter(Context context, ArrayList<Integer> arrImages, ArrayList<String> arrNames, ArrayList<String> arrDescription) {

        this.context = context;
        this.arrImages = arrImages;
        this.arrNames = arrNames;
        this.arrDescription = arrDescription;
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

        View rowView = inflater.inflate(R.layout.introduction_pager_layout, view, false);

        String lang = GlobalFunctions.getLang(context);

        final ImageView img = (ImageView)rowView.findViewById(R.id.img);

        TextView lblTitle = (TextView)rowView.findViewById(R.id.lblTitle);
        TextView lblDesc = (TextView)rowView.findViewById(R.id.lblDesc);

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




//        Picasso.with(context).
//                load(arrImages.get(position))
//
//                .into(img);

        lblTitle.setText(arrNames.get(position));
        lblDesc.setText(arrDescription.get(position));


        ImageLoader.getInstance().displayImage(String.valueOf(arrImages.get(position)), img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                ((IntroductionActivity)context).hideOverlay();

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
