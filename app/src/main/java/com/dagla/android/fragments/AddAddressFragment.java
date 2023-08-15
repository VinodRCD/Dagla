package com.dagla.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dagla.android.AreasAdapter;
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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class AddAddressFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    EditText txtAddressName, txtArea, txtBlock, txtStreet, txtAvenue, txtHouseBuilding, txtFloor,
            txtApartment, txtExtraDetails;
    Button btnAdd;

    Dialog dlgLoading = null;

    ArrayList<String> arrList;

    ArrayAdapter<String> arrAdapter;

    String addressId = "";

    int areaId = 0;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.add_new_address_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.add_new_address),false,false,false,true, false ,"0", false);
        }


        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_add_address_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_add_address, container, false);
            }


            txtAddressName = (EditText)rootView.findViewById(R.id.txtAddressName);
            txtArea = (EditText)rootView.findViewById(R.id.txtArea);
            txtBlock = (EditText)rootView.findViewById(R.id.txtBlock);
            txtStreet = (EditText)rootView.findViewById(R.id.txtStreet);
            txtAvenue = (EditText)rootView.findViewById(R.id.txtAvenue);
            txtHouseBuilding = (EditText)rootView.findViewById(R.id.txtHouseBuilding);
            txtFloor = (EditText)rootView.findViewById(R.id.txtFloor);
            txtApartment = (EditText)rootView.findViewById(R.id.txtApartment);
            txtExtraDetails = (EditText)rootView.findViewById(R.id.txtExtraDetails);
            btnAdd = (Button)rootView.findViewById(R.id.btnAdd);



            //
            if (getArguments()!= null) {

                addressId = getArguments().getString("address_id");
                //
//                ab.setTitle(R.string.edit_address);
            }
            //
            txtArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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

        return rootView;

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            String serviceName = "getAreas";

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            if (!addressId.equalsIgnoreCase("")) {

                serviceName = "getAddress";

                params.put("addressId", addressId);
                params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));

            }

            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

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
                            arrAdapter = new AreasAdapter(getActivity(), arrList);

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

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        Log.d("JSONException", e.getMessage());

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

    private void addUpdateAddress() {

        if (GlobalFunctions.isEmptyText(txtAddressName)) {
            txtAddressName.requestFocus();
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                txtAddressName.setError(getString(R.string.req_address_ar));
            }else {
                txtAddressName.setError(getString(R.string.req_address));
            }

            return;
        }

        if (areaId == 0) {
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                txtArea.setError(getString(R.string.req_area_ar));
            }else {
                txtArea.setError(getString(R.string.req_area));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtBlock)) {
            txtBlock.requestFocus();
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                txtBlock.setError(getString(R.string.req_block_ar));
            }else {
                txtBlock.setError(getString(R.string.req_block));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtStreet)) {
            txtStreet.requestFocus();
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                txtStreet.setError(getString(R.string.req_street_ar));
            }else {
                txtStreet.setError(getString(R.string.req_street));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtHouseBuilding)) {
            txtHouseBuilding.requestFocus();
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                txtHouseBuilding.setError(getString(R.string.req_house_building_ar));
            }else {
                txtHouseBuilding.setError(getString(R.string.req_house_building));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            String serviceName = "addAddress";

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            if (!addressId.equalsIgnoreCase("")) {

                serviceName = "updateAddress";

                params.put("addressId", addressId);
            }

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
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

//                            GlobalFunctions.showToastSuccess(getActivity(), obj.getString("msg"));

                            successDialog(obj.getString("msg"));



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

    public void successDialog(String msg) {
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
        alertMessage.setText(msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);

        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            okBtn.setText("تم");
        }else {
            okBtn.setText("Ok");
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        dialog.show();

    }
}
