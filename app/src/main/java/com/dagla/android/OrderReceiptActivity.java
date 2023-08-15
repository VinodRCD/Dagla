package com.dagla.android;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class OrderReceiptActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_order_receipt);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.order_receipt));
        //
        act = this;
        wv = (WebView)findViewById(R.id.wv);
        //
        GlobalFunctions.setWebViewParams(wv);
        //
        if (getIntent().hasExtra("order_id")) {

            Bundle b = getIntent().getExtras();

            wv.loadUrl(String.format(Locale.US, "%sorder_receipt.aspx?orderId=%s&lang=%s",
                    GlobalFunctions.baseURL,
                    b.getString("order_id"),
                    GlobalFunctions.getLang(act)));
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
