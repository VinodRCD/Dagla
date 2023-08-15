package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;

public class WebActivity extends AppCompatActivity {

    Activity act;
    WebView wv;

    Dialog dlgLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_web);
        //
//        ActionBar ab = getSupportActionBar();
//        //
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(false);
        //
        act = this;
        wv = (WebView)findViewById(R.id.wv);
        //
        GlobalFunctions.setWebViewParams(wv);
        //
        if (getIntent().hasExtra("pageURL")) {
            //
            Bundle b = getIntent().getExtras();
            //
//            ab.setTitle(b.getString("pageTitle"));
            //

            Log.v("PAgeURL",b.getString("pageURL"));
//            wv.loadUrl(b.getString("http://portal.dagla.com/services/ajax_v1.aspx?app=ios&lang=en&ver=1.0&cat=getStaticPagesData&page_id=a4gCDLs6glA=&ran=71"));

            wv.loadUrl(b.getString("pageURL"));

        }
        //
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);

                showLoading();

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);

                hideLoading();
            }

        });
        //
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
