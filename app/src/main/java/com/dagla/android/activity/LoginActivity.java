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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dagla.android.ExpressCheckoutActivity;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
//import ly.count.android.sdk.Countly;

public class LoginActivity extends AppCompatActivity {

    Activity act;
    EditText txtEmail, txtPassword;
    TextView lblOR;
    Button btnLogin, btnForgotPassword, btnExpressCheckout, btnRegister,btnBack;
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


        if(GlobalFunctions.getLang(LoginActivity.this).equals("ar")){
            setContentView(R.layout.login_activity_ar);
        }else {
            setContentView(R.layout.login_activity);
        }

        //
//        ActionBar ab = getSupportActionBar();
//        //
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(false);
//        //
//        ab.setTitle(getString(R.string.login));
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
        btnBack = (Button)findViewById(R.id.btnBack);


        if (GlobalFunctions.getLang(act).equals("ar")) {
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

                        startActivity(new Intent(act, Class.forName("com.dagla.android.activity." + returnActivity)));

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }

    private void login() {

        if (GlobalFunctions.isEmptyText(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(LoginActivity.this).equals("ar")){
                txtEmail.setError(getString(R.string.req_email_address_ar));
            }else {
                txtEmail.setError(getString(R.string.req_email_address));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(LoginActivity.this).equals("ar")){
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces_ar));
            }else {
                txtEmail.setError(getString(R.string.email_cannot_contain_spaces));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtPassword)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(LoginActivity.this).equals("ar")){
                txtPassword.setError(getString(R.string.req_password_ar));
            }else {
                txtPassword.setError(getString(R.string.req_password));
            }

            return;
        }

        if (GlobalFunctions.containsSpaces(txtPassword)) {
            txtPassword.requestFocus();
            if(GlobalFunctions.getLang(LoginActivity.this).equals("ar")){
                txtPassword.setError(getString(R.string.password_cannot_contain_spaces_ar));
            }else {
                txtPassword.setError(getString(R.string.password_cannot_contain_spaces));
            }

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

//                            act.finish();

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


                            loginSuccessDialog();
//                                GlobalFunctions.showToastSuccess(act, getString(R.string.your_login_was_successful));
//                                startActivity(new Intent(act, MainActivity.class));
//                            }

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

//                    } catch (ClassNotFoundException e) {

                        //
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


    public void loginSuccessDialog() {
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

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);


        if (GlobalFunctions.getLang(act).equals("ar")) {
            alertMessage.setText("تسجيل دخول ناجح");
            okBtn.setText("تم");
        }else {
            alertMessage.setText("Your login was successful");
            okBtn.setText("Ok");
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                act.finish();
//                startActivity(new Intent(act, MainActivity.class));
                startActivity(new Intent(act, LandingActivity.class));
            }
        });

        dialog.show();

    }


}
