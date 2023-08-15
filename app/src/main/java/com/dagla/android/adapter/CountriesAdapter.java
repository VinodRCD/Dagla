package com.dagla.android.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.CircleTransform;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.parser.CountriesDetails;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sys on 10-Jun-18.
 */

public class CountriesAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<CountriesDetails> countryDataList = null;
    private ArrayList<CountriesDetails> arraylist;
    ProgressDialog pDialog;
//    int pos = -1;
    int pos = 0;

    public CountriesAdapter(Context context, ArrayList<CountriesDetails> countryDataList) {
        mContext = context;
        this.countryDataList = countryDataList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CountriesDetails>();
        this.arraylist.addAll(countryDataList);
    }

    public class ViewHolder {

        TextView countryName;

        ImageView countrySelImg,flagImg;


    }

    @Override
    public int getCount() {
        return countryDataList.size();
    }

    @Override
    public CountriesDetails getItem(int position) {
        return countryDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            if(GlobalFunctions.getLang(mContext).equals("ar")){
                view = inflater.inflate(R.layout.countries_listview_layout_ar, null);
            }else {
                view = inflater.inflate(R.layout.countries_listview_layout, null);
            }

            // Locate the TextViews in listview_item.xml
//                holder.recipesName = (TextView) view.findViewById(R.id.recipesName);

            holder.countryName = (TextView) view.findViewById(R.id.countryName);
            holder.countrySelImg = (ImageView) view.findViewById(R.id.countrySelImg);
            holder.flagImg = (ImageView) view.findViewById(R.id.flagImg);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(GlobalFunctions.getLang(mContext).equals("ar")){
            holder.countryName.setText(countryDataList.get(position).getCountryNameAr());
        }else {
            holder.countryName.setText(countryDataList.get(position).getCountryName());
        }

//        Picasso.with(mContext).load(countryDataList.get(position).getCountryFlag()).transform(new CircleTransform()).into(holder.flagImg);



        Glide.with(mContext)
                .load(countryDataList.get(position).getCountryFlag())
//                    .placeholder(R.drawable.place_holder)
                .circleCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

//                            progress2.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            progress2.setVisibility(View.GONE);
                        return false;
                    }


                })
                .into(holder.flagImg);


        if(GlobalFunctions.getLang(mContext).equals("ar")){
            if(GlobalFunctions.getPrefrences(mContext, "CountryNameAr").equals(countryDataList.get(position).getCountryNameAr())){
                countryDataList.get(position).setSelected(true);
            }else {
                countryDataList.get(position).setSelected(false);
            }
        }else {
            if(GlobalFunctions.getPrefrences(mContext, "CountryName").equals(countryDataList.get(position).getCountryName())){
                countryDataList.get(position).setSelected(true);
            }else {
                countryDataList.get(position).setSelected(false);
            }
        }



        if(countryDataList.get(position).isSelected()){
//            holder.countrySelImg.setVisibility(View.VISIBLE);
            holder.countrySelImg.setImageResource(R.drawable.radio_btn_2);
        }else{
//            holder.countrySelImg.setVisibility(View.GONE);
            holder.countrySelImg.setImageResource(R.drawable.radio_btn_1);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(countryDataList.get(position).isSelected()){
//                    countryDataList.get(position).setSelected(false);
//                    notifyDataSetChanged();
//                }else{
//                    countryDataList.get(position).setSelected(true);
//                    notifyDataSetChanged();
//                }

                countryDataList.get(position).setSelected(true);

//                pos = position;

                SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
//                if(GlobalFunctions.getLang(mContext).equals("ar")){
                    editor.putString("CountryNameAr",countryDataList.get(position).getCountryNameAr());
//                }else {
                    editor.putString("CountryName",countryDataList.get(position).getCountryName());
//                }

                editor.putString("CountryCurrency",countryDataList.get(position).getCountryCurrency());
                editor.putString("CountryFlag",countryDataList.get(position).getCountryFlag());
                editor.commit();

                GlobalFunctions.clearCart();
                ((MainActivity) mContext).setCartCount();

                notifyDataSetChanged();


            }
        });



        return view;
    }
}
