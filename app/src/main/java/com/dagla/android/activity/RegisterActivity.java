package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class RegisterActivity extends AppCompatActivity {

    Activity act;
    EditText txtName, txtMobile, txtEmail, txtPassword;
    Button btnRegister, btnTerms, btnTermsAndConditions, btnPrivacyPolicy,btnBack;

    Dialog dlgLoading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);

        if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
            setContentView(R.layout.register_activity_ar);
        }else {
            setContentView(R.layout.register_activity);
        }

        //
//        ActionBar ab = getSupportActionBar();
//        //
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(false);
//        //
//        ab.setTitle(getString(R.string.register));
        //
        act = this;
        txtName = (EditText)findViewById(R.id.txtName);
        txtMobile = (EditText)findViewById(R.id.txtMobile);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnTerms = (Button)findViewById(R.id.btnTerms);
        btnBack = (Button)findViewById(R.id.btnBack);


        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(act);

        View sheetView;

        if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
            sheetView = act.getLayoutInflater().inflate(R.layout.layout_terms_privacy_sheet_ar, null);
        }else {
            sheetView = act.getLayoutInflater().inflate(R.layout.layout_terms_privacy_sheet, null);
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

                Intent intent = new Intent(act, WebActivity.class);

                intent.putExtra("pageTitle", getString(R.string.terms_n_conditions));
                intent.putExtra("pageURL", GlobalFunctions.baseURL +
                        "static_page.aspx?id=7yfr6aCwW0c=&lang=" + GlobalFunctions.getLang(act));

                startActivity(intent);

            }
        });

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBottomSheetDialog.dismiss();

                Intent intent = new Intent(act, WebActivity.class);

                intent.putExtra("pageTitle", getString(R.string.privacy_policy));
                intent.putExtra("pageURL", GlobalFunctions.baseURL +
                        "static_page.aspx?id=9QWXE0GhLlg=&lang=" + GlobalFunctions.getLang(act));

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
        //

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }

    private void register() {

        if (GlobalFunctions.isEmptyText(txtName)) {
            txtName.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtName.setError(getString(R.string.req_name_ar));
            }else {
                txtName.setError(getString(R.string.req_name));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtMobile)) {
            txtMobile.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtMobile.setError(getString(R.string.req_mobile_number_ar));
            }else {
                txtMobile.setError(getString(R.string.req_mobile_number));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtMobile)) {
            txtMobile.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtMobile.setError(getString(R.string.mobile_cannot_contain_spaces_ar));
            }else {
                txtMobile.setError(getString(R.string.mobile_cannot_contain_spaces));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtEmail.setError(getString(R.string.req_email_address_ar));
            }else {
                txtEmail.setError(getString(R.string.req_email_address));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces_ar));
            }else {
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtPassword)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtPassword.setError(getString(R.string.req_password_ar));
            }else {
                txtPassword.setError(getString(R.string.req_password));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtPassword)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtPassword.setError(getString(R.string.password_cannot_contain_spaces_ar));
            }else {
                txtPassword.setError(getString(R.string.password_cannot_contain_spaces));
            }

            return;
        }

        if (GlobalFunctions.isTextLengthLessThan(txtPassword, 6)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(RegisterActivity.this).equals("ar")){
                txtPassword.setError(getString(R.string.req_password_6_chars_ar));
            }else {
                txtPassword.setError(getString(R.string.req_password_6_chars));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("name", txtName.getText().toString().trim());
            params.put("mobile", txtMobile.getText().toString().trim());
            params.put("email", txtEmail.getText().toString().trim());
            params.put("password", txtPassword.getText().toString().trim());
            params.put("androidToken", GlobalFunctions.getPrefrences(act, "token"));
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

                            GlobalFunctions.setPrefrences(act, "id", obj.getString("id"));
                            GlobalFunctions.setPrefrences(act, "user_id", obj.getString("user_id"));
                            GlobalFunctions.setPrefrences(act, "name", obj.getString("name"));
                            GlobalFunctions.setPrefrences(act, "email", obj.getString("email"));

//                            GlobalFunctions.showToastSuccess(act, obj.getString("msg"));
//
////                            act.setResult(RESULT_OK);
//
//                            act.finish();
////                            GlobalFunctions.showToastSuccess(act, getString(R.string.your_login_was_successful));
//                            startActivity(new Intent(act, MainActivity.class));

                            registrationSuccessDialog(""+obj.getString("msg"));

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(act).equals("ar")) {
                            GlobalFunctions.showToastError(act, getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(act, getString(R.string.msg_error));
                        }

                    }
                    //
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(act).equals("ar")) {
                        GlobalFunctions.showToastError(act, getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));
                    }
                }
            });

        } else {

            if (GlobalFunctions.getLang(act).equals("ar")) {
                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));
            }

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


    public void registrationSuccessDialog(String msg) {
        final Dialog dialog = new Dialog(act);
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

        if (GlobalFunctions.getLang(act).equals("ar")) {
            okBtn.setText("تم");
        }else {
            okBtn.setText("Ok");
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                act.finish();
                startActivity(new Intent(act, MainActivity.class));

            }
        });

        dialog.show();

    }


}
