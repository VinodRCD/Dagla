package com.dagla.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ItemDetailsActivity extends AppCompatActivity {

    Activity act;
    ViewPager vPagerImages;
    ImageView imgOverlay, imgPlus;
    TabLayout tabDots;
    TextView lblName, lblPrice, lblDiscount, lblDescription, lblNew, lblSale;
    EditText txtSize;
    Button btnAddToCart;
    LinearLayout pnlRelated;
    RelativeLayout pnlSize, pnlDescriptionHeader, pnlDescription;
    RecyclerView rvRelatedItems;

    Dialog dlgLoading = null;

    JSONObject objMain;

    PagerAdapter adapterImages;

    ArrayList<String> arrImages, arrItems, arrSizes;

    RecyclerView.Adapter adapterItems;

    ArrayAdapter<String> adapterSizes;

    GridLayoutManager layoutManager;

    DisplayMetrics displaymetrics;

    String variationProductId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_item_details);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.item_details));
        //
        act = this;
        vPagerImages = findViewById(R.id.vPagerImages);
        imgOverlay = findViewById(R.id.imgOverlay);
        imgPlus = findViewById(R.id.imgPlus);
        tabDots = findViewById(R.id.tabDots);
        lblName = findViewById(R.id.lblName);
        lblPrice = findViewById(R.id.lblPrice);
        lblDiscount = findViewById(R.id.lblDiscount);
        lblDescription = findViewById(R.id.lblDescription);
        lblNew = findViewById(R.id.lblNew);
        lblSale = findViewById(R.id.lblSale);
        txtSize = findViewById(R.id.txtSize);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        rvRelatedItems = findViewById(R.id.rvRelatedItems);
        pnlSize = findViewById(R.id.pnlSize);
        pnlRelated = findViewById(R.id.pnlRelated);
        pnlDescriptionHeader = findViewById(R.id.pnlDescriptionHeader);
        pnlDescription = findViewById(R.id.pnlDescription);
        //
        lblDiscount.setPaintFlags(lblDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        //
        layoutManager = new GridLayoutManager(act, 2);
        //
        displaymetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //
        float screenWidth = displaymetrics.widthPixels;
        //
        ViewGroup.LayoutParams params1 = vPagerImages.getLayoutParams();
        ViewGroup.LayoutParams params2 = imgOverlay.getLayoutParams();
        //
        params1.height = (int)screenWidth;
        params2.height = (int)screenWidth;
        //
        vPagerImages.setLayoutParams(params1);
        imgOverlay.setLayoutParams(params2);
        //
        if (getIntent().hasExtra("obj")) {
            //
            Bundle b = getIntent().getExtras();
            //
            String s = b.getString("obj");
            //
            try {

                objMain = new JSONObject(s);

                ab.setTitle(objMain.getString("name"));

            } catch (JSONException e) {
                //
            }
            //
            setInitialData();
            loadData();
        }
        //
        pnlDescriptionHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (pnlDescription.getVisibility() == View.VISIBLE) {

                    pnlDescription.setVisibility(View.GONE);

                    imgPlus.animate().rotation(0f);

                } else {

                    imgPlus.animate().rotation(45f);

                    pnlDescription.setVisibility(View.VISIBLE);
                }

            }
        });
        //
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addToCart();
            }
        });
        //
        txtSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(act);

                builder.setTitle(R.string.size);

                builder.setAdapter(adapterSizes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            JSONObject obj = new JSONObject(arrSizes.get(which));

                            variationProductId = obj.getString("product_id");

                            txtSize.setText(obj.getString("size_name"));

                            dialog.dismiss();

                        } catch (JSONException e) {

                            Log.d("JSONException", e.getMessage());
                        }

                    }
                });

                builder.show();

            }
        });
    }

    private void setInitialData() {

        try {

            lblName.setText(objMain.getString("name"));

            lblPrice.setText(objMain.getString("price"));

            ImageLoader.getInstance().loadImage(objMain.getString("pic"), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    Bitmap blurredBitmap = BlurBuilder.blur(act, loadedImage);

                    imgOverlay.setImageBitmap(blurredBitmap);

                }
            });

        } catch (JSONException e) {
            //
        }

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            try {
                params.put("productId", objMain.getString("product_id"));
            } catch (JSONException e) {
                //
            }

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getItemDetails", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            lblPrice.setText(obj.getString("price"));

                            lblDiscount.setText(obj.getString("old_price"));

                            lblSale.setVisibility(View.GONE);
                            lblNew.setVisibility(View.GONE);

                            if (!lblDiscount.getText().toString().equalsIgnoreCase("")) {

                                lblSale.setVisibility(View.VISIBLE);

                            } else if (obj.getBoolean("is_new")) {

                                lblNew.setVisibility(View.VISIBLE);
                            }

                            arr = obj.getJSONArray("sizes");

                            arrSizes = new ArrayList<String>();

                            if (arr.length() > 0) {

                                pnlSize.setVisibility(View.VISIBLE);

                                JSONObject objSize = arr.getJSONObject(0);

                                txtSize.setText(objSize.getString("size_name"));

                                for (int i=0; i<arr.length(); i++) {

                                    arrSizes.add(arr.getJSONObject(i).toString());

                                }
                            }

                            adapterSizes = new SizesAdapter(act, arrSizes);

                            lblDescription.setText(obj.getString("description"));

                            arr = obj.getJSONArray("pics");

                            arrImages = new ArrayList<String>();

                            String lang = GlobalFunctions.getLang(act);

                            if (lang.equalsIgnoreCase("ar")) {

                                for (int i = arr.length() - 1; i >= 0; i--) {

                                    arrImages.add(arr.getJSONObject(i).getString("pic"));

                                }

                            } else {

                                for (int i = 0; i < arr.length(); i++) {

                                    arrImages.add(arr.getJSONObject(i).getString("pic"));

                                }

                            }

                            adapterImages = new ImagesAdapter(act, arrImages);

                            vPagerImages.setAdapter(adapterImages);

                            tabDots.setupWithViewPager(vPagerImages, true);

                            if (lang.equalsIgnoreCase("ar")) {

                                vPagerImages.setCurrentItem(arrImages.size()-1);
                            }

                            if (!obj.getBoolean("in_stock")) {

                                btnAddToCart.setEnabled(false);

                                btnAddToCart.setText(getString(R.string.sold_out));

                            }

                            arr = obj.getJSONArray("related");

                            if (arr.length() > 0) {

                                arrItems = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrItems.add(arr.getJSONObject(i).toString());

                                }

                                adapterItems = new ItemsAdapter(act, arrItems, displaymetrics.widthPixels);

                                rvRelatedItems.setLayoutManager(layoutManager);
                                rvRelatedItems.setAdapter(adapterItems);

                            } else {

                                pnlRelated.setVisibility(View.GONE);

                            }

                        } else {

                            btnAddToCart.setEnabled(false);

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }

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

    private void addToCart() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            if (!variationProductId.equalsIgnoreCase("")) {

                params.put("productId", variationProductId);

            } else {

                try {
                    params.put("productId", objMain.getString("product_id"));
                } catch (JSONException e) {
                    //
                }

            }

            params.put("userId", GlobalFunctions.getPrefrences(act, "user_id"));
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("qty", "1");
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "addToCart", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            if (!obj.getString("cart_id").equalsIgnoreCase("0")) {

                                GlobalFunctions.addToCart(obj.getString("cart_id"));
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(act);

                            builder.setMessage(obj.getString("msg"));

                            builder.setPositiveButton(getString(R.string.view_cart).toUpperCase(), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    startActivity(new Intent(act, CartActivity.class));

                                }
                            });

                            builder.setNegativeButton(getString(R.string.continue_shopping).toUpperCase(), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    finish();
                                }
                            });

                            AlertDialog dialog = builder.create();

                            dialog.show();

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }

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

    public void hideOverlay() {

        imgOverlay.setVisibility(View.GONE);

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

    // Setting up back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                act.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
