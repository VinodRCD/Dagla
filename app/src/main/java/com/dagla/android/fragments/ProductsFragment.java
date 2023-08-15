package com.dagla.android.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.FilterColorsAdapter;
import com.dagla.android.adapter.FilterSizesAdapter;
import com.dagla.android.adapter.ItemsAdapter;
import com.dagla.android.parser.FilterColorsDetails;
import com.dagla.android.parser.FilterSizesDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
//import ly.count.android.sdk.Countly;

public class ProductsFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    NestedScrollView nsv;
    RecyclerView rvItems;
    RelativeLayout pnlLoading, pnlNA, pnlDim, pnlSortBy;
    RadioGroup rgSortBy, rgDirection;
    Button btnApply, btnCancel;

    Dialog dlgLoading = null;

    public int pageNum=1;

    boolean canLoad=true, isLoading=false;

    String homeBannerId="0", categoryId="0", subCategoryId="0", search="", sortBy="", sortDirection="", title, brandId="0";

    ArrayList<String> arrItems;

    RecyclerView.Adapter adapterItems;

    GridLayoutManager layoutManager;

    DisplayMetrics displaymetrics;

    FilterSizesDetails filterSizesDetails;
    ArrayList<FilterSizesDetails> filterSizesDetailsArrayList = new ArrayList<FilterSizesDetails>();
    FilterColorsDetails filterColorsDetails;
    ArrayList<FilterColorsDetails> filterColorsDetailsArrayList = new ArrayList<FilterColorsDetails>();

    FilterSizesAdapter filterSizesAdapter;
    FilterColorsAdapter filterColorsAdapter;

    String colorIds = "";
    String sizeIds = "";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if (getArguments().getString("brand_id")!= null) {
            //
            brandId = getArguments().getString("brand_id");
            Log.v("BrandId",brandId);
        }

        if (getArguments().getString("home_banner_id")!= null) {
            //
            homeBannerId = getArguments().getString("home_banner_id");
            Log.v("HomeBannerId",homeBannerId);
        }


        if (getArguments().getString("cat_id")!= null) {
            //
            categoryId = getArguments().getString("cat_id");
            Log.v("CategoryId",categoryId);
        }


        //
        if (getArguments().getString("sub_cat_id")!= null) {
            //
            subCategoryId = getArguments().getString("sub_cat_id");
            Log.v("SubCategoryId",subCategoryId);
            //
        }

        if (getArguments().getString("search")!= null) {
            search =  getArguments().getString("search");
            Log.v("Search",search);

            HashMap<String, String> segmentation = new HashMap<String, String>();
            segmentation.put("Searched With", ""+search);

//            Countly.sharedInstance().recordEvent("Searched Mostly", segmentation, 1);
        }

        if (getArguments().getString("title")!= null) {
            title =  getArguments().getString("title");
            Log.v("Title",title);
        }

        if (getArguments().getString("home_banner_name")!= null) {
            String title = getArguments().getString("home_banner_name");
            ((MainActivity) getActivity()).setHeaders(title,false,false,true,true,true ,"1", false);
        }else {
//            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.essentials),false,false,false,true,true);
            ((MainActivity) getActivity()).setHeaders(title,false,false,true,true,true ,"1", false);
        }


        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_products_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_products, container, false);
            }


            nsv = rootView.findViewById(R.id.nsv);
            rvItems = rootView.findViewById(R.id.rvItems);
            pnlLoading = rootView.findViewById(R.id.pnlLoading);
            pnlNA = rootView.findViewById(R.id.pnlNA);
            pnlDim = rootView.findViewById(R.id.pnlDim);
            pnlSortBy = rootView.findViewById(R.id.pnlSortBy);
            rgSortBy = rootView.findViewById(R.id.rgSortBy);
            rgDirection = rootView.findViewById(R.id.rgDirection);
            btnApply = rootView.findViewById(R.id.btnApply);
            btnCancel = rootView.findViewById(R.id.btnCancel);

            rvItems.setNestedScrollingEnabled(false);
            //
            layoutManager = new GridLayoutManager(getActivity(), 2);
            //
            rvItems.setLayoutManager(layoutManager);
            //
            displaymetrics = new DisplayMetrics();
            //
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //




//            if (getIntent().hasExtra("search")) {
//                //
//                search = getIntent().getStringExtra("search");
//                //
//            }
//            //
//            if (getIntent().hasExtra("title")) {
//                //
//                ab.setTitle(getIntent().getStringExtra("title"));
//                //
//            }
            //
//            loadData();

            sizesData();

            //
            nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (canLoad && !isLoading && scrollY == ((v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()))) {

                        pageNum++;

                        loadData();
                    }

                }
            });
            //
            pnlDim.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hideSortBy();
                }
            });

            FloatingActionButton fab = rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*
                    public void smoothScrollToPosition (int position)
                        Smoothly scroll to the specified adapter position. The view
                        will scroll such that the indicated position is displayed.

                        position : Scroll to this adapter position.
                 */
                    // Scroll to position zero, first item of ListView
//                    rvItems.smoothScrollToPosition(0);

                    nsv.fullScroll(View.FOCUS_UP);
                    nsv.scrollTo(0,0);
                }
            });

            //
            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    hideSortBy();

                    switch (rgSortBy.getCheckedRadioButtonId()) {

                        case R.id.rbNewest:

                            sortBy = "date";
                            break;

                        case R.id.rbTitle:

                            sortBy = "name";
                            break;

                        case R.id.rbPrice:

                            sortBy = "price";
                            break;
                    }

                    switch (rgDirection.getCheckedRadioButtonId()) {

                        case R.id.rbASC:

                            sortDirection = "asc";
                            break;

                        case R.id.rbDESC:

                            sortDirection = "desc";
                            break;
                    }

                    pageNum = 1;

                    loadData();

                }
            });
            //
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSortBy();
                }
            });

        }

        return rootView;

    }


    private void loadData() {

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

            if(!categoryId.equals(subCategoryId)){
                params.put("subCategoryId", subCategoryId);
            }

            params.put("search", search);
            params.put("sortBy", sortBy);
            params.put("sortDirection", sortDirection);
            params.put("pageNum", pageNum);
            params.put("ran", GlobalFunctions.getRandom());

            params.put("colorid", colorIds);
            params.put("sizeid", sizeIds);

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

                                rvItems.setAdapter(adapterItems);

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

    private void showSortBy() {

        pnlSortBy.setTranslationY(GlobalFunctions.convertDpToPx(getActivity(), -130));

        pnlSortBy.setVisibility(View.VISIBLE);

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(pnlSortBy,
                PropertyValuesHolder.ofFloat("translationY", 0));

        animator.setInterpolator(new BounceInterpolator());
        animator.setDuration(600);
        animator.start();

        pnlDim.setVisibility(View.VISIBLE);

        pnlDim.animate().setDuration(600).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                pnlDim.setAlpha(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

    }

    private void hideSortBy() {

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(pnlSortBy,
                PropertyValuesHolder.ofFloat("translationY", GlobalFunctions.convertDpToPx(getActivity(), -130)));

        animator.setDuration(600);
        animator.start();

        pnlDim.animate().setDuration(600).alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                pnlSortBy.setVisibility(View.GONE);

                pnlDim.setVisibility(View.GONE);

                pnlDim.setAlpha(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();

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

    public void productsSorting() {

        if (pnlDim.getVisibility() == View.VISIBLE) {
            hideSortBy();
        } else {
            showSortBy();
        }

    }

    private void sizesData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();
//            https://portal.dagla.com/services/ajax_v2.aspx?app=ios&lang=en&ver=1.0&cat=getSizes&ran=71&subcatid=LyCV/+D1ogs=&catid=2ucHke6VLD8=
            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

//            params.put("brandId", brandId);
//            params.put("home_banner_id", homeBannerId);
            params.put("catid", categoryId);
            if(!categoryId.equals(subCategoryId)){
                params.put("subcatid", subCategoryId);
            }

//            params.put("search", search);
//            params.put("sortBy", sortBy);
//            params.put("sortDirection", sortDirection);
//            params.put("pageNum", pageNum);
            params.put("ran", GlobalFunctions.getRandom());

//            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
//                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
//            }

            client.get(GlobalFunctions.serviceURL + "getSizes" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    filterSizesDetailsArrayList.clear();

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {

                                String size_id = arr.getJSONObject(i).getString("size_id");
                                String size_name = arr.getJSONObject(i).getString("size_name");



                                filterSizesDetails = new FilterSizesDetails(size_id, size_name);

                                filterSizesDetailsArrayList.add(filterSizesDetails);

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

                    colorsData();
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

    private void colorsData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();
//            https://portal.dagla.com/services/ajax_v2.aspx?app=ios&lang=en&ver=1.0&cat=getSizes&ran=71&subcatid=LyCV/+D1ogs=&catid=2ucHke6VLD8=
            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

//            params.put("brandId", brandId);
//            params.put("home_banner_id", homeBannerId);
            params.put("catid", categoryId);
            if(!categoryId.equals(subCategoryId)){
                params.put("subcatid", subCategoryId);
            }
//            params.put("search", search);
//            params.put("sortBy", sortBy);
//            params.put("sortDirection", sortDirection);
//            params.put("pageNum", pageNum);
            params.put("ran", GlobalFunctions.getRandom());

//            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
//                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
//            }

            client.get(GlobalFunctions.serviceURL + "getColors" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {


                    filterColorsDetailsArrayList.clear();

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {

                                String color_id = arr.getJSONObject(i).getString("color_id");
                                String color_code = arr.getJSONObject(i).getString("color_code");
                                String color_name = arr.getJSONObject(i).getString("color_name");



                                filterColorsDetails = new FilterColorsDetails(color_id, color_code,color_name);

                                filterColorsDetailsArrayList.add(filterColorsDetails);
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

                    loadData();

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


    public void filterDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_layout);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
//        layoutParams.x = 0;   //x position
//        layoutParams.y = 30;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnDone = (Button) dialog.findViewById(R.id.btnDone);
        Button btnSize = (Button) dialog.findViewById(R.id.btnSize);
        Button btnColor = (Button) dialog.findViewById(R.id.btnColor);

        LinearLayout sizesLayout = (LinearLayout) dialog.findViewById(R.id.sizesLayout);
        LinearLayout colorsLayout = (LinearLayout) dialog.findViewById(R.id.colorsLayout);


        RecyclerView sizes_recyclerView = dialog.findViewById(R.id.sizes_recyclerView);

//        sizes_recyclerView.setNestedScrollingEnabled(false);
        //
        layoutManager = new GridLayoutManager(getActivity(), 1);
        //
        sizes_recyclerView.setLayoutManager(layoutManager);

        filterSizesAdapter = new FilterSizesAdapter(requireActivity(), filterSizesDetailsArrayList);
        sizes_recyclerView.setAdapter(filterSizesAdapter);

        filterSizesAdapter.setOnClickListener((position, v) -> {

            if (filterSizesDetailsArrayList.get(position).isSelected()) {

                filterSizesDetailsArrayList.get(position).setSelected(false);

            }else {
                filterSizesDetailsArrayList.get(position).setSelected(true);
            }

            filterSizesAdapter.Selected(position);


        });


        RecyclerView colors_recyclerView = dialog.findViewById(R.id.colors_recyclerView);

//        sizes_recyclerView.setNestedScrollingEnabled(false);
        //
        layoutManager = new GridLayoutManager(getActivity(), 1);
        //
        colors_recyclerView.setLayoutManager(layoutManager);

        filterColorsAdapter = new FilterColorsAdapter(requireActivity(), filterColorsDetailsArrayList);
        colors_recyclerView.setAdapter(filterColorsAdapter);

//        filterColorsAdapter.setOnClickListener((position, v) -> {
//            filterColorsAdapter.Selected(position)
//        });


        filterColorsAdapter.setOnClickListener((position, v) -> {

            if (filterColorsDetailsArrayList.get(position).isSelected()) {

                filterColorsDetailsArrayList.get(position).setSelected(false);

            }else {
                filterColorsDetailsArrayList.get(position).setSelected(true);
            }


            filterColorsAdapter.Selected(position);
        });


        btnCancel.setOnClickListener(view -> dialog.dismiss());

//        btnDone.setOnClickListener(view -> dialog.dismiss());

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringBuilder s1 = new StringBuilder();

                for (int i = 0; i < filterSizesDetailsArrayList.size(); i++) {

                    if (filterSizesDetailsArrayList.get(i).isSelected()) {
                        s1.append(filterSizesDetailsArrayList.get(i).getSizeId());

                        if (i == filterSizesDetailsArrayList.size() - 1) {
//                        s.append("]")
                        } else {

                            s1.append(",");

                        }
                    }

                }

                sizeIds = s1.toString();

                Log.d("SizeIds", sizeIds);

                StringBuilder s2 = new StringBuilder();

                for (int i = 0; i < filterColorsDetailsArrayList.size(); i++) {

                    if (filterColorsDetailsArrayList.get(i).isSelected()) {
                        s2.append(filterColorsDetailsArrayList.get(i).getColorId());

                        if (i == filterColorsDetailsArrayList.size() - 1) {
//                        s.append("]")
                        } else {

                            s2.append(",");

                        }
                    }

                }

                colorIds = s2.toString();

                Log.d("ColorIds", colorIds);

                dialog.dismiss();

                pageNum =  1;
                canLoad = true;
                isLoading = false;

                loadData();

            }
        });

        sizesLayout.setVisibility(View.VISIBLE);
        colorsLayout.setVisibility(View.GONE);

        btnSize.setBackgroundResource(R.drawable.customer_btn_2_bg);
        btnColor.setBackgroundResource(R.drawable.customer_btn_1_bg);

        btnSize.setOnClickListener(view -> {

            btnSize.setBackgroundResource(R.drawable.customer_btn_2_bg);
            btnColor.setBackgroundResource(R.drawable.customer_btn_1_bg);

            sizesLayout.setVisibility(View.VISIBLE);
            colorsLayout.setVisibility(View.GONE);

        });

        btnColor.setOnClickListener(view -> {

            btnSize.setBackgroundResource(R.drawable.customer_btn_1_bg);
            btnColor.setBackgroundResource(R.drawable.customer_btn_2_bg);

            sizesLayout.setVisibility(View.GONE);
            colorsLayout.setVisibility(View.VISIBLE);

        });

        dialog.show();

    }

}
