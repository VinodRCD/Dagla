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
import android.widget.EditText;

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

public class AddAddressActivity extends AppCompatActivity {

    Activity act;
    EditText txtAddressName, txtArea, txtBlock, txtStreet, txtAvenue, txtHouseBuilding, txtFloor,
            txtApartment, txtExtraDetails;
    Button btnAdd;

    Dialog dlgLoading = null;

    ArrayList<String> arrList;

    ArrayAdapter<String> arrAdapter;

    String addressId = "";

    int areaId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_add_address);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.add_new_address));
        //
        act = this;
        txtAddressName = (EditText)findViewById(R.id.txtAddressName);
        txtArea = (EditText)findViewById(R.id.txtArea);
        txtBlock = (EditText)findViewById(R.id.txtBlock);
        txtStreet = (EditText)findViewById(R.id.txtStreet);
        txtAvenue = (EditText)findViewById(R.id.txtAvenue);
        txtHouseBuilding = (EditText)findViewById(R.id.txtHouseBuilding);
        txtFloor = (EditText)findViewById(R.id.txtFloor);
        txtApartment = (EditText)findViewById(R.id.txtApartment);
        txtExtraDetails = (EditText)findViewById(R.id.txtExtraDetails);
        btnAdd = (Button)findViewById(R.id.btnAdd);
        //
        if (getIntent().hasExtra("address_id")) {
            //
            Bundle b = getIntent().getExtras();
            //
            addressId = b.getString("address_id");
            //
            ab.setTitle(R.string.edit_address);
        }
        //
        txtArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(act);

                builder.setTitle(R.string.area);

                builder.setAdapter(arrAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try {

                            JSONObject obj = new JSONObject(arrList.get(which));

                            if (obj.getInt("area_id") > 0) {

                                areaId = obj.getInt("area_id");

                                txtArea.setText(obj.getString("area"));

                                dialog.dismiss();

                            }

                        } catch (JSONException e) {

                            Log.d("JSONException", e.getMessage());
                        }

                    }
                });

                builder.show();

            }
        });
        //
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUpdateAddress();
            }
        });
        //
        loadData();
    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            String serviceName = "getAreas";

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            if (!addressId.equalsIgnoreCase("")) {

                serviceName = "getAddress";

                params.put("addressId", addressId);
                params.put("userId", GlobalFunctions.getPrefrences(act, "user_id"));

            }

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + serviceName, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");
                            //
                            arrList = new ArrayList<String>();
                            //
                            for (int i = 0; i < arr.length(); i++) {

                                arrList.add(arr.getJSONObject(i).toString());
                            }
                            //
                            arrAdapter = new AreasAdapter(act, arrList);

                            if (!addressId.equalsIgnoreCase("")) {

                                txtAddressName.setText(obj.getString("address_name"));
                                txtArea.setText(obj.getString("area"));
                                txtBlock.setText(obj.getString("block"));
                                txtStreet.setText(obj.getString("street"));
                                txtAvenue.setText(obj.getString("avenue"));
                                txtHouseBuilding.setText(obj.getString("house_building"));
                                txtFloor.setText(obj.getString("floor"));
                                txtApartment.setText(obj.getString("apartment"));
                                txtExtraDetails.setText(obj.getString("extra_details"));

                                areaId = obj.getInt("area_id");

                                btnAdd.setText(getString(R.string.save));

                            }

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        Log.d("JSONException", e.getMessage());

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

    private void addUpdateAddress() {

        if (GlobalFunctions.isEmptyText(txtAddressName)) {
            txtAddressName.requestFocus();
            txtAddressName.setError(getString(R.string.req_address));
            return;
        }

        if (areaId == 0) {
            txtArea.setError(getString(R.string.req_area));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtBlock)) {
            txtBlock.requestFocus();
            txtBlock.setError(getString(R.string.req_block));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtStreet)) {
            txtStreet.requestFocus();
            txtStreet.setError(getString(R.string.req_street));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtHouseBuilding)) {
            txtHouseBuilding.requestFocus();
            txtHouseBuilding.setError(getString(R.string.req_house_building));
            return;
        }

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            String serviceName = "addAddress";

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            if (!addressId.equalsIgnoreCase("")) {

                serviceName = "updateAddress";

                params.put("addressId", addressId);
            }

            params.put("userId", GlobalFunctions.getPrefrences(act, "user_id"));
            params.put("areaId", areaId);
            params.put("addressName", txtAddressName.getText().toString().trim());
            params.put("block", txtBlock.getText().toString().trim());
            params.put("street", txtStreet.getText().toString().trim());
            params.put("avenue", txtAvenue.getText().toString().trim());
            params.put("building", txtHouseBuilding.getText().toString().trim());
            params.put("floorNumber", txtFloor.getText().toString().trim());
            params.put("apartmentNumber", txtApartment.getText().toString().trim());
            params.put("extraDetails", txtExtraDetails.getText().toString().trim());
            params.put("ran", GlobalFunctions.getRandom());

            client.post(GlobalFunctions.serviceURL + serviceName, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            GlobalFunctions.showToastSuccess(act, obj.getString("msg"));

                            if (!addressId.equalsIgnoreCase("")) {

                                setResult(RESULT_OK);

                            } else {

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("address_id", obj.getString("address_id"));

                                setResult(RESULT_OK, resultIntent);

                            }

                            finish();

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
