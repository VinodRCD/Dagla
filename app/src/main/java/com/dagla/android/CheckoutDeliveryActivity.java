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
import android.widget.AdapterView;
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

public class CheckoutDeliveryActivity extends AppCompatActivity {

    Activity act;
    ListView lv;
    TextView lblTotal;
    Button btnAddAddress;

    Dialog dlgLoading = null;

    ArrayList<String> arrList;

    ArrayAdapter<String> arrAdapter;

    static final int ADD_ADDRESS_REQUEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_checkout_delivery);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.delivery_address));
        //
        act = this;
        lv = findViewById(R.id.lv);
        lblTotal = findViewById(R.id.lblTotal);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        //
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                arg1.setEnabled(false);

                try {

                    JSONObject obj = new JSONObject(lv.getItemAtPosition(position).toString());

                    ((MyApplication)getApplication()).setDeliveryAddressId(obj.getString("address_id"));

                    startActivity(new Intent(act, CheckoutPaymentActivity.class));

                } catch (JSONException e) {

                    Log.d("JSONException", e.getMessage());
                }

                arg1.setEnabled(true);

            }
        });
        //
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(act, AddAddressActivity.class), ADD_ADDRESS_REQUEST);

            }
        });
        //
        loadData();
    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(act, "user_id"));
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getCheckoutAddresses", params, new AsyncHttpResponseHandler() {

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
                            arrAdapter = new AddressesAdapter(act, arrList);
                            //
                            lv.setAdapter(arrAdapter);
                            //
                            if (arr.length() == 0) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(act);

                                builder.setMessage(R.string.your_address_book_is_empty);

                                builder.setPositiveButton(getString(R.string.add_new_address).toUpperCase(), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        startActivityForResult(new Intent(act, AddAddressActivity.class), ADD_ADDRESS_REQUEST);

                                    }
                                });

                                builder.setCancelable(false);

                                AlertDialog dialog = builder.create();

                                dialog.show();

                            } else if (arr.length() == 1) {

                                lblTotal.setText(String.format(Locale.US, "1 %s", getString(R.string.address)));

                            } else {

                                lblTotal.setText(String.format(Locale.US, "%d %s", arr.length(), getString(R.string.address)));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_ADDRESS_REQUEST) {

            if (resultCode == RESULT_OK) {

                if (data.hasExtra("address_id")) {

                    ((MyApplication)getApplication()).setDeliveryAddressId(data.getStringExtra("address_id"));

                    startActivity(new Intent(act, CheckoutPaymentActivity.class));

                }
            }
        }
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
