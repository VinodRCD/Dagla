package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;

public class VisitUsPayFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    WebView wv;

    Dialog dlgLoading = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard




        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.order_receipt_ar),false,false,false,false, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.order_receipt),false,false,false,false, false ,"0", false);
        }

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_pay, container, false);

            wv = (WebView)rootView.findViewById(R.id.wv);
            //
            GlobalFunctions.setWebViewParams(wv);
            //
            if (getArguments().getString("url")!= null) {

//                Bundle b = getIntent().getExtras();

//                ab.setTitle(b.getString("title"));

                String url = getArguments().getString("url")+"&lang="+ GlobalFunctions.getLang(getActivity());

                Log.v("Pay****",url);

                wv.loadUrl(url);

//                wv.loadUrl(getArguments().getString("url"));

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

        return rootView;

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

//    @Override
//    public void onBackPressed() {
//
//        String url = wv.getUrl();
//
//        if (url.contains("order_receipt") || url.contains("error_payment")) {
//
//            if (url.contains("order_receipt")) {
//
//                GlobalFunctions.clearCart();
//            }
//
//            Intent intent = new Intent(act, com.dagla.android.MainActivity.class);
//
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//            startActivity(intent);
//
//        } else {
//
//            super.onBackPressed();
//        }
//
//    }
}
