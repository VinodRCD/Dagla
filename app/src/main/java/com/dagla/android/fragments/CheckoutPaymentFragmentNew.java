package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.MyApplication;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.OrderSummaryAdapter;
import com.dagla.android.adapter.OrderSummaryAdapterNew;
import com.dagla.android.adapter.PaymentSummaryAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CheckoutPaymentFragmentNew extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    TextView lblAddress,lblKnetTxt,lblCreditCardTxt,lblWalletTxt;
    //    ListView lvOrderSummary,lvPaymentSummary;
    EditText txtCoupon;
    ImageView imgKNET, imgCreditCard,imgWallet;
    Button btnAddNewAddress,btnChange,btnApply,btnSecurePayment;
    TextView lblSubTotal,lblDeliveryCharge,lblTotal,lblDiscount;
    RelativeLayout addressLayout;
    LinearLayout discountLayout,knetLayout,masterVisaLayout,walletLayout;

    Dialog dlgLoading = null;

    ArrayList<String> arrList1, arrList2;

//    ArrayAdapter<String> arrAdapter2;

//    OrderSummaryAdapter arrAdapter1;
    OrderSummaryAdapterNew arrAdapter1;
//    PaymentSummaryAdapter arrAdapter2;

    RecyclerView lvOrderSummary,lvPaymentSummary;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.payment_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.payment),false,false,false,true, false ,"0", false);
        }

//        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_checkout_payment_new_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_checkout_payment_new, container, false);
            }



        addressLayout = rootView.findViewById(R.id.addressLayout);
            lblAddress = rootView.findViewById(R.id.lblAddress);
            lvOrderSummary = rootView.findViewById(R.id.lvOrderSummary);

            btnAddNewAddress = rootView.findViewById(R.id.btnAddNewAddress);
            btnChange = rootView.findViewById(R.id.btnChange);

            btnApply = rootView.findViewById(R.id.btnApply);
            btnSecurePayment = rootView.findViewById(R.id.btnSecurePayment);

            txtCoupon = rootView.findViewById(R.id.txtCoupon);
//            btnRedeem = rootView.findViewById(R.id.btnRedeem);
//            btnSubmitOrder = rootView.findViewById(R.id.btnSubmitOrder);
            imgKNET = rootView.findViewById(R.id.imgKnet);
            imgCreditCard = rootView.findViewById(R.id.imgCreditCard);
        imgWallet = rootView.findViewById(R.id.imgWallet);

        knetLayout = rootView.findViewById(R.id.knetLayout);
        masterVisaLayout = rootView.findViewById(R.id.masterVisaLayout);
        walletLayout = rootView.findViewById(R.id.walletLayout);


        lblKnetTxt = rootView.findViewById(R.id.lblKnetTxt);
        lblCreditCardTxt = rootView.findViewById(R.id.lblCreditCardTxt);
        lblWalletTxt = rootView.findViewById(R.id.lblWalletTxt);

        if(GlobalFunctions.getPrefrences(getActivity(), "CountryName").equals("Kuwait")||GlobalFunctions.getPrefrences(getActivity(), "CountryNameAr").equals("الكويت")|| GlobalFunctions.getPrefrences(getActivity(), "CountryName").equals("")){
            knetLayout.setVisibility(View.VISIBLE);
            masterVisaLayout.setVisibility(View.VISIBLE);
            walletLayout.setVisibility(View.VISIBLE);

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                lblKnetTxt.setText(getResources().getString(R.string.k_net_ar));
                lblCreditCardTxt.setText(getResources().getString(R.string.credit_card_ar));
                lblWalletTxt.setText(getResources().getString(R.string.wallet_ar));
            }else {
                lblKnetTxt.setText(getResources().getString(R.string.k_net));
                lblCreditCardTxt.setText(getResources().getString(R.string.credit_card));
                lblWalletTxt.setText(getResources().getString(R.string.wallet));
            }


        }else {
            knetLayout.setVisibility(View.GONE);
            masterVisaLayout.setVisibility(View.VISIBLE);
            walletLayout.setVisibility(View.VISIBLE);

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                lblKnetTxt.setText(getResources().getString(R.string.k_net_ar));
//                lblCreditCardTxt.setText(getResources().getString(R.string.debit_credit_card_ar));
                lblCreditCardTxt.setText(getResources().getString(R.string.credit_card_ar));
                lblWalletTxt.setText(getResources().getString(R.string.wallet_ar));
            }else {
                lblKnetTxt.setText(getResources().getString(R.string.k_net));
//                lblCreditCardTxt.setText(getResources().getString(R.string.debit_credit_card));
                lblCreditCardTxt.setText(getResources().getString(R.string.credit_card));
                lblWalletTxt.setText(getResources().getString(R.string.wallet));
            }



        }


            lblSubTotal = rootView.findViewById(R.id.lblSubTotal);
            lblDeliveryCharge = rootView.findViewById(R.id.lblDeliveryCharge);
            lblTotal = rootView.findViewById(R.id.lblTotal);
            lblDiscount = rootView.findViewById(R.id.lblDiscount);

        discountLayout = rootView.findViewById(R.id.discountLayout);

        discountLayout.setVisibility(View.GONE);

//            imgCash = rootView.findViewById(R.id.imgCash);
//            pnlKNET = rootView.findViewById(R.id.pnlKNET);
//            pnlCreditCard = rootView.findViewById(R.id.pnlCreditCard);
//            pnlCash = rootView.findViewById(R.id.pnlCash);
//
//            lvOrderSummary.setHasFixedSize(true);
            // use a linear layout manager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.isAutoMeasureEnabled();
            lvOrderSummary.setLayoutManager(linearLayoutManager);
            lvOrderSummary.setNestedScrollingEnabled(false);
//
////            lvPaymentSummary.setHasFixedSize(true);
//            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
//            linearLayoutManager2.isAutoMeasureEnabled();
//            lvPaymentSummary.setLayoutManager(linearLayoutManager2);
//            lvPaymentSummary.setNestedScrollingEnabled(false);
//
//
            txtCoupon.setTag("0");
            //
            clearPaymentMethod();
            //
            loadData("");


            btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CheckoutDeliveryFragment fragment = new CheckoutDeliveryFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, "CheckoutDeliveryFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });

            btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AddAddressFragmentFromCheckOut fragment = new AddAddressFragmentFromCheckOut();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, "CheckoutDeliveryFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });

//            //
            btnApply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    redeemCoupon();
                }
            });
//            //
            imgKNET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearPaymentMethod();

                    imgKNET.setTag("1");
                    imgKNET.setImageResource(R.drawable.radio_btn_2);
                    imgCreditCard.setImageResource(R.drawable.radio_btn_1);
                    imgWallet.setImageResource(R.drawable.radio_btn_1);
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
                    imgWallet.setImageResource(R.drawable.radio_btn_1);
                }
            });

        imgWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clearPaymentMethod();


                imgWallet.setTag("1");

                imgKNET.setImageResource(R.drawable.radio_btn_1);
                imgCreditCard.setImageResource(R.drawable.radio_btn_1);
                imgWallet.setImageResource(R.drawable.radio_btn_2);
            }
        });
//            //
//            imgCash.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    clearPaymentMethod();
//
//                    pnlCash.setVisibility(View.VISIBLE);
//
//                    imgCash.setTag("1");
//                }
//            });
//            //
            btnSecurePayment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    submitOrder();
                }
            });

//        }

        return rootView;

    }



    private void loadData(String couponCat) {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("addressId", ((MyApplication)getActivity().getApplication()).getDeliveryAddressId());
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("couponCode", txtCoupon.getText().toString().trim());
            params.put("couponCat", couponCat);
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

//            http://portal.dagla.com/services/ajax_v1.aspx?app=ios&lang=en&ver=1.0&cat=getNewCheckout&userId=ZGp0o2+iBIM=&cartIds=EAYy45EN97k=&couponCode=HELLO10&couponCat=add&addressId=SlE3k5TYB9U=&ran=71
            client.get(GlobalFunctions.serviceURL + "getNewCheckout", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {
                            //
                            if (obj.getString("valid_coupon").equalsIgnoreCase("0")) {

                                txtCoupon.setTag("0");
                                txtCoupon.setText("");
                                txtCoupon.setEnabled(true);

                                GlobalFunctions.showToastError(getActivity(), getString(R.string.invalid_coupon_code));

                            } else {

                                if(obj.getString("address").equals("")){
                                    btnAddNewAddress.setVisibility(View.VISIBLE);
                                    addressLayout.setVisibility(View.GONE);
                                }else {
                                    btnAddNewAddress.setVisibility(View.GONE);
                                    addressLayout.setVisibility(View.VISIBLE);
                                }

                                lblAddress.setText(obj.getString("address"));

                                ((MyApplication)getActivity().getApplication()).setDeliveryAddressId(obj.getString("address_id"));

                                arr = obj.getJSONArray("cart");

                                arrList1 = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrList1.add(arr.getJSONObject(i).toString());
                                }

                                arrAdapter1 = new OrderSummaryAdapterNew(getActivity(), arrList1);

                                lvOrderSummary.setAdapter(arrAdapter1);



//                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)lvOrderSummary.getLayoutParams();

//                                params1.height = arr.length() * GlobalFunctions.convertDpToPx(getActivity(), 100);

//                                lvOrderSummary.setLayoutParams(params1);


                                arr = obj.getJSONArray("payment");

                                arrList2 = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrList2.add(arr.getJSONObject(i).toString());

                                    if(i==0){
                                        JSONObject obj1 = new JSONObject(arr.getJSONObject(i).toString());
                                        lblSubTotal.setText(" "+obj1.getString("value"));
                                    }else if(i==1){
                                        JSONObject obj1 = new JSONObject(arr.getJSONObject(i).toString());
                                        lblDeliveryCharge.setText(" "+obj1.getString("value"));
                                    }else if(i==2){
                                        JSONObject obj1 = new JSONObject(arr.getJSONObject(i).toString());

                                        if(obj1.getString("name").equals("Discount")){
                                            discountLayout.setVisibility(View.VISIBLE);
                                            lblDiscount.setText(" "+obj1.getString("value"));
                                        }else {
                                            discountLayout.setVisibility(View.GONE);
                                            lblTotal.setText(" "+obj1.getString("value"));
                                        }

                                    }else if(i==3){
                                        JSONObject obj1 = new JSONObject(arr.getJSONObject(i).toString());
                                        lblTotal.setText(" "+obj1.getString("value"));
                                    }

                                }



//                                arrAdapter2 = new PaymentSummaryAdapter(getActivity(), arrList2);
//
//                                lvPaymentSummary.setAdapter(arrAdapter2);

//                                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)lvPaymentSummary.getLayoutParams();
//
//                                params2.height = arr.length() * GlobalFunctions.convertDpToPx(getActivity(), 40);
//
//                                lvPaymentSummary.setLayoutParams(params2);


                                if (obj.getString("valid_coupon").equalsIgnoreCase("1")) {

                                    btnApply.setText(getString(R.string.remove));

                                    txtCoupon.setTag("1");
                                    txtCoupon.setEnabled(false);

//                                    GlobalFunctions.showToastSuccess(getActivity(), getString(R.string.coupon_redeemed_successfully));

                                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                        redeemSuccessDialog(getString(R.string.coupon_redeemed_successfully_ar));
                                    }else {
                                        redeemSuccessDialog(getString(R.string.coupon_redeemed_successfully));
                                    }


                                }

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

        String paymentMethod = "";

        if (lblAddress.getText().toString().equals("")) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                GlobalFunctions.showToastError(getActivity(), getString(R.string.please_add_the_address_ar));
            }else {
                GlobalFunctions.showToastError(getActivity(), getString(R.string.please_add_the_address));
            }

            return;
        }else if (imgKNET.getTag().toString().equalsIgnoreCase("1")) {

            paymentMethod = "knet";

        } else if (imgCreditCard.getTag().toString().equalsIgnoreCase("1")) {

            paymentMethod = "credit_card";

        }else if (imgWallet.getTag().toString().equalsIgnoreCase("1")) {

            paymentMethod = "wallet";

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

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("addressId", ((MyApplication)getActivity().getApplication()).getDeliveryAddressId());
            params.put("paymentMethod", paymentMethod);
            params.put("couponCode", txtCoupon.getText().toString().trim());
            params.put("ran", GlobalFunctions.getRandom());
            http://portal.dagla.com/services/ajax_v1.aspx?app=ios&lang=ar&ver=1.0&cat=submitOrder&userId=2ucHke6VLD8=&cartIds=8Pr2Mo7RzYQ=
            // &addressId=If0aYb9Nf3I=&paymentMethod=cash&couponCode=&ran=2


            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

            client.get(GlobalFunctions.serviceURL + "submitOrder", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

//                            Intent intent = new Intent(getActivity(), PayActivity.class);
//
//                            intent.putExtra("url", obj.getString("url"));
//
//                            if (imgCash.getTag().toString().equalsIgnoreCase("1")) {
//                                intent.putExtra("title", getString(R.string.order_receipt));
//                            } else {
//                                intent.putExtra("title", getString(R.string.pay));
//                            }
//
//                            startActivity(intent);

//                            ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            GlobalFunctions.clearCart();


                            PayFragment fragment = new PayFragment();
                            Bundle b = new Bundle();
                            b.putString("url", obj.getString("url"));
//                            if (imgCash.getTag().toString().equalsIgnoreCase("1")) {
//                                b.putString("title", getString(R.string.order_receipt));
//                            }else{
                                b.putString("title", getString(R.string.pay));
//                            }

                            fragment.setArguments(b);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, fragment, "PayFragment")
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

    private void redeemCoupon() {

        if (txtCoupon.getTag().toString().equalsIgnoreCase("1")) {

            txtCoupon.setTag("0");
            txtCoupon.setText("");
            txtCoupon.setEnabled(true);

            btnApply.setText(getString(R.string.redeem));

            loadData("");

        } else {

            if (GlobalFunctions.isEmptyText(txtCoupon)) {
                txtCoupon.requestFocus();
                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                    txtCoupon.setError(getString(R.string.req_coupon_code_ar));
                }else {
                    txtCoupon.setError(getString(R.string.req_coupon_code));
                }

                return;
            }

            loadData("add");
        }

    }

    private void clearPaymentMethod() {

        imgKNET.setTag("0");
        imgCreditCard.setTag("0");
        imgWallet.setTag("0");
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


    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void redeemSuccessDialog(String str) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.login_success_dialog_box);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.gravity = Gravity.BOTTOM;
//        layoutParams.x = 0;   //x position
//        layoutParams.y = 30;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        final TextView alertMessage = (TextView) dialog.findViewById(R.id.alertMessage);

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);


        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            alertMessage.setText(str);
            okBtn.setText("تم");
        }else {
            alertMessage.setText(str);
            okBtn.setText("Ok");
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }
}
