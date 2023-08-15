package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.adapter.IntroductionImagesAdapter;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class IntroductionActivity extends AppCompatActivity {

    Activity act;

    ViewPager vPagerImages;
    TabLayout tabDots;
    ImageView imgOverlay, imgPlus;
//    Button btnSkipIntro;
    DotsIndicator dots_indicator;

    PagerAdapter adapterImages;

    Dialog dlgLoading = null;

    ArrayList arrIntroId,arrImages,arrIntroTextEn,arrIntroTextAr,arrDescriptionEn,arrDescriptionAr,arrBtnTextEn,arrBtnTextAr;

    DisplayMetrics displaymetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);

        if(GlobalFunctions.getLang(IntroductionActivity.this).equals("ar")){
            setContentView(R.layout.introduction_activity_ar);
        }else {
            setContentView(R.layout.introduction_activity);
        }

//        //
//        ActionBar ab = getSupportActionBar();
//        //
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(false);
//        //
//        ab.setTitle(getString(R.string.login));

        act = this;

        vPagerImages = findViewById(R.id.vPagerImages);
        tabDots = findViewById(R.id.tabDots);
        imgOverlay = findViewById(R.id.imgOverlay);
        dots_indicator = findViewById(R.id.dots_indicator);



//        btnSkipIntro.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (!GlobalFunctions.getPrefrences(IntroductionActivity.this, "user_id").equalsIgnoreCase("")) {
////                    startActivity(new Intent(act, MainActivity.class));
////                    finish();
////                }else {
//                    startActivity(new Intent(act, StartingActivity.class));
//                    finish();
////                }
//
//                GlobalFunctions.setPrefrences(IntroductionActivity.this, "intro", "1");
//
//            }
//        });

        arrIntroId = new ArrayList<String>();
        arrImages = new ArrayList<Integer>();
        arrIntroTextEn = new ArrayList<String>();
        arrIntroTextAr = new ArrayList<String>();
        arrDescriptionEn = new ArrayList<String>();
        arrDescriptionAr = new ArrayList<String>();
        arrBtnTextEn = new ArrayList<String>();
        arrBtnTextAr = new ArrayList<String>();

//        arrImages.add(R.drawable.skip_screen_1);
//        arrImages.add(R.drawable.skip_screen_2);
//        arrImages.add(R.drawable.skip_screen_3);
//
//        arrIntroTextEn.add("What's New");
//        arrIntroTextEn.add("Express Delivery");
//        arrIntroTextEn.add("Customer Care");
//
//        arrDescriptionEn.add("View and shop the peices you love as soon as they arrive on site every Monday, Wednesday and Friday");
//        arrDescriptionEn.add("We offer express delivery worldwide, plus same=day delivery to India, Kuwait, Dubai, Saudi, Bahrain, London, America and Canada");
//        arrDescriptionEn.add("Our Customer Care team is available 27/7/365 days and speaks more then 14 languages.");

        GlobalFunctions.initImageLoader(act);

//        displaymetrics = new DisplayMetrics();
//        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//        //
//        float screenWidth = displaymetrics.widthPixels;
//
//        ViewGroup.LayoutParams params1 = vPagerImages.getLayoutParams();
//        ViewGroup.LayoutParams params2 = imgOverlay.getLayoutParams();
//        //
//        params1.height = (int)screenWidth;
//        params2.height = (int)screenWidth;
//        //
//        vPagerImages.setLayoutParams(params1);
//        imgOverlay.setLayoutParams(params2);
//
//        vPagerImages.setLayoutParams(params1);

//        adapterImages = new IntroductionImagesAdapter(act, arrImages, arrIntroTextEn, arrDescriptionEn);
//
//        vPagerImages.setAdapter(adapterImages);
//
//        tabDots.setupWithViewPager(vPagerImages, true);


        loadData();

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());
            params.put("lang", GlobalFunctions.getLang(act));
            client.get(GlobalFunctions.serviceURL + "getIntroData" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("intros");

//                            arrImages = new ArrayList<String>();
//
//                            for (int i = 0; i < arr.length(); i++) {
//
//                                arrIntroId.add(arr.getJSONObject(i).get("intro_id"));
//                                arrImages.add(arr.getJSONObject(i).get("image_750"));
//                                arrIntroTextEn.add(arr.getJSONObject(i).get("intro_text"));
//                                arrIntroTextAr.add(arr.getJSONObject(i).get("intro_text_ar"));
//                                arrDescriptionEn.add(arr.getJSONObject(i).get("description"));
//                                arrDescriptionAr.add(arr.getJSONObject(i).get("description_ar"));
//                                arrBtnTextEn.add(arr.getJSONObject(i).get("btn_text"));
//                                arrBtnTextAr.add(arr.getJSONObject(i).get("btn_text_ar"));
//                            }

                            if (GlobalFunctions.getLang(act).equals("ar")) {

                                for (int i = arr.length() - 1; i >= 0; i--) {

                                    arrIntroId.add(arr.getJSONObject(i).get("intro_id"));
                                    arrImages.add(arr.getJSONObject(i).get("image_750"));
                                    arrIntroTextEn.add(arr.getJSONObject(i).get("intro_text"));
                                    arrIntroTextAr.add(arr.getJSONObject(i).get("intro_text_ar"));
                                    arrDescriptionEn.add(arr.getJSONObject(i).get("description"));
                                    arrDescriptionAr.add(arr.getJSONObject(i).get("description_ar"));
                                    arrBtnTextEn.add(arr.getJSONObject(i).get("btn_text"));
                                    arrBtnTextAr.add(arr.getJSONObject(i).get("btn_text_ar"));

                                }

                            } else {

                                for (int i = 0; i < arr.length(); i++) {

                                    arrIntroId.add(arr.getJSONObject(i).get("intro_id"));
                                    arrImages.add(arr.getJSONObject(i).get("image_750"));
                                    arrIntroTextEn.add(arr.getJSONObject(i).get("intro_text"));
                                    arrIntroTextAr.add(arr.getJSONObject(i).get("intro_text_ar"));
                                    arrDescriptionEn.add(arr.getJSONObject(i).get("description"));
                                    arrDescriptionAr.add(arr.getJSONObject(i).get("description_ar"));
                                    arrBtnTextEn.add(arr.getJSONObject(i).get("btn_text"));
                                    arrBtnTextAr.add(arr.getJSONObject(i).get("btn_text_ar"));

                                }

                            }

                            if (GlobalFunctions.getLang(act).equals("ar")) {
                                adapterImages = new IntroductionImagesAdapter(act, arrImages, arrIntroTextAr, arrDescriptionAr);
//                                btnSkipIntro.setText(arrBtnTextAr.get(0).toString());
                            }else {
                                adapterImages = new IntroductionImagesAdapter(act, arrImages, arrIntroTextEn, arrDescriptionEn);
//                                btnSkipIntro.setText(arrBtnTextEn.get(0).toString());
                            }

                            vPagerImages.setAdapter(adapterImages);
                            tabDots.setupWithViewPager(vPagerImages, true);
//                            dots_indicator.setupWithViewPager(vPagerImages, true);
                            dots_indicator.setViewPager(vPagerImages);
//                            if (GlobalFunctions.getLang(act).equalsIgnoreCase("ar")) {
//                                vPagerImages.setCurrentItem(arrImages.size()-1);
//                            }

//                            if (GlobalFunctions.getLang(act).equals("ar")) {
//
//                                vPagerImages.setCurrentItem(arrImages.size()-1);
//                            }

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(act).equals("ar")) {
                            GlobalFunctions.showToastError(act, getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(act, getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(act).equals("ar")) {
                        GlobalFunctions.showToastError(act, getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(act).equals("ar")) {
                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));
            }

        }

    }

    public void showLoading() {

        if (dlgLoading == null) {

            dlgLoading = GlobalFunctions.showLoading(act);

        } else {

            dlgLoading.show();
        }

    }

    public void hideLoading() {

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

    public void hideOverlay() {

        imgOverlay.setVisibility(View.GONE);
    }
}
