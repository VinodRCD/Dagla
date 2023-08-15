package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

import cz.msebera.android.httpclient.Header;

public class ForgotPasswordFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    EditText txtEmail;
    Button btnSubmit;

    Dialog dlgLoading = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.forgot_password_ar),false,false,false,true,false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.forgot_password),false,false,false,true,false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_forgot_password_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false);
            }


            context = getActivity();
            txtEmail = (EditText)rootView.findViewById(R.id.txtEmail);
            btnSubmit = (Button)rootView.findViewById(R.id.btnSubmit);


            //
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    forgotPassword();
                }
            });

        }

        return rootView;

    }



    private void forgotPassword() {

        if (GlobalFunctions.isEmptyText(txtEmail)) {
            txtEmail.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtEmail.setError(getString(R.string.req_email_address_ar));
            }else {
                txtEmail.setError(getString(R.string.req_email_address));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(context)) {

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

//                            GlobalFunctions.showPopup(context, obj.getString("msg"));
                            successDialog(obj.getString("msg"));
                        } else {

//                            GlobalFunctions.showToastError(context, obj.getString("msg"));
                            errorDialog(obj.getString("msg"));
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


            }
        });

        dialog.show();

    }


    public void errorDialog(String msg) {
        final Dialog dialog = new Dialog(getActivity());
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


        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
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
