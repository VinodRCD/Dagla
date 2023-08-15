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

public class ContactUsActivity extends AppCompatActivity {

    Activity act;
    EditText txtName, txtMobile, txtEmail, txtMessage;
    Button btnSubmit;

    Dialog dlgLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_contact_us);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.contact_us));
        //
        act = this;
        txtName = findViewById(R.id.txtName);
        txtMobile = findViewById(R.id.txtMobile);
        txtEmail = findViewById(R.id.txtEmail);
        txtMessage = findViewById(R.id.txtMessage);
        btnSubmit = findViewById(R.id.btnSubmit);
        //
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submit();
            }
        });
    }

    private void submit() {

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

        if (GlobalFunctions.isEmptyText(txtMessage)) {
            txtMessage.requestFocus();
            txtMessage.setError(getString(R.string.req_message));
            return;
        }

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("name", txtName.getText().toString().trim());
            params.put("mobile", txtMobile.getText().toString().trim());
            params.put("email", txtEmail.getText().toString().trim());
            params.put("message", txtMessage.getText().toString().trim());
            params.put("ran", GlobalFunctions.getRandom());

            client.post(GlobalFunctions.serviceURL + "contactUs", params, new AsyncHttpResponseHandler() {

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

                            txtName.setText("");
                            txtMobile.setText("");
                            txtEmail.setText("");
                            txtMessage.setText("");

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
