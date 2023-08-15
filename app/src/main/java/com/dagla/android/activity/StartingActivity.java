package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

/**
 * Created by sys on 22-Jan-19.
 */

public class StartingActivity extends AppCompatActivity implements View.OnClickListener{

    Activity act;

    TextView lblGuest,lblStartTitle,lblDesc;

    Button btnSignIn,btnRegister;

    ImageView signInImg;

    Dialog dlgLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
        //
        GlobalFunctions.setLanguage(this);

        if(GlobalFunctions.getLang(StartingActivity.this).equals("ar")){
            setContentView(R.layout.starting_activity_ar);
        }else {
            setContentView(R.layout.starting_activity);
        }


        act = this;

        idMappings();
        setOnClickListeners();

        loadData();

    }


    private void idMappings() {

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        lblGuest = (TextView) findViewById(R.id.lblGuest);
        lblStartTitle = (TextView) findViewById(R.id.lblStartTitle);
        lblDesc = (TextView) findViewById(R.id.lblDesc);

        signInImg = (ImageView) findViewById(R.id.signInImg);
    }

    private void setOnClickListeners() {


        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        lblGuest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSignIn:

                act.finish();
                startActivity(new Intent(act, LoginActivity.class));

                break;

            case R.id.btnRegister:

                act.finish();
                startActivity(new Intent(act, RegisterActivity.class));

                break;


            case R.id.lblGuest:

                act.finish();
//                startActivity(new Intent(act, MainActivity.class));

                startActivity(new Intent(act, LandingActivity.class));

                break;

        }
    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());
            params.put("lang", GlobalFunctions.getLang(act));
            client.get(GlobalFunctions.serviceURL + "getLoginBannerData" , params, new AsyncHttpResponseHandler() {

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



                            String image = obj.getString("image_750");

//                            Picasso.with(StartingActivity.this)
//                                    .load(image)
//                                    .into(signInImg);
                            DisplayImageOptions options = new DisplayImageOptions.Builder()
                                    .showImageOnLoading(R.drawable.bg_img)
                                    .cacheInMemory(true)
                                    .cacheOnDisk(true)
                                    .build();

                            GlobalFunctions.initImageLoader(act);

                            ImageLoader.getInstance().displayImage(String.valueOf(image), signInImg, options, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    showLoading();
                                }
                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                }
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                    hideLoading();

                                }
                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {

                                }
                            }, new ImageLoadingProgressListener() {
                                @Override
                                public void onProgressUpdate(String imageUri, View view, int current, int total) {

                                }
                            });

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
}
