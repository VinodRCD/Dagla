package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordActivity extends AppCompatActivity {

    Activity act;
    EditText txtEmail;
    Button btnSubmit,btnBack;

    Dialog dlgLoading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);

        if(GlobalFunctions.getLang(ForgotPasswordActivity.this).equals("ar")){
            setContentView(R.layout.forgot_password_activity_ar);
        }else {
            setContentView(R.layout.forgot_password_activity);
        }

        //
//        ActionBar ab = getSupportActionBar();
//        //
//        ab.setDisplayHomeAsUpEnabled(true);
//        ab.setDisplayShowHomeEnabled(false);
//        //
//        ab.setTitle(getString(R.string.forgot_password));
        //
        act = this;
        txtEmail = (EditText)findViewById(R.id.txtEmail);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnBack = (Button)findViewById(R.id.btnBack);


        //
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                forgotPassword();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }

    private void forgotPassword() {

        if (GlobalFunctions.isEmptyText(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(ForgotPasswordActivity.this).equals("ar")){
                txtEmail.setError(getString(R.string.req_email_address_ar));
            }else {
                txtEmail.setError(getString(R.string.req_email_address));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("email", txtEmail.getText().toString().trim());
            params.put("ran", GlobalFunctions.getRandom());
            //
            client.get(GlobalFunctions.serviceURL + "forgotPassword", params, new AsyncHttpResponseHandler() {

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

                            txtEmail.setText("");

//                            GlobalFunctions.showPopup(act, obj.getString("msg"));
                            successDialog(obj.getString("msg"));
                        } else {

//                            GlobalFunctions.showToastError(act, obj.getString("msg"));
                            errorDialog(obj.getString("msg"));
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


    public void successDialog(String msg) {
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


            }
        });

        dialog.show();

    }


    public void errorDialog(String msg) {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog_box);
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


            }
        });

        dialog.show();

    }



}
