package com.dagla.android;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ItemsActivity extends AppCompatActivity {

    Activity act;
    NestedScrollView nsv;
    RecyclerView rvItems;
    RelativeLayout pnlLoading, pnlNA, pnlDim, pnlSortBy;
    RadioGroup rgSortBy, rgDirection;
    Button btnApply, btnCancel;

    Dialog dlgLoading = null;

    public int pageNum=1;

    boolean canLoad=true, isLoading=false;

    String categoryId="0", subCategoryId="0", search="", sortBy="", sortDirection="";

    ArrayList<String> arrItems;

    RecyclerView.Adapter adapterItems;

    GridLayoutManager layoutManager;

    DisplayMetrics displaymetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_items);
        //
        act = this;
        nsv = findViewById(R.id.nsv);
        rvItems = findViewById(R.id.rvItems);
        pnlLoading = findViewById(R.id.pnlLoading);
        pnlNA = findViewById(R.id.pnlNA);
        pnlDim = findViewById(R.id.pnlDim);
        pnlSortBy = findViewById(R.id.pnlSortBy);
        rgSortBy = findViewById(R.id.rgSortBy);
        rgDirection = findViewById(R.id.rgDirection);
        btnApply = findViewById(R.id.btnApply);
        btnCancel = findViewById(R.id.btnCancel);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.items));
        //
        rvItems.setNestedScrollingEnabled(false);
        //
        layoutManager = new GridLayoutManager(act, 2);
        //
        rvItems.setLayoutManager(layoutManager);
        //
        displaymetrics = new DisplayMetrics();
        //
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //
        if (getIntent().hasExtra("cat_id")) {
            //
            categoryId = getIntent().getStringExtra("cat_id");
            //
        }
        //
        if (getIntent().hasExtra("sub_cat_id")) {
            //
            subCategoryId = getIntent().getStringExtra("sub_cat_id");
            //
        }
        //
        if (getIntent().hasExtra("search")) {
            //
            search = getIntent().getStringExtra("search");
            //
        }
        //
        if (getIntent().hasExtra("title")) {
            //
            ab.setTitle(getIntent().getStringExtra("title"));
            //
        }
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

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            isLoading = true;

            if (pageNum == 1) {

                showLoading();

            } else {

                pnlLoading.setVisibility(View.VISIBLE);
            }

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("categoryId", categoryId);
            params.put("subCategoryId", subCategoryId);
            params.put("search", search);
            params.put("sortBy", sortBy);
            params.put("sortDirection", sortDirection);
            params.put("pageNum", pageNum);
            params.put("ran", GlobalFunctions.getRandom());

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

                                adapterItems = new ItemsAdapter(act, arrItems, displaymetrics.widthPixels);

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

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }

                    isLoading = false;

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                }
            });

        } else {

            GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));

        }

    }

    private void showSortBy() {

        pnlSortBy.setTranslationY(GlobalFunctions.convertDpToPx(act, -130));

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
                PropertyValuesHolder.ofFloat("translationY", GlobalFunctions.convertDpToPx(act, -130)));

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

            dlgLoading = GlobalFunctions.showLoading(act);

        } else {

            dlgLoading.show();
        }

    }

    private void hideLoading() {

        dlgLoading.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    // Setting up back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                act.finish();
                return true;
            case R.id.itmSort:
                if (pnlDim.getVisibility() == View.VISIBLE) {
                    hideSortBy();
                } else {
                    showSortBy();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
