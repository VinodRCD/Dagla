package com.dagla.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CheckoutPaymentActivity extends AppCompatActivity {

    Activity act;
    TextView lblAddress;
    ListView lvOrderSummary, lvPaymentSummary;
    EditText txtCoupon;
    Button btnRedeem, btnSubmitOrder;
    ImageView imgKNET, imgCreditCard, imgCash;
    RelativeLayout pnlKNET, pnlCreditCard, pnlCash;

    Dialog dlgLoading = null;

    ArrayList<String> arrList1, arrList2;

    ArrayAdapter<String> arrAdapter1, arrAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_checkout_payment);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.payment));
        //
        act = this;
        lblAddress = findViewById(R.id.lblAddress);
        lvOrderSummary = findViewById(R.id.lvOrderSummary);
        lvPaymentSummary = findViewById(R.id.lvPaymentSummary);
        txtCoupon = findViewById(R.id.txtCoupon);
        btnRedeem = findViewById(R.id.btnRedeem);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);
        imgKNET = findViewById(R.id.imgKNET);
        imgCreditCard = findViewById(R.id.imgCreditCard);
        imgCash = findViewById(R.id.imgCash);
        pnlKNET = findViewById(R.id.pnlKNET);
        pnlCreditCard = findViewById(R.id.pnlCreditCard);
        pnlCash = findViewById(R.id.pnlCash);
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

    private void loadData(String couponCat) {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(act, "user_id"));
            params.put("addressId", ((MyApplication)getApplication()).getDeliveryAddressId());
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("couponCode", txtCoupon.getText().toString().trim());
            params.put("couponCat", couponCat);
            params.put("ran", GlobalFunctions.getRandom());

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

                                GlobalFunctions.showToastError(act, getString(R.string.invalid_coupon_code));

                            } else {

                                lblAddress.setText(obj.getString("address"));

                                arr = obj.getJSONArray("cart");

                                arrList1 = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrList1.add(arr.getJSONObject(i).toString());
                                }

                                arrAdapter1 = new OrderSummaryAdapter(act, arrList1);

                                lvOrderSummary.setAdapter(arrAdapter1);

                                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams)lvOrderSummary.getLayoutParams();

                                params1.height = arr.length() * GlobalFunctions.convertDpToPx(act, 100);

                                lvOrderSummary.setLayoutParams(params1);

                                arr = obj.getJSONArray("payment");

                                arrList2 = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrList2.add(arr.getJSONObject(i).toString());
                                }

                                arrAdapter2 = new PaymentSummaryAdapter(act, arrList2);

                                lvPaymentSummary.setAdapter(arrAdapter2);

                                LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)lvPaymentSummary.getLayoutParams();

                                params2.height = arr.length() * GlobalFunctions.convertDpToPx(act, 40);

                                lvPaymentSummary.setLayoutParams(params2);

                                if (obj.getString("valid_coupon").equalsIgnoreCase("1")) {

                                    btnRedeem.setText(getString(R.string.remove));

                                    txtCoupon.setTag("1");
                                    txtCoupon.setEnabled(false);

                                    GlobalFunctions.showToastSuccess(act, getString(R.string.coupon_redeemed_successfully));

                                }

                            }

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                }
            });

        } else {

            GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));

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

            GlobalFunctions.showToastError(act, getString(R.string.req_payment_method));
            return;
        }

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(act, "user_id"));
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("addressId", ((MyApplication)getApplication()).getDeliveryAddressId());
            params.put("paymentMethod", paymentMethod);
            params.put("couponCode", txtCoupon.getText().toString().trim());
            params.put("ran", GlobalFunctions.getRandom());

            client.post(GlobalFunctions.serviceURL + "submitOrder", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            Intent intent = new Intent(act, PayActivity.class);

                            intent.putExtra("url", obj.getString("url"));

                            if (imgCash.getTag().toString().equalsIgnoreCase("1")) {
                                intent.putExtra("title", getString(R.string.order_receipt));
                            } else {
                                intent.putExtra("title", getString(R.string.pay));
                            }

                            startActivity(intent);

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                }
            });

        } else {

            GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));

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
                txtCoupon.setError(getString(R.string.req_coupon_code));
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
