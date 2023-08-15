package com.dagla.android.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.BestSellerAdapter;
import com.dagla.android.adapter.EditorialChoiceAdapter;
import com.dagla.android.adapter.WhatsNewAdapter;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
//import ly.count.android.sdk.Countly;

public class HomeFragment extends Fragment {

    Activity act;

    ImageView imgBanner1,imgBanner2,imgBanner3,imgBanner4,imgBanner5;
    ViewPager vPagerImages1,vPagerImages2,vPagerImages3;
    TabLayout tabDots1,tabDots2,tabDots3;
    TextView lblTitle1,lblTitle2,lblTitle3;
    View view2;
    Button btnViewMore1,btnViewMore2,btnViewMore3;
    View rootView;
    Bundle savedInstanceState;

    WhatsNewAdapter whatsNewAdapter;
    BestSellerAdapter bestSellerAdapter;
    EditorialChoiceAdapter editorialChoiceAdapter;

    Dialog dlgLoading = null;

    ArrayList<String> bannerIdArrList;
    ArrayList<String> bannerCatIdArrList;
    ArrayList<String> bannerSubCatIdArrList;
    ArrayList<String> bannerProductIdArrList;
    ArrayList<String> bannerBrandIdArrList;
    ArrayList<String> bannerNameArrList;
    ArrayList<String> bannerImageArrList;
    ArrayList<String> productNameArrList1;
    ArrayList<String> productNameArrList2;
    ArrayList<String> productNameArrList3;
    ArrayList<String> productIdArrList1;
    ArrayList<String> productIdArrList2;
    ArrayList<String> productIdArrList3;
    ArrayList<String> productPriceArrList1;
    ArrayList<String> productPriceArrList2;
    ArrayList<String> productPriceArrList3;
    ArrayList<String> picArrList1;
    ArrayList<String> picArrList2;
    ArrayList<String> picArrList3;


    ArrayList<HashMap<String, String>> arrayList1;
    ArrayList<HashMap<String, String>> arrayList2;
    ArrayList<HashMap<String, String>> arrayList3;
    ArrayList<HashMap<String, String>> arrayList4;
    ArrayList<HashMap<String, String>> arrayList5;
    ArrayList<HashMap<String, String>> arrayList6;
    ArrayList<HashMap<String, String>> arrayList7;
    ArrayList<HashMap<String, String>> arrayList8;
    ArrayList<HashMap<String, String>> arrayList9;

    ArrayList<HashMap<String, String>> arrayList10;
    ArrayList<HashMap<String, String>> arrayList11;
    ArrayList<HashMap<String, String>> arrayList12;


    String main_banner_Id,pro_banner_Id;

    RelativeLayout imgBanner1Layout,imgBanner2Layout,imgBanner3Layout,imgBanner4Layout,imgBanner5Layout;
    ProgressBar progress1,progress2,progress3,progress4,progress5;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_home_ar),true,true,true,false, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_home),true,true,true,false, false ,"0", false);
        }

        ((MainActivity) getActivity()).setCartCount();

        GlobalFunctions.initImageLoader(getActivity());

        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_home_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_home, container, false);
            }


            imgBanner1Layout = rootView.findViewById(R.id.imgBanner1Layout);
            imgBanner2Layout = rootView.findViewById(R.id.imgBanner2Layout);
            imgBanner3Layout = rootView.findViewById(R.id.imgBanner3Layout);
            imgBanner4Layout = rootView.findViewById(R.id.imgBanner4Layout);
            imgBanner5Layout = rootView.findViewById(R.id.imgBanner5Layout);

            progress1 = rootView.findViewById(R.id.progress1);
            progress2 = rootView.findViewById(R.id.progress2);
            progress3 = rootView.findViewById(R.id.progress3);
            progress4 = rootView.findViewById(R.id.progress4);
            progress5 = rootView.findViewById(R.id.progress5);

            imgBanner1 = rootView.findViewById(R.id.imgBanner1);
            imgBanner2 = rootView.findViewById(R.id.imgBanner2);
            imgBanner3 = rootView.findViewById(R.id.imgBanner3);
            imgBanner4 = rootView.findViewById(R.id.imgBanner4);
            imgBanner5 = rootView.findViewById(R.id.imgBanner5);


//            ViewGroup.LayoutParams paramsImg = imgBanner1.getLayoutParams();
//
//            int w = GlobalFunctions.convertDpToPx(getActivity(), 640);
//            int h = GlobalFunctions.convertDpToPx(getActivity(), 640);
//            paramsImg.width = w;
//            paramsImg.height = h;
//
//            imgBanner1.setLayoutParams(paramsImg);


//            int h = imgBanner1.getLayoutParams().width;
//            int w = imgBanner1.getLayoutParams().width;
//
//            ViewGroup.LayoutParams paramsImg = imgBanner1.getLayoutParams();
//
//            paramsImg.width = w;
//            paramsImg.height = h;
////
//            imgBanner1.setLayoutParams(paramsImg);


            vPagerImages1 = rootView.findViewById(R.id.vPagerImages1);
            vPagerImages2 = rootView.findViewById(R.id.vPagerImages2);
            vPagerImages3 = rootView.findViewById(R.id.vPagerImages3);

            tabDots1 = rootView.findViewById(R.id.tabDots1);
            tabDots2 = rootView.findViewById(R.id.tabDots2);
            tabDots3 = rootView.findViewById(R.id.tabDots3);

            lblTitle1 = rootView.findViewById(R.id.lblTitle1);
            lblTitle2 = rootView.findViewById(R.id.lblTitle2);
            lblTitle3 = rootView.findViewById(R.id.lblTitle3);

            btnViewMore1 = rootView.findViewById(R.id.btnViewMore1);
            btnViewMore2 = rootView.findViewById(R.id.btnViewMore2);
            btnViewMore3 = rootView.findViewById(R.id.btnViewMore3);

            TextView lblViewMore1 = rootView.findViewById(R.id.lblViewMore1);
            TextView lblViewMore2 = rootView.findViewById(R.id.lblViewMore2);
            TextView lblViewMore3 = rootView.findViewById(R.id.lblViewMore3);

            lblViewMore1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            lblViewMore2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            lblViewMore3.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

            view2 = rootView.findViewById(R.id.view2);

            lblTitle1.setText("");
            lblTitle2.setText("");
            lblTitle3.setText("");

            bannerNameArrList = new ArrayList<String>();
            bannerIdArrList = new ArrayList<String>();
            bannerCatIdArrList = new ArrayList<String>();
            bannerSubCatIdArrList = new ArrayList<String>();
            bannerProductIdArrList = new ArrayList<String>();
            bannerBrandIdArrList = new ArrayList<String>();

            bannerImageArrList = new ArrayList<String>();
            productNameArrList1 = new ArrayList<String>();
            productNameArrList2 = new ArrayList<String>();
            productNameArrList3 = new ArrayList<String>();

            productIdArrList1 = new ArrayList<String>();
            productIdArrList2 = new ArrayList<String>();
            productIdArrList3 = new ArrayList<String>();

            productPriceArrList1 = new ArrayList<String>();
            productPriceArrList2 = new ArrayList<String>();
            productPriceArrList3 = new ArrayList<String>();

            picArrList1 = new ArrayList<String>();
            picArrList2 = new ArrayList<String>();
            picArrList3 = new ArrayList<String>();


            arrayList1 = new ArrayList<HashMap<String, String>>();
            arrayList2 = new ArrayList<HashMap<String, String>>();

            arrayList3 = new ArrayList<HashMap<String, String>>();
            arrayList4 = new ArrayList<HashMap<String, String>>();

            arrayList5 = new ArrayList<HashMap<String, String>>();
            arrayList6 = new ArrayList<HashMap<String, String>>();

            arrayList7 = new ArrayList<HashMap<String, String>>();

            arrayList8 = new ArrayList<HashMap<String, String>>();
            arrayList9 = new ArrayList<HashMap<String, String>>();

            arrayList10 = new ArrayList<HashMap<String, String>>();
            arrayList11 = new ArrayList<HashMap<String, String>>();
            arrayList12 = new ArrayList<HashMap<String, String>>();

            loadData();


            imgBanner1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=1){

                        if(!bannerCatIdArrList.get(0).toString().equals("vpM1hJ6qjWc=")||!bannerSubCatIdArrList.get(0).toString().equals("vpM1hJ6qjWc=")||
                                !bannerProductIdArrList.get(0).toString().equals("vpM1hJ6qjWc=")||!bannerBrandIdArrList.get(0).toString().equals("vpM1hJ6qjWc=")){

                            if(bannerProductIdArrList.get(0).toString()!= null && !bannerProductIdArrList.get(0).toString().equals("vpM1hJ6qjWc=")){
                                ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                                Bundle b = new Bundle();
                                b.putString("product_id", bannerProductIdArrList.get(0).toString());
                                b.putString("title", bannerNameArrList.get(0).toString());
                                b.putString("home_banner_name", bannerNameArrList.get(0).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }else {

                                ProductsFromHomeBannerFragment fragment = new ProductsFromHomeBannerFragment();
                                Bundle b = new Bundle();
                                b.putString("cat_id", bannerCatIdArrList.get(0).toString());
                                b.putString("sub_cat_id", bannerSubCatIdArrList.get(0).toString());
                                b.putString("brand_id", bannerBrandIdArrList.get(0).toString());
                                b.putString("home_banner_name", bannerNameArrList.get(0).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductsFromHomeBannerFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }

                        }else {

                        }




                    }

                }
            });

            imgBanner2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=2){

                        if(!bannerCatIdArrList.get(1).toString().equals("vpM1hJ6qjWc=")||!bannerSubCatIdArrList.get(1).toString().equals("vpM1hJ6qjWc=")||
                                !bannerProductIdArrList.get(1).toString().equals("vpM1hJ6qjWc=")||!bannerBrandIdArrList.get(1).toString().equals("vpM1hJ6qjWc=")){

                            if(bannerProductIdArrList.get(1).toString()!= null && !bannerProductIdArrList.get(1).toString().equals("vpM1hJ6qjWc=")){
                                ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                                Bundle b = new Bundle();
                                b.putString("product_id", bannerProductIdArrList.get(1).toString());
                                b.putString("title", bannerNameArrList.get(1).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }else {

                                ProductsFromHomeBannerFragment fragment = new ProductsFromHomeBannerFragment();
                                Bundle b = new Bundle();
                                b.putString("cat_id", bannerCatIdArrList.get(1).toString());
                                b.putString("sub_cat_id", bannerSubCatIdArrList.get(1).toString());
                                b.putString("brand_id", bannerBrandIdArrList.get(1).toString());
                                b.putString("home_banner_name", bannerNameArrList.get(1).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductsFromHomeBannerFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }

                        }else {

                        }


                    }

                }
            });

            imgBanner3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=3){

                        if(!bannerCatIdArrList.get(2).toString().equals("vpM1hJ6qjWc=")||!bannerSubCatIdArrList.get(2).toString().equals("vpM1hJ6qjWc=")||
                                !bannerProductIdArrList.get(2).toString().equals("vpM1hJ6qjWc=")||!bannerBrandIdArrList.get(2).toString().equals("vpM1hJ6qjWc=")){

                            if(bannerProductIdArrList.get(2).toString()!= null && !bannerProductIdArrList.get(2).toString().equals("vpM1hJ6qjWc=")){
                                ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                                Bundle b = new Bundle();
                                b.putString("product_id", bannerProductIdArrList.get(2).toString());
                                b.putString("title", bannerNameArrList.get(2).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }else {

                                ProductsFromHomeBannerFragment fragment = new ProductsFromHomeBannerFragment();
                                Bundle b = new Bundle();
                                b.putString("cat_id", bannerCatIdArrList.get(2).toString());
                                b.putString("sub_cat_id", bannerSubCatIdArrList.get(2).toString());
                                b.putString("brand_id", bannerBrandIdArrList.get(2).toString());
                                b.putString("home_banner_name", bannerNameArrList.get(2).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductsFromHomeBannerFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }
                        }


                    }

                }
            });

            imgBanner4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(bannerIdArrList.size()== 4){

                        if(!bannerCatIdArrList.get(3).toString().equals("vpM1hJ6qjWc=")||!bannerSubCatIdArrList.get(3).toString().equals("vpM1hJ6qjWc=")||
                                !bannerProductIdArrList.get(3).toString().equals("vpM1hJ6qjWc=")||!bannerBrandIdArrList.get(3).toString().equals("vpM1hJ6qjWc=")){

                            if(bannerProductIdArrList.get(3).toString()!= null && !bannerProductIdArrList.get(3).toString().equals("vpM1hJ6qjWc=")){
                                ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                                Bundle b = new Bundle();
                                b.putString("product_id", bannerProductIdArrList.get(3).toString());
                                b.putString("title", bannerNameArrList.get(3).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }else {

                                ProductsFromHomeBannerFragment fragment = new ProductsFromHomeBannerFragment();
                                Bundle b = new Bundle();
                                b.putString("cat_id", bannerCatIdArrList.get(3).toString());
                                b.putString("sub_cat_id", bannerSubCatIdArrList.get(3).toString());
                                b.putString("brand_id", bannerBrandIdArrList.get(3).toString());
                                b.putString("home_banner_name", bannerNameArrList.get(3).toString());
                                fragment.setArguments(b);
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment, "ProductsFromHomeBannerFragment")
                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                        .commitAllowingStateLoss();

                            }

                        }

                    }

                }
            });


            lblViewMore1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=1){
//                        Crashlytics.getInstance().crash(); // Force a crash
                        ProductsFromHomeViewMoreFragment fragment = new ProductsFromHomeViewMoreFragment();
                        Bundle b = new Bundle();
                        b.putString("home_banner_id", bannerIdArrList.get(0).toString());
                        b.putString("home_banner_name", bannerNameArrList.get(0).toString());
                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "SubCategoriesFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }


                }
            });

            lblViewMore2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=2){

                        ProductsFromHomeViewMoreFragment fragment = new ProductsFromHomeViewMoreFragment();
                        Bundle b = new Bundle();
                        b.putString("home_banner_id", bannerIdArrList.get(1).toString());
                        b.putString("home_banner_name", bannerNameArrList.get(1).toString());
                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "SubCategoriesFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }



                }
            });

            lblViewMore3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=3){

                        ProductsFromHomeViewMoreFragment fragment = new ProductsFromHomeViewMoreFragment();
                        Bundle b = new Bundle();
                        b.putString("home_banner_id", bannerIdArrList.get(2).toString());
                        b.putString("home_banner_name", bannerNameArrList.get(2).toString());
                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "ProductsFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }

                }
            });


            lblTitle1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=1){
//                        Crashlytics.getInstance().crash(); // Force a crash
                        ProductsFromHomeViewMoreFragment fragment = new ProductsFromHomeViewMoreFragment();
                        Bundle b = new Bundle();
                        b.putString("home_banner_id", bannerIdArrList.get(0).toString());
                        b.putString("home_banner_name", bannerNameArrList.get(0).toString());
                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "SubCategoriesFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }


                }
            });

            lblTitle2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=2){

                        ProductsFromHomeViewMoreFragment fragment = new ProductsFromHomeViewMoreFragment();
                        Bundle b = new Bundle();
                        b.putString("home_banner_id", bannerIdArrList.get(1).toString());
                        b.putString("home_banner_name", bannerNameArrList.get(1).toString());
                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "SubCategoriesFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }



                }
            });

            lblTitle3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(bannerIdArrList.size()>=3){

                        ProductsFromHomeViewMoreFragment fragment = new ProductsFromHomeViewMoreFragment();
                        Bundle b = new Bundle();
                        b.putString("home_banner_id", bannerIdArrList.get(2).toString());
                        b.putString("home_banner_name", bannerNameArrList.get(2).toString());
                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "ProductsFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }

                }
            });
        }



        return rootView;

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }


            client.get(GlobalFunctions.serviceURL + "getHomeBannerData", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1, arr2;
                    JSONObject obj, obj1, obj2;

                    bannerNameArrList.clear();
                    bannerIdArrList.clear();
                    bannerCatIdArrList.clear();
                    bannerSubCatIdArrList.clear();
                    bannerProductIdArrList.clear();
                    bannerBrandIdArrList.clear();

                    bannerImageArrList.clear();
                    productNameArrList1.clear();
                    productNameArrList2.clear();
                    productNameArrList3.clear();

                    productIdArrList1.clear();
                    productIdArrList2.clear();
                    productIdArrList3.clear();

                    productPriceArrList1.clear();
                    productPriceArrList2.clear();
                    productPriceArrList3.clear();

                    picArrList1.clear();
                    picArrList2.clear();
                    picArrList3.clear();

                    arrayList1.clear();
                    arrayList2.clear();
                    arrayList3.clear();
                    arrayList4.clear();
                    arrayList5.clear();
                    arrayList6.clear();
                    arrayList7.clear();
                    arrayList8.clear();
                    arrayList9 .clear();
                    arrayList10.clear();
                    arrayList11.clear();
                    arrayList12 .clear();

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("Banners");


                            for (int i = 0; i < arr.length(); i++) {
                                main_banner_Id = arr.getJSONObject(i).getString("banner_id");
                                bannerIdArrList .add(arr.getJSONObject(i).getString("banner_id"));
                                bannerCatIdArrList.add(arr.getJSONObject(i).getString("banner_cat_id"));
                                bannerSubCatIdArrList.add(arr.getJSONObject(i).getString("banner_sub_cat_id"));
                                bannerProductIdArrList.add(arr.getJSONObject(i).getString("banner_product_id"));
                                bannerBrandIdArrList.add(arr.getJSONObject(i).getString("banner_brand_id"));
                                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                    bannerNameArrList.add(arr.getJSONObject(i).getString("home_banner_cat_name_ar"));
                                }else {
                                    bannerNameArrList.add(arr.getJSONObject(i).getString("home_banner_cat_name"));
                                }

                                Log.v("BannerName",arr.getJSONObject(i).getString("home_banner_cat_name"));
                                bannerImageArrList.add(arr.getJSONObject(i).getString("image"));


                                arr1 = arr.getJSONObject(i).getJSONArray("Products");

                                for (int j = 0; j < arr1.length(); j++) {

                                    if(main_banner_Id.equals(arr1.getJSONObject(j).getString("banner_id"))){
                                        if(i==0){

                                            if(main_banner_Id.equals(arr1.getJSONObject(j).getString("banner_id"))){
                                                productIdArrList1.add(arr1.getJSONObject(j).getString("product_id"));
                                                productNameArrList1.add(arr1.getJSONObject(j).getString("name"));
                                                productPriceArrList1.add(arr1.getJSONObject(j).getString("price"));
                                                picArrList1.add(arr1.getJSONObject(j).getString("pic"));
                                                Log.v("ProductName1",arr1.getJSONObject(j).getString("name"));
                                            }

                                        }else if(i==1){

                                            if(main_banner_Id.equals(arr1.getJSONObject(j).getString("banner_id"))){
                                                productIdArrList2.add(arr1.getJSONObject(j).getString("product_id"));
                                                productNameArrList2.add(arr1.getJSONObject(j).getString("name"));
                                                productPriceArrList2.add(arr1.getJSONObject(j).getString("price"));
                                                picArrList2.add(arr1.getJSONObject(j).getString("pic"));
                                                Log.v("ProductName2",arr1.getJSONObject(j).getString("name"));
                                            }

                                        }else if(i==2){

                                            if(main_banner_Id.equals(arr1.getJSONObject(j).getString("banner_id"))){
                                                productIdArrList3.add(arr1.getJSONObject(j).getString("product_id"));
                                                productNameArrList3.add(arr1.getJSONObject(j).getString("name"));
                                                productPriceArrList3.add(arr1.getJSONObject(j).getString("price"));
                                                picArrList3.add(arr1.getJSONObject(j).getString("pic"));
                                                Log.v("ProductName3",arr1.getJSONObject(j).getString("name"));
                                            }

                                        }


//                                        arr2 = arr1.getJSONObject(j).getJSONArray("pics");
//
//                                        for (int k = 0; k < 1; k++) {
//                                            if(i==0){
//                                                picArrList1.add(arr2.getJSONObject(k).getString("pic"));
//                                                Log.v("PicName1",arr2.getJSONObject(k).getString("pic"));
//                                            }else if(i==1){
//                                                picArrList2.add(arr2.getJSONObject(k).getString("pic"));
//                                                Log.v("PicName2",arr2.getJSONObject(k).getString("pic"));
//                                            }else if(i==2){
//                                                picArrList3.add(arr2.getJSONObject(k).getString("pic"));
//                                                Log.v("PicName3",arr2.getJSONObject(k).getString("pic"));
//                                            }
//
//                                        }
                                    }


                                }

                            }


                            for (int a = 0; a < bannerNameArrList.size(); a++) {
                                if(a==0){
                                    lblTitle1.setText(bannerNameArrList.get(a).toString());

                                    progress1.setVisibility(View.VISIBLE);

                                    Glide.with(getActivity())
                                            .load(bannerImageArrList.get(a).toString())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                    progress1.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progress1.setVisibility(View.GONE);
                                                    return false;
                                                }


                                            })
                                            .into(imgBanner1);


                                }else if (a==1){
                                    lblTitle2.setText(bannerNameArrList.get(a).toString());

                                    progress2.setVisibility(View.VISIBLE);

                                    Glide.with(getActivity())
                                            .load(bannerImageArrList.get(a).toString())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                    progress2.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progress2.setVisibility(View.GONE);
                                                    return false;
                                                }


                                            })
                                            .into(imgBanner2);



                                }else if (a==2){
                                    lblTitle3.setText(bannerNameArrList.get(a).toString());


                                    progress3.setVisibility(View.VISIBLE);

                                    Glide.with(getActivity())
                                            .load(bannerImageArrList.get(a).toString())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                    progress3.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progress3.setVisibility(View.GONE);
                                                    return false;
                                                }


                                            })
                                            .into(imgBanner3);


                                }else if (a==3){

                                    progress4.setVisibility(View.VISIBLE);

                                    Glide.with(getActivity())
                                            .load(bannerImageArrList.get(a).toString())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                    progress4.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progress4.setVisibility(View.GONE);
                                                    return false;
                                                }


                                            })
                                            .into(imgBanner4);



                                    imgBanner4Layout.setVisibility(View.VISIBLE);

                                }else if (a==4){

                                    progress5.setVisibility(View.VISIBLE);

                                    Glide.with(getActivity())
                                            .load(bannerImageArrList.get(a).toString())
                                            .listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                                    progress5.setVisibility(View.GONE);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    progress5.setVisibility(View.GONE);
                                                    return false;
                                                }


                                            })
                                            .into(imgBanner5);

                                    imgBanner5Layout.setVisibility(View.VISIBLE);
                                    view2.setVisibility(View.VISIBLE);
                                }
                            }



                            if(productNameArrList1.size()>=1  || productNameArrList1.size()>=2){

                                HashMap<String, String> productName1Map1 = new HashMap<String, String>();
                                HashMap<String, String> productImage1Map1 = new HashMap<String, String>();
                                HashMap<String, String> productId1Map1 = new HashMap<String, String>();
                                HashMap<String, String> productPrice1Map1 = new HashMap<String, String>();

                                if (productNameArrList1.size()>=2){

                                    productName1Map1.put("Key1",productNameArrList1.get(0).toString());
                                    productImage1Map1.put("Key1",picArrList1.get(0).toString());
                                    productId1Map1.put("Key1",productIdArrList1.get(0).toString());
                                    productPrice1Map1.put("Key1",productPriceArrList1.get(0).toString());

                                    productName1Map1.put("Key2",productNameArrList1.get(1).toString());
                                    productImage1Map1.put("Key2",picArrList1.get(1).toString());
                                    productId1Map1.put("Key2",productIdArrList1.get(1).toString());
                                    productPrice1Map1.put("Key2",productPriceArrList1.get(1).toString());
                                }else if (productNameArrList1.size()>=1){

                                    productName1Map1.put("Key1",productNameArrList1.get(0).toString());
                                    productImage1Map1.put("Key1",picArrList1.get(0).toString());
                                    productId1Map1.put("Key1",productIdArrList1.get(0).toString());
                                    productPrice1Map1.put("Key1",productPriceArrList1.get(0).toString());

                                    productName1Map1.put("Key2","");
                                    productImage1Map1.put("Key2","");
                                    productId1Map1.put("Key2","");
                                    productPrice1Map1.put("Key2","");
                                }


                                arrayList1.add(productName1Map1);
                                arrayList2.add(productImage1Map1);
                                arrayList7.add(productId1Map1);
                                arrayList10.add(productPrice1Map1);

                            }

                            if(productNameArrList1.size()>=3  || productNameArrList1.size()>=4){

                                HashMap<String, String> productName1Map2 = new HashMap<String, String>();
                                HashMap<String, String> productImage1Map2 = new HashMap<String, String>();
                                HashMap<String, String> productId1Map2 = new HashMap<String, String>();
                                HashMap<String, String> productPrice1Map2 = new HashMap<String, String>();

                                if (productNameArrList1.size()>=4){
                                    productName1Map2.put("Key1",productNameArrList1.get(2).toString());
                                    productImage1Map2.put("Key1",picArrList1.get(2).toString());
                                    productId1Map2.put("Key1",productIdArrList1.get(2).toString());
                                    productPrice1Map2.put("Key1",productPriceArrList1.get(2).toString());

                                    productName1Map2.put("Key2",productNameArrList1.get(3).toString());
                                    productImage1Map2.put("Key2",picArrList1.get(3).toString());
                                    productId1Map2.put("Key2",productIdArrList1.get(3).toString());
                                    productPrice1Map2.put("Key2",productPriceArrList1.get(3).toString());
                                }else if (productNameArrList1.size()>=3){
                                    productName1Map2.put("Key1",productNameArrList1.get(2).toString());
                                    productImage1Map2.put("Key1",picArrList1.get(2).toString());
                                    productId1Map2.put("Key1",productIdArrList1.get(2).toString());
                                    productPrice1Map2.put("Key1",productPriceArrList1.get(2).toString());

                                    productName1Map2.put("Key2","");
                                    productImage1Map2.put("Key2","");
                                    productId1Map2.put("Key2","");
                                    productPrice1Map2.put("Key2","");
                                }


                                arrayList1.add(productName1Map2);
                                arrayList2.add(productImage1Map2);
                                arrayList7.add(productId1Map2);
                                arrayList10.add(productPrice1Map2);

                            }

                            if(productNameArrList1.size()>=5  || productNameArrList1.size()>=6){

                                HashMap<String, String> productName1Map3 = new HashMap<String, String>();
                                HashMap<String, String> productImage1Map3 = new HashMap<String, String>();
                                HashMap<String, String> productId1Map3 = new HashMap<String, String>();
                                HashMap<String, String> productPrice1Map3 = new HashMap<String, String>();

                                if (productNameArrList1.size()>=6){
                                    productName1Map3.put("Key1",productNameArrList1.get(4).toString());
                                    productImage1Map3.put("Key1",picArrList1.get(4).toString());
                                    productId1Map3.put("Key1",productIdArrList1.get(4).toString());
                                    productPrice1Map3.put("Key1",productPriceArrList1.get(4).toString());

                                    productName1Map3.put("Key2",productNameArrList1.get(5).toString());
                                    productImage1Map3.put("Key2",picArrList1.get(5).toString());
                                    productId1Map3.put("Key2",productIdArrList1.get(5).toString());
                                    productPrice1Map3.put("Key2",productPriceArrList1.get(5).toString());
                                }else if (productNameArrList1.size()>=5){
                                    productName1Map3.put("Key1",productNameArrList1.get(4).toString());
                                    productImage1Map3.put("Key1",picArrList1.get(4).toString());
                                    productId1Map3.put("Key1",productIdArrList1.get(4).toString());
                                    productPrice1Map3.put("Key1",productPriceArrList1.get(4).toString());

                                    productName1Map3.put("Key2","");
                                    productImage1Map3.put("Key2","");
                                    productId1Map3.put("Key2","");
                                    productPrice1Map3.put("Key2","");
                                }


                                arrayList1.add(productName1Map3);
                                arrayList2.add(productImage1Map3);
                                arrayList7.add(productId1Map3);
                                arrayList10.add(productPrice1Map3);
                            }

                            whatsNewAdapter = new WhatsNewAdapter(getActivity(), arrayList1, arrayList2, arrayList7, arrayList10);
                            vPagerImages1.setAdapter(whatsNewAdapter);
                            tabDots1.setupWithViewPager(vPagerImages1, true);

//                            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//
//                                vPagerImages1.setCurrentItem(arrayList1.size()-1);
//                            }

                            if(productNameArrList2.size()>=1  || productNameArrList2.size()>=2){

                                HashMap<String, String> productName2Map1 = new HashMap<String, String>();
                                HashMap<String, String> productImage2Map1 = new HashMap<String, String>();
                                HashMap<String, String> productId2Map1 = new HashMap<String, String>();
                                HashMap<String, String> productPrice2Map1 = new HashMap<String, String>();

                                if(productNameArrList2.size()>=2){

                                    productName2Map1.put("Key1",productNameArrList2.get(0).toString());
                                    productImage2Map1.put("Key1",picArrList2.get(0).toString());
                                    productId2Map1.put("Key1",productIdArrList2.get(0).toString());
                                    productPrice2Map1.put("Key1",productPriceArrList2.get(0).toString());

                                    productName2Map1.put("Key2",productNameArrList2.get(1).toString());
                                    productImage2Map1.put("Key2",picArrList2.get(1).toString());
                                    productId2Map1.put("Key2",productIdArrList2.get(1).toString());
                                    productPrice2Map1.put("Key2",productPriceArrList2.get(1).toString());

                                }else if (productNameArrList2.size()>=1) {

                                    productName2Map1.put("Key1",productNameArrList2.get(0).toString());
                                    productImage2Map1.put("Key1",picArrList2.get(0).toString());
                                    productId2Map1.put("Key1",productIdArrList2.get(0).toString());
                                    productPrice2Map1.put("Key1",productPriceArrList2.get(0).toString());

                                    productName2Map1.put("Key2","");
                                    productImage2Map1.put("Key2","");
                                    productId2Map1.put("Key2","");
                                    productPrice2Map1.put("Key2","");
                                }

                                arrayList3.add(productName2Map1);
                                arrayList4.add(productImage2Map1);
                                arrayList8.add(productId2Map1);
                                arrayList11.add(productPrice2Map1);
                            }


                            if(productNameArrList2.size()>=3  || productNameArrList2.size()>=4){

                                HashMap<String, String> productName2Map2 = new HashMap<String, String>();
                                HashMap<String, String> productImage2Map2 = new HashMap<String, String>();
                                HashMap<String, String> productId2Map2 = new HashMap<String, String>();
                                HashMap<String, String> productPrice2Map2 = new HashMap<String, String>();

                                if(productNameArrList2.size()>=4){
                                    productName2Map2.put("Key1",productNameArrList2.get(2).toString());
                                    productImage2Map2.put("Key1",picArrList2.get(2).toString());
                                    productId2Map2.put("Key1",productIdArrList2.get(2).toString());
                                    productPrice2Map2.put("Key1",productPriceArrList2.get(2).toString());

                                    productName2Map2.put("Key2",productNameArrList2.get(3).toString());
                                    productImage2Map2.put("Key2",picArrList2.get(3).toString());
                                    productId2Map2.put("Key2",productIdArrList2.get(3).toString());
                                    productPrice2Map2.put("Key2",productPriceArrList2.get(3).toString());

                                }else  if(productNameArrList2.size()>=3){
                                    productName2Map2.put("Key1",productNameArrList2.get(2).toString());
                                    productImage2Map2.put("Key1",picArrList2.get(2).toString());
                                    productId2Map2.put("Key1",productIdArrList2.get(2).toString());
                                    productPrice2Map2.put("Key1",productPriceArrList2.get(2).toString());

                                    productName2Map2.put("Key2","");
                                    productImage2Map2.put("Key2","");
                                    productId2Map2.put("Key2","");
                                    productPrice2Map2.put("Key2","");
                                }

                                arrayList3.add(productName2Map2);
                                arrayList4.add(productImage2Map2);
                                arrayList8.add(productId2Map2);
                                arrayList11.add(productPrice2Map2);
                            }

                            if(productNameArrList2.size()>=5  || productNameArrList2.size()>=6){

                                HashMap<String, String> productName2Map3 = new HashMap<String, String>();
                                HashMap<String, String> productImage2Map3 = new HashMap<String, String>();
                                HashMap<String, String> productId2Map3 = new HashMap<String, String>();
                                HashMap<String, String> productPrice2Map3 = new HashMap<String, String>();

                                if(productNameArrList2.size()>=6){
                                    productName2Map3.put("Key1",productNameArrList2.get(4).toString());
                                    productImage2Map3.put("Key1",picArrList2.get(4).toString());
                                    productId2Map3.put("Key1",productIdArrList2.get(4).toString());
                                    productPrice2Map3.put("Key1",productPriceArrList2.get(4).toString());

                                    productName2Map3.put("Key2",productNameArrList2.get(5).toString());
                                    productImage2Map3.put("Key2",picArrList2.get(5).toString());
                                    productId2Map3.put("Key2",productIdArrList2.get(5).toString());
                                    productPrice2Map3.put("Key2",productPriceArrList2.get(5).toString());
                                }else if(productNameArrList2.size()>=5){
                                    productName2Map3.put("Key1",productNameArrList2.get(4).toString());
                                    productImage2Map3.put("Key1",picArrList2.get(4).toString());
                                    productId2Map3.put("Key1",productIdArrList2.get(4).toString());
                                    productPrice2Map3.put("Key1",productPriceArrList2.get(4).toString());

                                    productName2Map3.put("Key2","");
                                    productImage2Map3.put("Key2","");
                                    productId2Map3.put("Key2","");
                                    productPrice2Map3.put("Key2","");
                                }

                                arrayList3.add(productName2Map3);
                                arrayList4.add(productImage2Map3);
                                arrayList8.add(productId2Map3);
                                arrayList11.add(productPrice2Map3);

                            }

                            bestSellerAdapter = new BestSellerAdapter(getActivity(), arrayList3, arrayList4, arrayList8, arrayList11);
                            vPagerImages2.setAdapter(bestSellerAdapter);
                            tabDots2.setupWithViewPager(vPagerImages2, true);

//                            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//
//                                vPagerImages2.setCurrentItem(arrayList3.size()-1);
//                            }

                            if(productNameArrList3.size()>=1  || productNameArrList3.size()>=2){

                                HashMap<String, String> productName3Map1 = new HashMap<String, String>();
                                HashMap<String, String> productImage3Map1 = new HashMap<String, String>();
                                HashMap<String, String> productId3Map1 = new HashMap<String, String>();
                                HashMap<String, String> productPrice3Map1 = new HashMap<String, String>();

                                if(productNameArrList3.size()>=2){

                                    productName3Map1.put("Key1",productNameArrList3.get(0).toString());
                                    productImage3Map1.put("Key1",picArrList3.get(0).toString());
                                    productId3Map1.put("Key1",productIdArrList3.get(0).toString());
                                    productPrice3Map1.put("Key1",productPriceArrList3.get(0).toString());

                                    productName3Map1.put("Key2",productNameArrList3.get(1).toString());
                                    productImage3Map1.put("Key2",picArrList3.get(1).toString());
                                    productId3Map1.put("Key2",productIdArrList3.get(1).toString());
                                    productPrice3Map1.put("Key2",productPriceArrList3.get(1).toString());

                                }else if(productNameArrList3.size()>=1){

                                    productName3Map1.put("Key1",productNameArrList3.get(0).toString());
                                    productImage3Map1.put("Key1",picArrList3.get(0).toString());
                                    productId3Map1.put("Key1",productIdArrList3.get(0).toString());
                                    productPrice3Map1.put("Key1",productPriceArrList3.get(0).toString());

                                    productName3Map1.put("Key2","");
                                    productImage3Map1.put("Key2","");
                                    productId3Map1.put("Key2","");
                                    productPrice3Map1.put("Key2","");

                                }


                                arrayList5.add(productName3Map1);
                                arrayList6.add(productImage3Map1);
                                arrayList9.add(productId3Map1);
                                arrayList12.add(productPrice3Map1);

                            }

                            if(productNameArrList3.size()>=3 || productNameArrList3.size()>=4){

                                HashMap<String, String> productName3Map2 = new HashMap<String, String>();
                                HashMap<String, String> productImage3Map2 = new HashMap<String, String>();
                                HashMap<String, String> productId3Map2 = new HashMap<String, String>();
                                HashMap<String, String> productPrice3Map2 = new HashMap<String, String>();

                                if(productNameArrList3.size()>=4){

                                    productName3Map2.put("Key1",productNameArrList3.get(2).toString());
                                    productImage3Map2.put("Key1",picArrList3.get(2).toString());
                                    productId3Map2.put("Key1",productIdArrList3.get(2).toString());
                                    productPrice3Map2.put("Key1",productPriceArrList3.get(2).toString());

                                    productName3Map2.put("Key2",productNameArrList3.get(3).toString());
                                    productImage3Map2.put("Key2",picArrList3.get(3).toString());
                                    productId3Map2.put("Key2",productIdArrList3.get(3).toString());
                                    productPrice3Map2.put("Key2",productPriceArrList3.get(3).toString());
                                }else if(productNameArrList3.size()>=3){

                                    productName3Map2.put("Key1",productNameArrList3.get(2).toString());
                                    productImage3Map2.put("Key1",picArrList3.get(2).toString());
                                    productId3Map2.put("Key1",productIdArrList3.get(2).toString());
                                    productPrice3Map2.put("Key1",productPriceArrList3.get(2).toString());

                                    productName3Map2.put("Key2","");
                                    productImage3Map2.put("Key2","");
                                    productId3Map2.put("Key2","");
                                    productPrice3Map2.put("Key2",productPriceArrList3.get(2).toString());

                                }

                                arrayList5.add(productName3Map2);
                                arrayList6.add(productImage3Map2);
                                arrayList9.add(productId3Map2);
                                arrayList12.add(productPrice3Map2);
                            }

                            if(productNameArrList3.size()>=5 || productNameArrList3.size()>=6){

                                HashMap<String, String> productName3Map3 = new HashMap<String, String>();
                                HashMap<String, String> productImage3Map3 = new HashMap<String, String>();
                                HashMap<String, String> productId3Map3 = new HashMap<String, String>();
                                HashMap<String, String> productPrice3Map3 = new HashMap<String, String>();

                                if(productNameArrList3.size()>=6){

                                    productName3Map3.put("Key1",productNameArrList3.get(4).toString());
                                    productImage3Map3.put("Key1",picArrList3.get(4).toString());
                                    productId3Map3.put("Key1",productIdArrList3.get(4).toString());
                                    productPrice3Map3.put("Key1",productPriceArrList3.get(4).toString());

                                    productName3Map3.put("Key2",productNameArrList3.get(5).toString());
                                    productImage3Map3.put("Key2",picArrList3.get(5).toString());
                                    productId3Map3.put("Key2",productIdArrList3.get(5).toString());
                                    productPrice3Map3.put("Key2",productPriceArrList3.get(5).toString());

                                }else if(productNameArrList3.size()>=5){

                                    productName3Map3.put("Key1",productNameArrList3.get(4).toString());
                                    productImage3Map3.put("Key1",picArrList3.get(4).toString());
                                    productId3Map3.put("Key1",productIdArrList3.get(4).toString());
                                    productPrice3Map3.put("Key1",productPriceArrList3.get(4).toString());

                                    productName3Map3.put("Key2","");
                                    productImage3Map3.put("Key2","");
                                    productId3Map3.put("Key2","");
                                    productPrice3Map3.put("Key2","");

                                }

                                arrayList5.add(productName3Map3);
                                arrayList6.add(productImage3Map3);
                                arrayList9.add(productId3Map3);
                                arrayList12.add(productPrice3Map3);
                            }


                            editorialChoiceAdapter = new EditorialChoiceAdapter(getActivity(), arrayList5, arrayList6, arrayList9, arrayList12);
                            vPagerImages3.setAdapter(editorialChoiceAdapter);
                            tabDots3.setupWithViewPager(vPagerImages3, true);

//                            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//
//                                vPagerImages3.setCurrentItem(arrayList5.size()-1);
//                            }

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                        if(!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")){
//                            loadProfileData();
                        }


                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }


        }

    }

    private void loadProfileData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("ran", GlobalFunctions.getRandom());

            client.post(GlobalFunctions.serviceURL + "getProfile", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                    //
                    hideLoading();
                    //
                    JSONObject obj;
                    //
                    String response = new String(bytes);
                    //
                    Log.d("onSuccess", response);
                    //
                    try {
                        //
                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {

//                            txtName.setText(obj.getString("name"));
//                            txtMobile.setText(obj.getString("mobile"));
//                            txtEmail.setText(obj.getString("email"));

                            HashMap<String, String> data = new HashMap<>();
                            data.put("name", ""+obj.getString("name"));
                            data.put("username", "");
                            data.put("email", ""+obj.getString("email"));
                            data.put("organization", "");
                            data.put("phone", ""+obj.getString("mobile"));

                            //providing any custom key values to store with user
                            HashMap<String, String> custom = new HashMap<>();
                            custom.put("country", "");
                            custom.put("city", "");
                            custom.put("address", "");

//                            //set multiple custom properties
//                            Countly.userData.setUserData(data, custom);
//
//                            //set custom properties by one
////                            Countly.userData.setProperty("test", "test");
//
//                            //increment used value by 1
//                            Countly.userData.incrementBy("used", 1);
//
//                            //insert value to array of unique values
////                            Countly.userData.pushUniqueValue("type", "");
//
//                            //insert multiple values to same property
////                            Countly.userData.pushUniqueValue("skill", "");
////                            Countly.userData.pushUniqueValue("skill", "earth");
//
//                            Countly.userData.save();

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }
                    //
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }
                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }

        }

    }

    private void showLoading() {

        if (dlgLoading == null) {

            dlgLoading = GlobalFunctions.showLoading(getActivity());

        } else {

            dlgLoading.show();
        }

    }

    private void hideLoading() {

        dlgLoading.dismiss();

    }
}
