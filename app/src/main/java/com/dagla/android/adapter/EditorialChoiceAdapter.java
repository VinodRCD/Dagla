package com.dagla.android.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.ProductDescriptionFromHomeFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sys on 22-Jan-19.
 */

public class EditorialChoiceAdapter extends PagerAdapter {

    private LayoutInflater inflater;
    private Context context;

    ArrayList<HashMap<String, String>> arrayList1;
    ArrayList<HashMap<String, String>> arrayList2;
    ArrayList<HashMap<String, String>> arrayList3;
    ArrayList<HashMap<String, String>> arrayList4;

    FragmentActivity activity;
    FragmentTransaction ft;


    public EditorialChoiceAdapter(Context context, ArrayList<HashMap<String, String>> arrayList1, ArrayList<HashMap<String, String>> arrayList2, ArrayList<HashMap<String, String>> arrayList3,
                                  ArrayList<HashMap<String, String>> arrayList4) {

        this.context = context;

        this.arrayList1 = arrayList1;
        this.arrayList2 = arrayList2;
        this.arrayList3 = arrayList3;
        this.arrayList4 = arrayList4;

        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return arrayList1.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View rowView;

        if(GlobalFunctions.getLang(context).equals("ar")){
            rowView = inflater.inflate(R.layout.home_pager_layout_ar, view, false);
        }else {
            rowView = inflater.inflate(R.layout.home_pager_layout, view, false);
        }

        String lang = GlobalFunctions.getLang(context);

        ImageView img1 = (ImageView)rowView.findViewById(R.id.img1);
        ImageView img2 = (ImageView)rowView.findViewById(R.id.img2);

        TextView lblTitle1 = (TextView)rowView.findViewById(R.id.lblTitle1);
        TextView lblTitle2 = (TextView)rowView.findViewById(R.id.lblTitle2);

        TextView lblPrice1 = (TextView)rowView.findViewById(R.id.lblPrice1);
        TextView lblPrice2 = (TextView)rowView.findViewById(R.id.lblPrice2);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

//        lblTitle1.setTypeface(custom_fontmedium);
//        lblTitle2.setTypeface(custom_fontmedium);

        lblTitle1.setText(arrayList1.get(position).get("Key1").toString());
        lblTitle2.setText(arrayList1.get(position).get("Key2").toString());

        if (GlobalFunctions.getLang(context).equals("ar")) {
//            String price1 = arrayList4.get(position).get("Key1").toString();
//            price1 = price1.replace("د.ك", "");
//            lblPrice1.setText("د.ك " + price1);
//
//            String price2 = arrayList4.get(position).get("Key2").toString();
//            price1 = price2.replace("د.ك", "");
//            lblPrice2.setText("د.ك " + price1);

            lblPrice1.setText(arrayList4.get(position).get("Key1").toString());
            lblPrice2.setText(arrayList4.get(position).get("Key2").toString());

        }else {
            lblPrice1.setText(arrayList4.get(position).get("Key1").toString());
            lblPrice2.setText(arrayList4.get(position).get("Key2").toString());
        }

//        Picasso.with(context).
//                load(arrayList2.get(position).get("Key1"))
//
//                .into(img1);
//
//        Picasso.with(context).
//                load(arrayList2.get(position).get("Key2"))
//
//                .into(img2);

        ImageLoader.getInstance().displayImage(String.valueOf(arrayList2.get(position).get("Key1")), img1, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

//                ((IntroductionActivity)context).hideOverlay();

            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });

        ImageLoader.getInstance().displayImage(String.valueOf(arrayList2.get(position).get("Key2")), img2, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

//                ((IntroductionActivity)context).hideOverlay();

            }
            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });

        LinearLayout linear1 = (LinearLayout)rowView.findViewById(R.id.linear1);
        LinearLayout linear2 = (LinearLayout)rowView.findViewById(R.id.linear2);


        if(arrayList2.get(position).get("Key2").equals("")){
            linear2.setVisibility(View.INVISIBLE);
        }else {
            linear2.setVisibility(View.VISIBLE);
        }


        linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                Bundle b = new Bundle();
                b.putString("product_id", arrayList3.get(position).get("Key1").toString());
                b.putString("title", arrayList1.get(position).get("Key1").toString());
                fragment.setArguments(b);


                activity = ((FragmentActivity) context);
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
            }
        });

        linear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                Bundle b = new Bundle();
                b.putString("product_id", arrayList3.get(position).get("Key2").toString());
                b.putString("title", arrayList1.get(position).get("Key1").toString());
                fragment.setArguments(b);

                activity = ((FragmentActivity) context);
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();

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
