package com.dagla.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class CartActivity extends AppCompatActivity {

    Activity act;
    ListView lv;
    TextView lblTotal;
    Button btnCheckout;

    Dialog dlgLoading = null;

    ArrayList<String> arrList;

    ArrayAdapter<String> arrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_cart);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.cart));
        //
        act = this;
        lv = findViewById(R.id.lv);
        lblTotal = findViewById(R.id.lblTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        //
        if (GlobalFunctions.getCart().equalsIgnoreCase("")) {
            //
            cartIsEmpty();
            //
        } else {
            //
            loadData();
        }
        //
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!GlobalFunctions.getPrefrences(act, "user_id").equalsIgnoreCase("")) {

                    startActivity(new Intent(act, CheckoutDeliveryActivity.class));

                } else {

                    Intent intent = new Intent(act, LoginActivity.class);

                    intent.putExtra("returnActivity", "CheckoutDeliveryActivity");

                    startActivity(intent);

                }
            }
        });
        //
    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("cartIds", GlobalFunctions.getCart());
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getCart", params, new AsyncHttpResponseHandler() {

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
                            arr = obj.getJSONArray("data");
                            //
                            arrList = new ArrayList<String>();
                            //
                            for (int i = 0; i < arr.length(); i++) {
                                //
                                arrList.add(arr.getJSONObject(i).toString());
                            }
                            //
                            arrAdapter = new CartAdapter(act, arrList);
                            //
                            lv.setAdapter(arrAdapter);
                            //
                            lblTotal.setText(obj.getString("total"));
                            //
                            if (arrList.size() == 0) {
                                //
                                GlobalFunctions.clearCart();
                                //
                                cartIsEmpty();
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

    public void quantityMinus(int pos) {

        boolean canUpdate = false;

        String cartId = "";

        try {

            JSONObject obj = new JSONObject(arrList.get(pos));

            cartId = obj.getString("cart_id");

            int quantity = obj.getInt("quantity");
            double price = obj.getDouble("price_per");

            if (quantity > 1) {

                quantity = quantity - 1;

                price = price * quantity;

                obj.put("quantity", quantity);
                obj.put("price", String.format(Locale.US, "%s %.3f", obj.getString("currency"), price));

                arrList.set(pos, obj.toString());

                arrAdapter.notifyDataSetChanged();

                canUpdate = true;

            }

        } catch (JSONException e) {

            //

        }

        if (canUpdate) {

            if (GlobalFunctions.hasConnection(act)) {

                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();

                params.put("cartId", cartId);
                params.put("cartIds", GlobalFunctions.getCart());
                params.put("ran", GlobalFunctions.getRandom());

                client.get(GlobalFunctions.serviceURL + "delCartItemQuantity", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                        String response = new String(bytes);

                        Log.d("onSuccess", response);

                        JSONObject obj;

                        try {

                            obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                            //
                            if (obj.getString("status").equalsIgnoreCase("1")) {

                                lblTotal.setText(obj.getString("total"));

                            } else {

                                GlobalFunctions.showToastError(act, obj.getString("msg"));

                            }

                        } catch (JSONException e) {

                            GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }
                });

            } else {

                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));

            }

        }

    }

    public void quantityPlus(int pos) {

        boolean canUpdate = false;

        String cartId = "";

        try {

            JSONObject obj = new JSONObject(arrList.get(pos));

            cartId = obj.getString("cart_id");

            int quantity = obj.getInt("quantity");
            double price = obj.getDouble("price_per");

            if (quantity < obj.getInt("max_quantity")) {

                quantity = quantity + 1;

                price = price * quantity;

                obj.put("quantity", quantity);
                obj.put("price", String.format(Locale.US, "%s %.3f", obj.getString("currency"), price));

                arrList.set(pos, obj.toString());

                arrAdapter.notifyDataSetChanged();

                canUpdate = true;

            }

        } catch (JSONException e) {

            //

        }

        if (canUpdate) {

            if (GlobalFunctions.hasConnection(act)) {

                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();

                params.put("cartId", cartId);
                params.put("cartIds", GlobalFunctions.getCart());
                params.put("ran", GlobalFunctions.getRandom());

                client.get(GlobalFunctions.serviceURL + "addCartItemQuantity", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                        String response = new String(bytes);

                        Log.d("onSuccess", response);

                        JSONObject obj;

                        try {

                            obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                            //
                            if (obj.getString("status").equalsIgnoreCase("1")) {

                                lblTotal.setText(obj.getString("total"));

                            } else {

                                GlobalFunctions.showToastError(act, obj.getString("msg"));

                            }

                        } catch (JSONException e) {

                            GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }
                });

            } else {

                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));

            }

        }

    }

    public void delCartItem(int pos) {

        String cartId = "";

        try {

            JSONObject obj = new JSONObject(arrList.get(pos));

            cartId = obj.getString("cart_id");

            GlobalFunctions.deleteFromCart(cartId);

            arrList.remove(pos);

            arrAdapter.notifyDataSetChanged();

            if (arrList.size() == 0) {

                cartIsEmpty();
            }

        } catch (JSONException e) {

            //

        }

        if (GlobalFunctions.hasConnection(act)) {

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("cartId", cartId);
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "delCartItem", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            lblTotal.setText(obj.getString("total"));

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                }
            });

        } else {

            GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));

        }

    }

    private void cartIsEmpty() {

        AlertDialog.Builder builder = new AlertDialog.Builder(act);

        builder.setMessage(R.string.your_cart_is_empty);

        builder.setPositiveButton(getString(R.string.ok).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                finish();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();

        dialog.show();

        GlobalFunctions.clearCart();

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
