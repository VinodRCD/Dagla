package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class HomeVisitServiceFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;
    EditText txtName,txtMobile,txtEmail,txtAddress;
    ImageView imgKNET, imgCreditCard,imgMyWallet;
    TextView titleTxt,descriptionTxt;

    Button btnNext;

    Dialog dlgLoading = null;

    String slot_date,slot_time_id;

    String v_title,v_desc,v_id;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        context = getActivity();

//        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.home_visit_service_ar),true,true,true,true, false ,"0");
//        }else {
//            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.home_visit_service),true,true,true,true, false ,"0");
//        }

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.home_visit_service_ar),true,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.home_visit_service),true,false,false,true, false ,"0", false);
        }

        GlobalFunctions.setLanguage(getActivity());
        GlobalFunctions.initImageLoader(context);


//        if (getArguments().getString("VisitDate")!= null) {
//
//            slot_date = getArguments().getString("VisitDate");
//
//        }
//        if (getArguments().getString("VisitTimeId")!= null) {
//
//            slot_time_id = getArguments().getString("VisitTimeId");
//
//        }


        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_home_visit_service_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_home_visit_service, container, false);

            }



            txtName = rootView.findViewById(R.id.txtName);
            txtMobile = rootView.findViewById(R.id.txtMobile);
            txtEmail = rootView.findViewById(R.id.txtEmail);
            txtAddress = rootView.findViewById(R.id.txtAddress);

//            if(!GlobalFunctions.getPrefrences(getActivity(), "user_id").equals("")){
//                txtName.setText(GlobalFunctions.getPrefrences(getActivity(), "name"));
//                txtEmail.setText(GlobalFunctions.getPrefrences(getActivity(), "email"));
//            }
            titleTxt = rootView.findViewById(R.id.titleTxt);
            descriptionTxt = rootView.findViewById(R.id.descriptionTxt);

            imgKNET = rootView.findViewById(R.id.imgKnet);
            imgCreditCard = rootView.findViewById(R.id.imgCreditCard);
            imgMyWallet = rootView.findViewById(R.id.imgMyWallet);

            btnNext = rootView.findViewById(R.id.btnNext);

            clearPaymentMethod();

            imgKNET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearPaymentMethod();

                    imgKNET.setTag("1");
                    imgKNET.setImageResource(R.drawable.radio_btn_2);
                    imgCreditCard.setImageResource(R.drawable.radio_btn_1);
                    imgMyWallet.setImageResource(R.drawable.radio_btn_1);

                }
            });
            //
            imgCreditCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearPaymentMethod();


                    imgCreditCard.setTag("1");

                    imgKNET.setImageResource(R.drawable.radio_btn_1);
                    imgCreditCard.setImageResource(R.drawable.radio_btn_2);
                    imgMyWallet.setImageResource(R.drawable.radio_btn_1);

                }
            });

            imgMyWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearPaymentMethod();


                    imgMyWallet.setTag("1");

                    imgKNET.setImageResource(R.drawable.radio_btn_1);
                    imgCreditCard.setImageResource(R.drawable.radio_btn_1);
                    imgMyWallet.setImageResource(R.drawable.radio_btn_2);

                }
            });

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitOrder();
                }
            });

            visitUsContentData();

        }





        return rootView;

    }

    private void visitUsContentData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getRequestVisitContent" , params, new AsyncHttpResponseHandler() {

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

                            String title_en = obj.getString("RQDishTitleEN");
                            String title_ar = obj.getString("RQDishTitleAR");
                            String ex_desc_en = obj.getString("RQDishExDescEN");
                            String ex_desc_ar = obj.getString("RQDishExDescAR");
                            String new_desc_en = obj.getString("RQDishNewDescEN");
                            String new_desc_ar = obj.getString("RQDishNewDescAR");
                            String ex_thanks_en = obj.getString("RQDishExThanksEN");
                            String ex_thanks_ar = obj.getString("RQDishExThanksAR");
                            String new_thanks_en = obj.getString("RQDishNewThanksEN");
                            String new_thanks_ar = obj.getString("RQDishNewThanksAR");


                            v_title = obj.getString("vtitle");
                            v_desc = obj.getString("vdesc");
                            v_id = obj.getString("vtid");



                            titleTxt.setText(v_title);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                descriptionTxt.setText(Html.fromHtml(v_desc, Html.FROM_HTML_MODE_COMPACT));
                            } else {
                                descriptionTxt.setText(Html.fromHtml(v_desc));
                            }



                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }

        }

    }


    private void submitOrder() {


        if (GlobalFunctions.isEmptyText(txtName)) {
            txtName.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtName.setError(getString(R.string.req_name_ar));
            }else {
                txtName.setError(getString(R.string.req_name));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtMobile)) {
            txtMobile.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtMobile.setError(getString(R.string.req_mobile_number_ar));
            }else {
                txtMobile.setError(getString(R.string.req_mobile_number));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtMobile)) {
            txtMobile.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtMobile.setError(getString(R.string.mobile_cannot_contain_spaces_ar));
            }else {
                txtMobile.setError(getString(R.string.mobile_cannot_contain_spaces));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtEmail.setError(getString(R.string.req_email_address_ar));
            }else {
                txtEmail.setError(getString(R.string.req_email_address));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces_ar));
            }else {
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces));
            }

            return;
        }


        if (GlobalFunctions.isEmptyText(txtAddress)) {
            txtAddress.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtAddress.setError(getString(R.string.req_address_ar));
            }else {
                txtAddress.setError(getString(R.string.req_address));
            }

            return;
        }

        String paymentMethod = "";

        if (imgKNET.getTag().toString().equalsIgnoreCase("1")) {

            paymentMethod = "knet";

//        } else if (imgKNET.getTag().toString().equalsIgnoreCase("1")) {
//
//            paymentMethod = "knet";

        } else if (imgCreditCard.getTag().toString().equalsIgnoreCase("1")) {

            paymentMethod = "credit_card";

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.req_payment_method_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.req_payment_method));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();


//            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
//            params.put("visitDate", slot_date);
//            params.put("visitSlotId", slot_time_id);
            params.put("vname", txtName.getText().toString().trim());
            params.put("vmobileNo", txtMobile.getText().toString().trim());
            params.put("vemailAddress", txtEmail.getText().toString().trim());
            params.put("vaddress", txtAddress.getText().toString().trim());
            params.put("paymentMethod", paymentMethod);
            params.put("ran", GlobalFunctions.getRandom());
            http://portal.dagla.com/services/ajax_v1.aspx?app=ios&lang=ar&ver=1.0&cat=submitOrder&userId=2ucHke6VLD8=&cartIds=8Pr2Mo7RzYQ=
            // &addressId=If0aYb9Nf3I=&paymentMethod=cash&couponCode=&ran=2



            client.get(GlobalFunctions.serviceURL + "submitVisitRequest", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {


                            HomeVisitServiceTermsFragment fragment = new HomeVisitServiceTermsFragment();
                            Bundle b = new Bundle();
                            b.putString("url", obj.getString("url"));
                            b.putString("page_id", v_id);
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

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                    }

                }
            });

        } else {

            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));
            }

        }

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

    private void clearPaymentMethod() {

//        imgCash.setTag("0");
        imgKNET.setTag("0");
        imgCreditCard.setTag("0");
        imgMyWallet.setTag("0");
    }
}
