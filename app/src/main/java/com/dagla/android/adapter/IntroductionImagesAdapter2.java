package com.dagla.android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.IntroductionActivity2;
import com.dagla.android.activity.StartingActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;

public class IntroductionImagesAdapter2 extends PagerAdapter {

    private ArrayList<Integer> arrImages;
    private ArrayList<String> arrNames;
    private ArrayList<String> arrDescription;
    private LayoutInflater inflater;
    private Context context;

    public IntroductionImagesAdapter2(Context context, ArrayList<Integer> arrImages, ArrayList<String> arrNames, ArrayList<String> arrDescription) {

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

        View rowView;

        if(GlobalFunctions.getLang(context).equals("ar")){
            rowView = inflater.inflate(R.layout.introduction_item, view, false);
        }else {
            rowView = inflater.inflate(R.layout.introduction_item, view, false);
        }

        String lang = GlobalFunctions.getLang(context);

        final ImageView img = (ImageView)rowView.findViewById(R.id.img);

//        TextView lblTitle = (TextView)rowView.findViewById(R.id.lblTitle);
//        TextView lblDesc = (TextView)rowView.findViewById(R.id.lblDesc);
//        Button btnSkipIntro = (Button)rowView.findViewById(R.id.btnSkipIntro);

//        lblTitle.setTypeface(custom_fontbold);
//        lblDesc.setTypeface(custom_fontnormal);

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

//        if (lang.equalsIgnoreCase("ar")) {
//            if (position == 0){
//                btnSkipIntro.setVisibility(View.VISIBLE);
//            }else {
//                btnSkipIntro.setVisibility(View.GONE);
//            }
//        }else {
//        if (position == arrImages.size()-1){
//            btnSkipIntro.setVisibility(View.VISIBLE);
//        }else {
//            btnSkipIntro.setVisibility(View.GONE);
//        }
//        }



//        btnSkipIntro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (!GlobalFunctions.getPrefrences(IntroductionActivity.this, "user_id").equalsIgnoreCase("")) {
////                    startActivity(new Intent(act, MainActivity.class));
////                    finish();
////                }else {
//                context.startActivity(new Intent(context, StartingActivity.class));
//                ((Activity)context).finish();
////                }
//
//                GlobalFunctions.setPrefrences(context, "intro", "1");
//
//            }
//        });

//        Picasso.with(context).
//                load(arrImages.get(position))
//
//                .into(img);

//        lblTitle.setText(arrNames.get(position));
//        lblDesc.setText(arrDescription.get(position));


        ImageLoader.getInstance().displayImage(String.valueOf(arrImages.get(position)), img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                ((IntroductionActivity2)context).showLoading();
            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                ((IntroductionActivity2)context).hideLoading();

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
