package com.dagla.android.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.ItemsAdapter2;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ProductsFromHomeFragment extends Fragment {

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
        }

        if (getArguments().getString("title")!= null) {
            title =  getArguments().getString("title");
            Log.v("Title",title);
        }

        if (getArguments().getString("home_banner_name")!= null) {
            String title = getArguments().getString("home_banner_name");
            ((MainActivity) getActivity()).setHeaders(title,false,false,true,true,true ,"2", false);
        }else {
//            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.essentials),false,false,false,true,true);
            ((MainActivity) getActivity()).setHeaders(title,false,false,true,true,true ,"2", false);
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

            loadData();
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
            params.put("banner_id", homeBannerId);
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

            client.get(GlobalFunctions.serviceURL + "getHomeBannerProductsData" , params, new AsyncHttpResponseHandler() {

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

//                            canLoad = obj.getBoolean("can_load");
//
//                            if (pageNum == 1) {

//                                sortBy = obj.getString("sort_by");
//                                sortDirection = obj.getString("sort_direction");

                                arrItems = new ArrayList<String>();
//                            }

                            arr = obj.getJSONArray("Products");

                            for (int i = 0; i < arr.length(); i++) {

                                arrItems.add(arr.getJSONObject(i).toString());

                            }

                            if (pageNum == 1) {

                                adapterItems = new ItemsAdapter2(getActivity(), arrItems, displaymetrics.widthPixels);

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
}
