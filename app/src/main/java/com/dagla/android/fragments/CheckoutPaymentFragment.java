package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class CheckoutPaymentFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    TextView lblAddress;
//    ListView lvOrderSummary,lvPaymentSummary;
    EditText txtCoupon;
    Button btnRedeem, btnSubmitOrder;
    ImageView imgKNET, imgCreditCard, imgCash;
    RelativeLayout pnlKNET, pnlCreditCard, pnlCash;

    Dialog dlgLoading = null;

    ArrayList<String> arrList1, arrList2;

//    ArrayAdapter<String> arrAdapter2;

    OrderSummaryAdapter arrAdapter1;
    PaymentSummaryAdapter arrAdapter2;

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

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_checkout_payment_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_checkout_payment, container, false);
            }



            lblAddress = rootView.findViewById(R.id.lblAddress);
            lvOrderSummary = rootView.findViewById(R.id.lvOrderSummary);
            lvPaymentSummary = rootView.findViewById(R.id.lvPaymentSummary);
            txtCoupon = rootView.findViewById(R.id.txtCoupon);
            btnRedeem = rootView.findViewById(R.id.btnRedeem);
            btnSubmitOrder = rootView.findViewById(R.id.btnSubmitOrder);
            imgKNET = rootView.findViewById(R.id.imgKNET);
            imgCreditCard = rootView.findViewById(R.id.imgCreditCard);
            imgCash = rootView.findViewById(R.id.imgCash);
            pnlKNET = rootView.findViewById(R.id.pnlKNET);
            pnlCreditCard = rootView.findViewById(R.id.pnlCreditCard);
            pnlCash = rootView.findViewById(R.id.pnlCash);

//            lvOrderSummary.setHasFixedSize(true);
            // use a linear layout manager
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.isAutoMeasureEnabled();
            lvOrderSummary.setLayoutManager(linearLayoutManager);
            lvOrderSummary.setNestedScrollingEnabled(false);

//            lvPaymentSummary.setHasFixedSize(true);
            LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
            linearLayoutManager2.isAutoMeasureEnabled();
            lvPaymentSummary.setLayoutManager(linearLayoutManager2);
            lvPaymentSummary.setNestedScrollingEnabled(false);

            TextView lblAddressTxt = (TextView) rootView.findViewById(R.id.lblAddressTxt);
            TextView lblOrderSummaryTxt = (TextView) rootView.findViewById(R.id.lblOrderSummaryTxt);
            TextView lblCouponCodeTxt = (TextView) rootView.findViewById(R.id.lblCouponCodeTxt);
            TextView lblPaymentSummaryTxt = (TextView) rootView.findViewById(R.id.lblPaymentSummaryTxt);
            TextView lblPaymentMethodTxt = (TextView) rootView.findViewById(R.id.lblPaymentMethodTxt);


//            lblAddressTxt.setTypeface(custom_fontbold);
//            lblOrderSummaryTxt.setTypeface(custom_fontbold);
//            lblCouponCodeTxt.setTypeface(custom_fontbold);
//            lblPaymentSummaryTxt.setTypeface(custom_fontbold);
//            lblPaymentMethodTxt.setTypeface(custom_fontbold);


//            txtCoupon.setTypeface(custom_fontnormal);
//            lblAddress.setTypeface(custom_fontnormal);
//
//            btnRedeem.setTypeface(custom_fontbold);
//            btnSubmitOrder.setTypeface(custom_fontbold);

            //
            txtCoupon.setTag("0");
            //
            clearPaymentMethod();
            //
            loadData("");
            //
            btnRedeem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    redeemCoupon();
                }
            });
            //
            imgKNET.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearPaymentMethod();

                    pnlKNET.setVisibility(View.VISIBLE);

                    imgKNET.setTag("1");
                }
            });
            //
            imgCreditCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearPaymentMethod();

                    pnlCreditCard.setVisibility(View.VISIBLE);

                    imgCreditCard.setTag("1");
                }
            });
            //
            imgCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clearPaymentMethod();

                    pnlCash.setVisibility(View.VISIBLE);

                    imgCash.setTag("1");
                }
            });
            //
            btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    submitOrder();
                }
            });

        }

        return rootView;

    }



    private void loadData(String couponCat) {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("addressId", ((MyApplication)getActivity().getApplication()).getDeliveryAddressId());
//            params.put("addressId", "4lyH7PoAjNc=");
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("couponCode", txtCoupon.getText().toString().trim());
            params.put("couponCat", couponCat);
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

            client.get(GlobalFunctions.serviceURL + "getCheckoutPayment", params, new AsyncHttpResponseHandler() {

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

                                lblAddress.setText(obj.getString("address"));

                                arr = obj.getJSONArray("cart");

                                arrList1 = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrList1.add(arr.getJSONObject(i).toString());
                                }

                                arrAdapter1 = new OrderSummaryAdapter(getActivity(), arrList1);

                                lvOrderSummary.setAdapter(arrAdapter1);

//                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)lvOrderSummary.getLayoutParams();

//                                params1.height = arr.length() * GlobalFunctions.convertDpToPx(getActivity(), 100);

//                                lvOrderSummary.setLayoutParams(params1);


                                arr = obj.getJSONArray("payment");

                                arrList2 = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrList2.add(arr.getJSONObject(i).toString());
                                }

                                arrAdapter2 = new PaymentSummaryAdapter(getActivity(), arrList2);

                                lvPaymentSummary.setAdapter(arrAdapter2);

//                                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)lvPaymentSummary.getLayoutParams();
//
//                                params2.height = arr.length() * GlobalFunctions.convertDpToPx(getActivity(), 40);
//
//                                lvPaymentSummary.setLayoutParams(params2);


                                if (obj.getString("valid_coupon").equalsIgnoreCase("1")) {

                                    btnRedeem.setText(getString(R.string.remove));

                                    txtCoupon.setTag("1");
                                    txtCoupon.setEnabled(false);

                                    GlobalFunctions.showToastSuccess(getActivity(), getString(R.string.coupon_redeemed_successfully));

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

        if (imgCash.getTag().toString().equalsIgnoreCase("1")) {

            paymentMethod = "cash";

        } else if (imgKNET.getTag().toString().equalsIgnoreCase("1")) {

            paymentMethod = "knet";

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

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("addressId", ((MyApplication)getActivity().getApplication()).getDeliveryAddressId());
            params.put("paymentMethod", paymentMethod);
            params.put("couponCode", txtCoupon.getText().toString().trim());
            params.put("ran", GlobalFunctions.getRandom());
            http://portal.dagla.com/services/ajax_v1.aspx?app=ios&lang=ar&ver=1.0&cat=submitOrder&userId=2ucHke6VLD8=&cartIds=8Pr2Mo7RzYQ=
            // &addressId=If0aYb9Nf3I=&paymentMethod=cash&couponCode=&ran=2



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
                            if (imgCash.getTag().toString().equalsIgnoreCase("1")) {
                                b.putString("title", getString(R.string.order_receipt));
                            }else{
                                b.putString("title", getString(R.string.pay));
                            }

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

            btnRedeem.setText(getString(R.string.redeem));

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

        pnlCash.setVisibility(View.GONE);
        pnlKNET.setVisibility(View.GONE);
        pnlCreditCard.setVisibility(View.GONE);

        imgCash.setTag("0");
        imgKNET.setTag("0");
        imgCreditCard.setTag("0");
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
}
