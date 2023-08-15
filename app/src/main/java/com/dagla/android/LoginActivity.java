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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    Activity act;
    EditText txtEmail, txtPassword;
    TextView lblOR;
    Button btnLogin, btnForgotPassword, btnExpressCheckout, btnRegister;
    LinearLayout pnlExpressCheckout;

    Dialog dlgLoading = null;

    String returnActivity = "";

    static final int REGISTER_REQUEST = 101;
    static final int EXPRESS_CHECKOUT_REQUEST = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_login);
        //
        ActionBar ab = getSupportActionBar();
        //
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(false);
        //
        ab.setTitle(getString(R.string.login));
        //
        act = this;
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        lblOR = findViewById(R.id.lblOR);
        btnLogin = findViewById(R.id.btnLogin);
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnExpressCheckout = findViewById(R.id.btnExpressCheckout);
        btnRegister = findViewById(R.id.btnRegister);
        pnlExpressCheckout = findViewById(R.id.pnlExpressCheckout);
        //
        if (GlobalFunctions.getLang(act).equalsIgnoreCase("ar")) {
            //
            btnForgotPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_arrow_left, 0, 0, 0);
            //
        } else {
            //
            btnForgotPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_arrow_right, 0);
        }
        //
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
        //
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, ForgotPasswordActivity.class));
            }
        });
        //
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(act, RegisterActivity.class), REGISTER_REQUEST);

            }
        });
        //
        btnExpressCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!GlobalFunctions.getPrefrences(act, "user_id").equalsIgnoreCase("") && !returnActivity.equalsIgnoreCase("")) {

                    try {

                        startActivity(new Intent(act, Class.forName("com.dagla.android." + returnActivity)));

                    } catch (ClassNotFoundException e) {

                        startActivityForResult(new Intent(act, ExpressCheckoutActivity.class), EXPRESS_CHECKOUT_REQUEST);

                    }

                } else {

                    startActivityForResult(new Intent(act, ExpressCheckoutActivity.class), EXPRESS_CHECKOUT_REQUEST);

                }

            }
        });
        //
        if (getIntent().hasExtra("returnActivity")) {
            //
            Bundle b = getIntent().getExtras();
            //
            returnActivity = b.getString("returnActivity");
            //
            if (returnActivity.equalsIgnoreCase("CheckoutDeliveryActivity")) {
                //
                lblOR.setVisibility(View.VISIBLE);
                //
                pnlExpressCheckout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void login() {

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

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("email", txtEmail.getText().toString().trim());
            params.put("password", txtPassword.getText().toString().trim());
            params.put("androidToken", GlobalFunctions.getPrefrences(act, "token"));
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "authenticateUser", params, new AsyncHttpResponseHandler() {

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

                            act.finish();

                            if (!returnActivity.equalsIgnoreCase("")) {

                                startActivity(new Intent(act, Class.forName("com.dagla.android." + returnActivity)));

                            } else {

                                GlobalFunctions.showToastSuccess(act, getString(R.string.your_login_was_successful));

                            }

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    } catch (ClassNotFoundException e) {

                        //
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REGISTER_REQUEST || requestCode == EXPRESS_CHECKOUT_REQUEST) {

            if (resultCode == RESULT_OK) {

                act.finish();

                if (!returnActivity.equalsIgnoreCase("")) {

                    try {

                        startActivity(new Intent(act, Class.forName("com.dagla.android." + returnActivity)));

                    } catch (ClassNotFoundException e) {
                        //
                    }

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
