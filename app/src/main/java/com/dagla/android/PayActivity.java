package com.dagla.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PayActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_pay);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.pay));
        //
        act = this;
        wv = (WebView)findViewById(R.id.wv);
        //
        GlobalFunctions.setWebViewParams(wv);
        //
        if (getIntent().hasExtra("url")) {

            Bundle b = getIntent().getExtras();

            ab.setTitle(b.getString("title"));

            wv.loadUrl(b.getString("url"));

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

    @Override
    public void onBackPressed() {

        String url = wv.getUrl();

        if (url.contains("order_receipt") || url.contains("error_payment")) {

            if (url.contains("order_receipt")) {

                GlobalFunctions.clearCart();
            }

            Intent intent = new Intent(act, MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);

        } else {

            super.onBackPressed();
        }

    }

    // Setting up back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                String url = wv.getUrl();

                if (url.contains("order_receipt") || url.contains("error_payment")) {

                    if (url.contains("order_receipt")) {

                        GlobalFunctions.clearCart();
                    }

                    Intent intent = new Intent(act, MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    startActivity(intent);

                } else {

                    act.finish();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
