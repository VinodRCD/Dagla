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

public class OrderReceiptFragment extends Fragment {

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


//        Resources res = getActivity().getResources();
//        Configuration configuration = res.getConfiguration();
//        Locale newLocale = new Locale(GlobalFunctions.getLang(getActivity()));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            configuration.setLocale(newLocale);
//            LocaleList localeList = new LocaleList(newLocale);
//            LocaleList.setDefault(localeList);
//            configuration.setLocales(localeList);
//            getActivity().createConfigurationContext(configuration);
//
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLocale(newLocale);
//            getActivity().createConfigurationContext(configuration);
//
//        } else {
//            configuration.locale = newLocale;
//            res.updateConfiguration(configuration, res.getDisplayMetrics());
//        }


        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.order_receipt_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.order_receipt),false,false,false,true, false ,"0", false);
        }

        if (rootView == null) {

            rootView = inflater.inflate(R.layout.fragment_order_receipt, container, false);

            wv = (WebView)rootView.findViewById(R.id.wv);
            //
            GlobalFunctions.setWebViewParams(wv);
            //
            if (getArguments().getString("order_id")!= null) {

//                Bundle b = getIntent().getExtras();

//                wv.loadUrl(String.format(Locale.US, "%sorder_receipt.aspx?orderId=%s&lang=%s",
//                        GlobalFunctions.baseURL,
//                        getArguments().getString("order_id"),
//                        GlobalFunctions.getLang(getActivity())));


                wv.loadUrl(GlobalFunctions.baseURL+"order_receipt.aspx?orderId="+getArguments().getString("order_id")+"&lang="+GlobalFunctions.getLang(getActivity()));

            }
            //
            wv.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    super.onPageStarted(view, url, favicon);

                    Log.v("UrlOrderReceipt",url);
                    showLoading();

                }

                @Override
                public void onPageFinished(WebView view, String url) {

                    super.onPageFinished(view, url);
                    Log.v("UrlOrderReceipt*****",url);
                    hideLoading();
                }

            });

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
}
