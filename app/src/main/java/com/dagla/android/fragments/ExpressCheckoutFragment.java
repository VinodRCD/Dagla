package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
//import ly.count.android.sdk.Countly;

import static android.app.Activity.RESULT_OK;

public class ExpressCheckoutFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    EditText txtName, txtMobile, txtEmail;
    Button btnSubmit;

    Dialog dlgLoading = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        context = getActivity();
        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.express_checkout_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.express_checkout),false,false,false,true, false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_express_checkout_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_express_checkout, container, false);
            }


            txtName = rootView.findViewById(R.id.txtName);
            txtMobile = rootView.findViewById(R.id.txtMobile);
            txtEmail = rootView.findViewById(R.id.txtEmail);
            btnSubmit = rootView.findViewById(R.id.btnSubmit);
            //
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    expressCheckout();
                }
            });

        }

        return rootView;

    }

    private void expressCheckout() {

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

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("name", txtName.getText().toString().trim());
            params.put("mobile", txtMobile.getText().toString().trim());
            params.put("email", txtEmail.getText().toString().trim());
            params.put("androidToken", GlobalFunctions.getPrefrences(getActivity(), "token"));
            params.put("ran", GlobalFunctions.getRandom());

            client.post(GlobalFunctions.serviceURL + "expressCheckout", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                    //
                    hideLoading();
                    //
                    JSONObject obj;
                    //
                    String response = new String(bytes);
                    //
                    Log.d("onSuccess", response);
                    //
                    try {
                        //
                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            GlobalFunctions.setPrefrences(getActivity(), "id", obj.getString("id"));
                            GlobalFunctions.setPrefrences(getActivity(), "user_id", obj.getString("user_id"));
                            GlobalFunctions.setPrefrences(getActivity(), "name", obj.getString("name"));
                            GlobalFunctions.setPrefrences(getActivity(), "email", obj.getString("email"));

                            HashMap<String, String> data = new HashMap<>();
                            data.put("name", ""+obj.getString("name"));
                            data.put("username", "");
                            data.put("email", ""+obj.getString("email"));
                            data.put("organization", "");
                            data.put("phone", "");

                            //providing any custom key values to store with user
                            HashMap<String, String> custom = new HashMap<>();
                            custom.put("country", "");
                            custom.put("city", "");
                            custom.put("address", "");

//                            //set multiple custom properties
//                            Countly.userData.setUserData(data, custom);
//
//                            //set custom properties by one
////                            Countly.userData.setProperty("test", "test");
//
//                            //increment used value by 1
//                            Countly.userData.incrementBy("used", 1);
//
//                            //insert value to array of unique values
////                            Countly.userData.pushUniqueValue("type", "");
//
//                            //insert multiple values to same property
////                            Countly.userData.pushUniqueValue("skill", "");
////                            Countly.userData.pushUniqueValue("skill", "earth");
//
//                            Countly.userData.save();


                            ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
                            Intent intent = new Intent(context, ExpressCheckoutFragment.class);

                            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }
                    //
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


}
