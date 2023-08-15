package com.dagla.android.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.HomeCateAdapter;
import com.dagla.android.adapter.ItemsAdapter;
import com.dagla.android.parser.HomeBannerDetails;
import com.dagla.android.parser.HomeCateDetails;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class HomeFragmentNew extends Fragment {

    Activity act;

    ViewPager vPagerImages;
    TabLayout tabDots;
    View rootView;
    Bundle savedInstanceState;



    Dialog dlgLoading = null;

    HomeBannerDetails homeBannerDetails;
    ArrayList<HomeBannerDetails> homeBannerDetailsArrayList = new ArrayList<HomeBannerDetails>();
    HomeCateDetails homeCateDetails;
    ArrayList<HomeCateDetails> homeCateDetailsArrayList = new ArrayList<HomeCateDetails>();

    RecyclerView cate_recyclerView,products_recyclerView;
    HomeCateAdapter homeCateAdapter;

    ArrayList<String> arrItems;

    DisplayMetrics displaymetrics;
    GridLayoutManager layoutManager;

    Button btnMen,btnWomen,btnJunior;
    RelativeLayout pnlLoading, pnlNA;
    NestedScrollView nsv;

    public int pageNum=1;

    boolean canLoad=true, isLoading=false;

    String homeBannerId="0", categoryId="0", subCategoryId="0", search="", sortBy="", sortDirection="", title, brandId="0";

    RecyclerView.Adapter adapterItems;

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
                rootView = inflater.inflate(R.layout.fragment_home_new_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_home_new, container, false);
            }

            displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

            vPagerImages = rootView.findViewById(R.id.vPagerImages);
            tabDots = rootView.findViewById(R.id.tabDots);

            btnMen = rootView.findViewById(R.id.btnMen);
            btnWomen = rootView.findViewById(R.id.btnWomen);
            btnJunior = rootView.findViewById(R.id.btnJunior);
            nsv = rootView.findViewById(R.id.nsv);
            pnlLoading = rootView.findViewById(R.id.pnlLoading);
            pnlNA = rootView.findViewById(R.id.pnlNA);



            cate_recyclerView = rootView.findViewById(R.id.cate_recyclerView);

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                cate_recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, true));
            }else {
                cate_recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
            }

            homeCateAdapter = new HomeCateAdapter(requireActivity(), homeCateDetailsArrayList);
            cate_recyclerView.setAdapter(homeCateAdapter);

            homeCateAdapter.setOnClickListener(new HomeCateAdapter.ClickListener() {
                @Override
                public void OnItemClick(int position, View v) {

                    homeCateAdapter.Selected(position);
                    categoryId = homeCateDetailsArrayList.get(position).getCateId();
                    loadProductsData();
                }
            });

            products_recyclerView = rootView.findViewById(R.id.products_recyclerView);

            products_recyclerView.setNestedScrollingEnabled(false);
            //
            layoutManager = new GridLayoutManager(getActivity(), 2);
            //
            products_recyclerView.setLayoutManager(layoutManager);

//            homeCateProductsAdapter = new HomeCateProductsAdapter(requireActivity(), homeCateProductsDetailsArrayList,displaymetrics.widthPixels);
//            products_recyclerView.setAdapter(homeCateProductsAdapter);
//
//            homeCateProductsAdapter.setOnClickListener(new HomeCateProductsAdapter.ClickListener() {
//                @Override
//                public void OnItemClick(int position, View v) {
//
//                    homeCateProductsAdapter.Selected(position);
////                    homeCateProductsDetailsArrayList.clear();
////                    homeCateProductsAdapter = new HomeCateProductsAdapter(requireActivity(), homeCateDetailsArrayList.get(position).getHomeCateProducts(),displaymetrics.widthPixels);
////                    products_recyclerView.setAdapter(homeCateProductsAdapter);
//                }
//            });



            btnMen.setBackgroundResource(R.drawable.bg_1);
            btnMen.setTextColor(Color.parseColor("#FFFFFF"));
            btnWomen.setBackgroundResource(R.drawable.bg_2);
            btnWomen.setTextColor(Color.parseColor("#222222"));
            btnJunior.setBackgroundResource(R.drawable.bg_2);
            btnJunior.setTextColor(Color.parseColor("#222222"));

            categoryId = "CIlnSO+5vP8=";

            btnMen.setOnClickListener(v -> {

                btnMen.setBackgroundResource(R.drawable.bg_1);
                btnMen.setTextColor(Color.parseColor("#FFFFFF"));
                btnWomen.setBackgroundResource(R.drawable.bg_2);
                btnWomen.setTextColor(Color.parseColor("#222222"));
                btnJunior.setBackgroundResource(R.drawable.bg_2);
                btnJunior.setTextColor(Color.parseColor("#222222"));

                categoryId = "CIlnSO+5vP8=";

                pageNum=1;
                canLoad = true;
                isLoading = false;

                loadProductsData();

            });

            btnWomen.setOnClickListener(v -> {

                btnMen.setBackgroundResource(R.drawable.bg_2);
                btnMen.setTextColor(Color.parseColor("#222222"));
                btnWomen.setBackgroundResource(R.drawable.bg_1);
                btnWomen.setTextColor(Color.parseColor("#FFFFFF"));
                btnJunior.setBackgroundResource(R.drawable.bg_2);
                btnJunior.setTextColor(Color.parseColor("#222222"));

                categoryId = "nrV7TSMKyNk=";

                pageNum=1;
                canLoad = true;
                isLoading = false;

                loadProductsData();

            });

            btnJunior.setOnClickListener(v -> {

                btnMen.setBackgroundResource(R.drawable.bg_2);
                btnMen.setTextColor(Color.parseColor("#222222"));
                btnWomen.setBackgroundResource(R.drawable.bg_2);
                btnWomen.setTextColor(Color.parseColor("#222222"));
                btnJunior.setBackgroundResource(R.drawable.bg_1);
                btnJunior.setTextColor(Color.parseColor("#FFFFFF"));

                categoryId = "ymbsS2nKWRo=";

                pageNum=1;
                canLoad = true;
                isLoading = false;

                loadProductsData();

            });

            nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (canLoad && !isLoading && scrollY == ((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))) {

                        pageNum++;

                        loadProductsData();
                    }

                }
            });

            loadBannerData();


        }



        return rootView;

    }

    private void loadBannerData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            homeBannerDetailsArrayList.clear();
//            homeCateDetailsArrayList.clear();

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



                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("Banners");


                            for (int i = 0; i < arr.length(); i++) {


                                String main_banner_Id = arr.getJSONObject(i).getString("banner_id");
                                String home_banner_cat_id = arr.getJSONObject(i).getString("home_banner_cat_id");
                                String home_banner_cat_name_en = arr.getJSONObject(i).getString("home_banner_cat_name");
                                String home_banner_cat_name_ar = arr.getJSONObject(i).getString("home_banner_cat_name_ar");
                                String banner_cat_id = arr.getJSONObject(i).getString("banner_cat_id");
                                String banner_sub_cat_id = arr.getJSONObject(i).getString("banner_sub_cat_id");
                                String banner_product_id = arr.getJSONObject(i).getString("banner_product_id");
                                String banner_brand_id = arr.getJSONObject(i).getString("banner_brand_id");
                                String image = arr.getJSONObject(i).getString("image");

                                homeBannerDetails = new HomeBannerDetails(main_banner_Id,home_banner_cat_id,home_banner_cat_name_en,banner_cat_id,
                                        banner_sub_cat_id,banner_product_id,banner_brand_id,image);
                                homeBannerDetailsArrayList.add(homeBannerDetails);


//                                arr1 = arr.getJSONObject(i).getJSONArray("Products");
//
//                                homeCateProductsDetailsArrayList = new ArrayList<HomeCateProductsDetails>();
//
//                                for (int j = 0; j < arr1.length(); j++) {
//
//                                    String banner_id = arr1.getJSONObject(j).getString("banner_id");
//                                    String product_id = arr1.getJSONObject(j).getString("product_id");
//                                    String name = arr1.getJSONObject(j).getString("name");
//                                    String brand_name = arr1.getJSONObject(j).getString("brand_name");
//                                    String price = arr1.getJSONObject(j).getString("price");
//                                    String old_price = arr1.getJSONObject(j).getString("old_price");
//
//                                    String pic = arr1.getJSONObject(j).getString("pic");
//
//
//                                    homeCateProductsDetails = new HomeCateProductsDetails(banner_id, product_id, name, brand_name, price, old_price, pic);
//
//                                    homeCateProductsDetailsArrayList.add(homeCateProductsDetails);
//
//                                }
//
//
//
//                                homeBannerDetails = new HomeBannerDetails(main_banner_Id,home_banner_cat_id,home_banner_cat_name_en,banner_cat_id,
//                                        banner_sub_cat_id,banner_product_id,banner_brand_id,image,homeCateProductsDetailsArrayList);
//                                homeBannerDetailsArrayList.add(homeBannerDetails);

                            }


                            CustomImagePagerAdapter mAdapter = new CustomImagePagerAdapter(getActivity(), homeBannerDetailsArrayList);
                            vPagerImages.setAdapter(mAdapter);
                            vPagerImages.setCurrentItem(0);
                            tabDots.setupWithViewPager(vPagerImages, true);

//                            homeCateAdapter.notifyDataSetChanged();

//                            homeCateProductsAdapter.notifyDataSetChanged();

//                            loadProductsData();

                            loadCategoriesData();

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

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

    private void loadCategoriesData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

//            homeBannerDetailsArrayList.clear();
            homeCateDetailsArrayList.clear();

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }


//            client.get(GlobalFunctions.serviceURL + "getHomeBannerData", params, new AsyncHttpResponseHandler() {
                            client.get(GlobalFunctions.serviceURL + "getHomeData", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1, arr2;
                    JSONObject obj, obj1, obj2;



                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("home_cats");


                            for (int i = 0; i < arr.length(); i++) {


                                String cat_id = arr.getJSONObject(i).getString("cat_id");
                                String cat = arr.getJSONObject(i).getString("cat");

                                homeCateDetails = new HomeCateDetails(cat_id,cat);
                                homeCateDetailsArrayList.add(homeCateDetails);

                                if(i==0){
                                    categoryId = homeCateDetailsArrayList.get(i).getCateId();
                                }
                            }

                            homeCateAdapter.notifyDataSetChanged();


                            loadProductsData();

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

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


    private void loadProductsData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            isLoading = true;

            if (pageNum == 1) {

                showLoading();

            } else {

                pnlLoading.setVisibility(View.VISIBLE);
            }

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("brandId", brandId);
            params.put("home_banner_id", homeBannerId);
            params.put("categoryId", categoryId);
            params.put("subCategoryId", subCategoryId);
            params.put("search", search);
            params.put("sortBy", sortBy);
            params.put("sortDirection", sortDirection);
            params.put("pageNum", pageNum);
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

            client.get(GlobalFunctions.serviceURL + "getItems" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    if (pageNum == 1) {

                        hideLoading();

                    } else {

                        pnlLoading.setVisibility(View.GONE);
                    }

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            canLoad = obj.getBoolean("can_load");

                            if (pageNum == 1) {

                                sortBy = obj.getString("sort_by");
                                sortDirection = obj.getString("sort_direction");

                                arrItems = new ArrayList<String>();
                            }

                            arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {

                                arrItems.add(arr.getJSONObject(i).toString());

                            }

                            if (pageNum == 1) {

                                adapterItems = new ItemsAdapter(getActivity(), arrItems, displaymetrics.widthPixels);

                                products_recyclerView.setAdapter(adapterItems);

                                if (arr.length() == 0) {

                                    pnlNA.setVisibility(View.VISIBLE);

                                } else {

                                    pnlNA.setVisibility(View.GONE);

                                }

                            } else {

                                adapterItems.notifyDataSetChanged();

                            }

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

                    isLoading = false;
//                    sizesData();
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



    public class CustomImagePagerAdapter extends PagerAdapter {
        private Context mContext;
        LayoutInflater mLayoutInflater;
        ArrayList<HomeBannerDetails> homeBannerDetailsArrayList1;

        public CustomImagePagerAdapter(Context context, ArrayList<HomeBannerDetails> homeBannerDetailsArrayList1) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.homeBannerDetailsArrayList1 = homeBannerDetailsArrayList1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View itemView = mLayoutInflater.inflate(R.layout.home_image_item,container,false);
            ImageView adImg = (ImageView) itemView.findViewById(R.id.adImg);
//            ImageView favoriteImg = (ImageView) itemView.findViewById(R.id.favoriteImg);

//            adImg.getLayoutParams().width = (int) (General.get_device_width(mContext) / 1) - 50;
//            adImg.getLayoutParams().height = (int) (General.get_device_width(mContext) / 2) - 80;

//            Picasso.with(mContext).load(mResources[position]).into(adImg);

            final ProgressBar progress1 = (ProgressBar) itemView.findViewById(R.id.progress);



            Glide.with(mContext)
                    .load(homeBannerDetailsArrayList1.get(position).getImage())
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
                    .into(adImg);


            adImg.setOnClickListener(v -> {

                if(!homeBannerDetailsArrayList1.get(position).getBannerCatId().equals("vpM1hJ6qjWc=")||
                        !homeBannerDetailsArrayList1.get(position).getBannerSubCatId().equals("vpM1hJ6qjWc=")||
                        !homeBannerDetailsArrayList1.get(position).getBannerProductId().equals("vpM1hJ6qjWc=")||
                        !homeBannerDetailsArrayList1.get(position).getBannerBrandId().equals("vpM1hJ6qjWc=")

                ){
                    if( homeBannerDetailsArrayList1.get(position).getBannerProductId()!=null&& !homeBannerDetailsArrayList1.get(position).getBannerProductId().equals("vpM1hJ6qjWc=")){
                        ProductDescriptionFromHomeFragment fragment = new ProductDescriptionFromHomeFragment();
                        Bundle b = new Bundle();
                        b.putString("product_id", homeBannerDetailsArrayList1.get(position).getBannerProductId());
                        b.putString("title", homeBannerDetailsArrayList1.get(position).getHomeBannerCatName());
                        b.putString("home_banner_name", homeBannerDetailsArrayList1.get(position).getHomeBannerCatName());
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
                        b.putString("cat_id", homeBannerDetailsArrayList1.get(position).getBannerCatId());
                        b.putString("sub_cat_id", homeBannerDetailsArrayList1.get(position).getBannerSubCatId());
                        b.putString("brand_id", homeBannerDetailsArrayList1.get(position).getBannerBrandId());
                        b.putString("home_banner_name", homeBannerDetailsArrayList1.get(position).getHomeBannerCatName());
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



            });


            container.addView(itemView);





            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
//        Log.v("mResources.length",""+mResources.length);
            return homeBannerDetailsArrayList1.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

}
