package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.adapter.IntroductionImagesAdapter;
import com.dagla.android.adapter.IntroductionImagesAdapter2;
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

public class IntroductionActivity2 extends AppCompatActivity {

    Activity act;

    ViewPager vPagerImages;
//    TabLayout tabDots;
//    ImageView imgOverlay, imgPlus;
    //    Button btnSkipIntro;
    DotsIndicator dots_indicator;
    TextView lblTitle,lblSkip;
    Button btnNext;

    PagerAdapter adapterImages;

    Dialog dlgLoading = null;

    ArrayList arrIntroId,arrImages,arrIntroTextEn,arrIntroTextAr,arrDescriptionEn,arrDescriptionAr,arrBtnTextEn,arrBtnTextAr;

    DisplayMetrics displaymetrics;

    int screen_pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);

        if(GlobalFunctions.getLang(IntroductionActivity2.this).equals("ar")){
            setContentView(R.layout.activity_introduction);
        }else {
            setContentView(R.layout.activity_introduction);
        }



        act = this;

        vPagerImages = findViewById(R.id.vPagerImages);
//        tabDots = findViewById(R.id.tabDots);
//        imgOverlay = findViewById(R.id.imgOverlay);
        dots_indicator = findViewById(R.id.dots_indicator);

        lblTitle = findViewById(R.id.lblTitle);
        lblSkip = findViewById(R.id.lblSkip);
        btnNext = findViewById(R.id.btnNext);

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



        vPagerImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                screen_pos = position;

                if(GlobalFunctions.getLang(IntroductionActivity2.this).equals("ar")){
                    lblTitle.setText(arrIntroTextAr.get(position).toString());
                    lblSkip.setText(arrBtnTextAr.get(position).toString());
                }else {
                    lblTitle.setText(arrIntroTextEn.get(position).toString());
                    lblSkip.setText(arrBtnTextEn.get(position).toString());
                }


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        lblSkip.setOnClickListener(v -> {
            startActivity(new Intent(IntroductionActivity2.this, StartingActivity.class));
            finish();

            GlobalFunctions.setPrefrences(IntroductionActivity2.this, "intro", "1");
        });


        btnNext.setOnClickListener(v -> {


            if(screen_pos!=arrIntroId.size()-1){

                screen_pos = screen_pos+1;
                vPagerImages.setCurrentItem(screen_pos);


            }else if(screen_pos == arrIntroId.size()-1){
                screen_pos = 0;
                vPagerImages.setCurrentItem(screen_pos);
            }


        });



        loadData();

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            arrIntroId.clear();
            arrImages.clear();
            arrIntroTextEn.clear();
            arrIntroTextAr.clear();
            arrDescriptionEn.clear();
            arrDescriptionAr.clear();
            arrBtnTextEn.clear();
            arrBtnTextAr.clear();

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
                                adapterImages = new IntroductionImagesAdapter2(act, arrImages, arrIntroTextAr, arrDescriptionAr);
//                                btnSkipIntro.setText(arrBtnTextAr.get(0).toString());
                            }else {
                                adapterImages = new IntroductionImagesAdapter2(act, arrImages, arrIntroTextEn, arrDescriptionEn);
//                                btnSkipIntro.setText(arrBtnTextEn.get(0).toString());
                            }

                            vPagerImages.setAdapter(adapterImages);
//                            tabDots.setupWithViewPager(vPagerImages, true);
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

//    public void hideOverlay() {
//
//        imgOverlay.setVisibility(View.GONE);
//    }
}
