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

public class ChangePasswordFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    EditText txtCurrentPassword, txtNewPassword, txtConfirmPassword;
    Button btnSubmit;

    Dialog dlgLoading = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.change_password_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.change_password),false,false,false,true, false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_change_password_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_change_password, container, false);
            }


            txtCurrentPassword = rootView.findViewById(R.id.txtCurrentPassword);
            txtNewPassword = rootView.findViewById(R.id.txtNewPassword);
            txtConfirmPassword = rootView.findViewById(R.id.txtConfirmPassword);
            btnSubmit = rootView.findViewById(R.id.btnSubmit);


            //
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    changePassword();

                }
            });

        }

        return rootView;

    }

    private void changePassword() {

        if (GlobalFunctions.isEmptyText(txtCurrentPassword)) {
            txtCurrentPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtCurrentPassword.setError(getString(R.string.req_current_password_ar));
            }else {
                txtCurrentPassword.setError(getString(R.string.req_current_password));
            }


            return;
        }

        if (GlobalFunctions.isEmptyText(txtNewPassword)) {
            txtNewPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtNewPassword.setError(getString(R.string.req_new_password_ar));
            }else {
                txtNewPassword.setError(getString(R.string.req_new_password));
            }

            return;
        }

        if (GlobalFunctions.isTextLengthLessThan(txtNewPassword, 6)) {
            txtNewPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtNewPassword.setError(getString(R.string.req_new_password_6_chars_ar));
            }else {
                txtNewPassword.setError(getString(R.string.req_new_password_6_chars));
            }

            return;
        }

        if (GlobalFunctions.isEmptyText(txtConfirmPassword)) {
            txtConfirmPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtConfirmPassword.setError(getString(R.string.req_confirm_password_ar));
            }else {
                txtConfirmPassword.setError(getString(R.string.req_confirm_password));
            }

            return;
        }

        if (GlobalFunctions.isTextNotEqualsText(txtNewPassword, txtConfirmPassword)) {
            txtConfirmPassword.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                txtConfirmPassword.setError(getString(R.string.req_new_password_do_not_match_ar));
            }else {
                txtConfirmPassword.setError(getString(R.string.req_new_password_do_not_match));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
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

//                            GlobalFunctions.showToastSuccess(getActivity(), obj.getString("msg"));
                            successDialog(obj.getString("msg"));
                            txtCurrentPassword.setText("");
                            txtNewPassword.setText("");
                            txtConfirmPassword.setText("");

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
}
