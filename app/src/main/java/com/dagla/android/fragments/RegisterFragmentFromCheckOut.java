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
import com.dagla.android.activity.WebActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class RegisterFragmentFromCheckOut extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    EditText txtName, txtMobile, txtEmail, txtPassword;
    Button btnRegister, btnTerms, btnTermsAndConditions, btnPrivacyPolicy;

    Dialog dlgLoading = null;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.register_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.register),false,false,false,true, false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_register_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_register, container, false);
            }


            context = getActivity();
            txtName = (EditText)rootView.findViewById(R.id.txtName);
            txtMobile = (EditText)rootView.findViewById(R.id.txtMobile);
            txtEmail = (EditText)rootView.findViewById(R.id.txtEmail);
            txtPassword = (EditText)rootView.findViewById(R.id.txtPassword);
            btnRegister = (Button)rootView.findViewById(R.id.btnRegister);
            btnTerms = (Button)rootView.findViewById(R.id.btnTerms);


            final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);

//            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.layout_terms_privacy_sheet, null);

            View sheetView;

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                sheetView = getActivity().getLayoutInflater().inflate(R.layout.layout_terms_privacy_sheet_ar, null);
            }else {
                sheetView = getActivity().getLayoutInflater().inflate(R.layout.layout_terms_privacy_sheet, null);
            }

            mBottomSheetDialog.setContentView(sheetView);

            btnTermsAndConditions = sheetView.findViewById(R.id.btnTermsAndConditions);
            btnPrivacyPolicy = sheetView.findViewById(R.id.btnPrivacyPolicy);

            btnTerms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mBottomSheetDialog.show();

                }
            });

            btnTermsAndConditions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mBottomSheetDialog.dismiss();

                    Intent intent = new Intent(getActivity(), WebActivity.class);

                    intent.putExtra("pageTitle", getString(R.string.terms_n_conditions));
                    intent.putExtra("pageURL", GlobalFunctions.baseURL +
                            "static_page.aspx?id=7yfr6aCwW0c=&lang=" + GlobalFunctions.getLang(getActivity()));

                    startActivity(intent);

                }
            });

            btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mBottomSheetDialog.dismiss();

                    Intent intent = new Intent(getActivity(), WebActivity.class);

                    intent.putExtra("pageTitle", getString(R.string.privacy_policy));
                    intent.putExtra("pageURL", GlobalFunctions.baseURL +
                            "static_page.aspx?id=9QWXE0GhLlg=&lang=" + GlobalFunctions.getLang(getActivity()));

                    startActivity(intent);

                }
            });
            //
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    register();

                }
            });

        }

        return rootView;

    }


    private void register() {

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

        if (GlobalFunctions.isEmptyText(txtPassword)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtPassword.setError(getString(R.string.req_password_ar));
            }else {
                txtPassword.setError(getString(R.string.req_password));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtPassword)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtPassword.setError(getString(R.string.password_cannot_contain_spaces_ar));
            }else {
                txtPassword.setError(getString(R.string.password_cannot_contain_spaces));
            }

            return;
        }

        if (GlobalFunctions.isTextLengthLessThan(txtPassword, 6)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtPassword.setError(getString(R.string.req_password_6_chars_ar));
            }else {
                txtPassword.setError(getString(R.string.req_password_6_chars));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(context)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("name", txtName.getText().toString().trim());
            params.put("mobile", txtMobile.getText().toString().trim());
            params.put("email", txtEmail.getText().toString().trim());
            params.put("password", txtPassword.getText().toString().trim());
            params.put("androidToken", GlobalFunctions.getPrefrences(context, "token"));
            params.put("ran", GlobalFunctions.getRandom());

            client.post(GlobalFunctions.serviceURL + "registerUser", params, new AsyncHttpResponseHandler() {

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

                            GlobalFunctions.setPrefrences(context, "id", obj.getString("id"));
                            GlobalFunctions.setPrefrences(context, "user_id", obj.getString("user_id"));
                            GlobalFunctions.setPrefrences(context, "name", obj.getString("name"));
                            GlobalFunctions.setPrefrences(context, "email", obj.getString("email"));

//                            GlobalFunctions.showToastSuccess(context, obj.getString("msg"));


                            ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
                            Intent intent = new Intent(context, RegisterFragmentFromCheckOut.class);

                            getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, intent);

//                            ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();

                        } else {

                            GlobalFunctions.showToastError(context, obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(context, getString(R.string.msg_error));

                    }
                    //
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    GlobalFunctions.showToastError(context, getString(R.string.msg_error));
                }
            });

        } else {

            GlobalFunctions.showToastError(context, getString(R.string.msg_no_internet));

        }

    }

    private void showLoading() {

        if (dlgLoading == null) {

            dlgLoading = GlobalFunctions.showLoading(context);

        } else {

            dlgLoading.show();
        }

    }

    private void hideLoading() {

        dlgLoading.dismiss();

    }
}
