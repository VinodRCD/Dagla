package com.dagla.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
    Button btnRegister, btnTerms, btnTermsAndConditions, btnPrivacyPolicy;

    Dialog dlgLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_register);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.register));
        //
        act = this;
        txtName = (EditText)findViewById(R.id.txtName);
        txtMobile = (EditText)findViewById(R.id.txtMobile);
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnTerms = (Button)findViewById(R.id.btnTerms);

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(act);

        View sheetView = act.getLayoutInflater().inflate(R.layout.layout_terms_privacy_sheet, null);

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

                Intent intent = new Intent(act, WebActivity.class);

                intent.putExtra("pageTitle", getString(R.string.terms_n_conditions));
                intent.putExtra("pageURL", GlobalFunctions.baseURL +
                        "static_page.aspx?id=7yfr6aCwW0c=+lang=" + GlobalFunctions.getLang(act));

                startActivity(intent);

            }
        });

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(act, WebActivity.class);

                intent.putExtra("pageTitle", getString(R.string.privacy_policy));
                intent.putExtra("pageURL", GlobalFunctions.baseURL +
                        "static_page.aspx?id=9QWXE0GhLlg=+lang=" + GlobalFunctions.getLang(act));

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
    }

    private void register() {

        if (GlobalFunctions.isEmptyText(txtName)) {
            txtName.requestFocus();
            txtName.setError(getString(R.string.req_name));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtMobile)) {
            txtMobile.requestFocus();
            txtMobile.setError(getString(R.string.req_mobile_number));
            return;
        }

        if (GlobalFunctions.containsSpaces(txtMobile)) {
            txtMobile.requestFocus();
            txtMobile.setError(getString(R.string.mobile_cannot_contain_spaces));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtEmail)) {
            txtEmail.requestFocus();
            txtEmail.setError(getString(R.string.req_email_address));
            return;
        }

        if (GlobalFunctions.containsSpaces(txtEmail)) {
            txtEmail.requestFocus();
            txtEmail.setError(getString(R.string.email_cannot_contain_spaces));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtPassword)) {
            txtPassword.requestFocus();
            txtPassword.setError(getString(R.string.req_password));
            return;
        }

        if (GlobalFunctions.containsSpaces(txtPassword)) {
            txtPassword.requestFocus();
            txtPassword.setError(getString(R.string.password_cannot_contain_spaces));
            return;
        }

        if (GlobalFunctions.isTextLengthLessThan(txtPassword, 6)) {
            txtPassword.requestFocus();
            txtPassword.setError(getString(R.string.req_password_6_chars));
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

                            GlobalFunctions.showToastSuccess(act, obj.getString("msg"));

                            act.setResult(RESULT_OK);

                            act.finish();

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }
                    //
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
