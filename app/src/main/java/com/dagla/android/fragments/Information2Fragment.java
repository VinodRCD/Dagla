package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;

import java.util.Locale;

public class Information2Fragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    WebView wv;

    Dialog dlgLoading = null;

    String page_Id,title;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard


        if (rootView == null) {

//            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_information_2, container, false);
//            }else {
//                rootView = inflater.inflate(R.layout.fragment_information, container, false);
//            }


            wv = (WebView)rootView.findViewById(R.id.wv);
            //
            GlobalFunctions.setWebViewParams(wv);
            //
            if (getArguments().getString("page_id")!= null) {

//                Bundle b = getIntent().getExtras();
                ((MainActivity) getActivity()).setHeaders(getArguments().getString("title"),false,false,false,true, false ,"0", false);

                wv.loadUrl(String.format(Locale.US, "%sstatic_page.aspx?id=%s&lang=%s",
                        GlobalFunctions.baseURL,
                        getArguments().getString("page_id"),
                        GlobalFunctions.getLang(getActivity())));
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
