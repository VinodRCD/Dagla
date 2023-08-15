package com.dagla.android;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class ChangePasswordActivity extends AppCompatActivity {

    Activity act;
    EditText txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    Button btnSubmit;

    Dialog dlgLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_change_password);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.change_password));
        //
        act = this;
        txtCurrentPassword = findViewById(R.id.txtCurrentPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnSubmit = findViewById(R.id.btnSubmit);
        //
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePassword();

            }
        });
        //
    }

    private void changePassword() {

        if (GlobalFunctions.isEmptyText(txtCurrentPassword)) {
            txtCurrentPassword.requestFocus();
            txtCurrentPassword.setError(getString(R.string.req_current_password));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtNewPassword)) {
            txtNewPassword.requestFocus();
            txtNewPassword.setError(getString(R.string.req_new_password));
            return;
        }

        if (GlobalFunctions.isTextLengthLessThan(txtNewPassword, 6)) {
            txtNewPassword.requestFocus();
            txtNewPassword.setError(getString(R.string.req_new_password_6_chars));
            return;
        }

        if (GlobalFunctions.isEmptyText(txtConfirmPassword)) {
            txtConfirmPassword.requestFocus();
            txtConfirmPassword.setError(getString(R.string.req_confirm_password));
            return;
        }

        if (GlobalFunctions.isTextNotEqualsText(txtNewPassword, txtConfirmPassword)) {
            txtConfirmPassword.requestFocus();
            txtConfirmPassword.setError(getString(R.string.req_new_password_do_not_match));
            return;
        }

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(act, "user_id"));
            params.put("currentPassword", txtCurrentPassword.getText().toString().trim());
            params.put("newPassword", txtNewPassword.getText().toString().trim());
            params.put("ran", GlobalFunctions.getRandom());
            //
            client.get(GlobalFunctions.serviceURL + "changePassword", params, new AsyncHttpResponseHandler() {

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

                            GlobalFunctions.showToastSuccess(act, obj.getString("msg"));

                            txtCurrentPassword.setText("");
                            txtNewPassword.setText("");
                            txtConfirmPassword.setText("");

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
