package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;

import java.util.Locale;

public class HomeVisitServiceTermsFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    ImageView imgIAgree, imgCreditCard,imgMyWallet;
    TextView titleTxt,descriptionTxt;

    Button btnSubmit;

    WebView wv;

    Dialog dlgLoading = null;

    String page_Id,title;

    String url;

    String agree = "0";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if (getArguments().getString("url")!= null) {

//                Bundle b = getIntent().getExtras();

//                ab.setTitle(b.getString("title"));

            url = getArguments().getString("url") + "&lang=" + GlobalFunctions.getLang(getActivity());

        }


        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_home_visit_service_terms_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_home_visit_service_terms, container, false);

            }

            wv = (WebView)rootView.findViewById(R.id.wv);

            titleTxt = rootView.findViewById(R.id.titleTxt);
            descriptionTxt = rootView.findViewById(R.id.descriptionTxt);
            descriptionTxt.setMovementMethod(new ScrollingMovementMethod());

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                descriptionTxt.setText(getString(R.string.terms_n_conditions_ar));
            }else {
                descriptionTxt.setText(getString(R.string.terms_n_conditions));
            }


            imgIAgree = rootView.findViewById(R.id.imgIAgree);
            

            btnSubmit = rootView.findViewById(R.id.btnSubmit);


            imgIAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    agree = "1";
                    imgIAgree.setTag("1");
                    imgIAgree.setImageResource(R.drawable.radio_btn_2);


                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (agree.equalsIgnoreCase("1")) {
                        if (imgIAgree.getTag().toString().equalsIgnoreCase("1")) {

                            VisitUsPayFragment fragment = new VisitUsPayFragment();
                            Bundle b = new Bundle();
                            b.putString("url", url);
//                            if (imgCash.getTag().toString().equalsIgnoreCase("1")) {
//                                b.putString("title", getString(R.string.order_receipt));
//                            }else{
                            b.putString("title", getString(R.string.pay));
//                            }

                            fragment.setArguments(b);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, fragment, "VisitUsPayFragment")
                                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .addToBackStack(null)
                                    // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                    .commitAllowingStateLoss();

                        }
                    } else {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.agree_terms_conditions_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.agree_terms_conditions));
                        }

                    }


                }
            });


            if (getArguments().getString("page_id")!= null) {

//                Bundle b = getIntent().getExtras();
                ((MainActivity) getActivity()).setHeaders(getArguments().getString("title"),false,false,false,true, false ,"0", false);


                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                    wv.loadUrl("https://portal.dagla.com/static_page.aspx?id=YPXYbjn4tVA=&lang=ar");
                }else{
                    wv.loadUrl("https://portal.dagla.com/static_page.aspx?id=YPXYbjn4tVA=&lang=en");
                }


                /*
                wv.loadUrl(String.format(Locale.US, "%sstatic_page.aspx?id=%s&lang=%s",
                        GlobalFunctions.baseURL,
                        getArguments().getString("page_id"),
                        GlobalFunctions.getLang(getActivity())));*/
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
