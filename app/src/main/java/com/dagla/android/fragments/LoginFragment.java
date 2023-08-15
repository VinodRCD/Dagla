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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.ExpressCheckoutActivity;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
//import ly.count.android.sdk.Countly;

import static android.app.Activity.RESULT_OK;

public class LoginFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    EditText txtEmail, txtPassword;
    TextView lblOR;
    Button btnLogin, btnForgotPassword, btnExpressCheckout, btnRegister;
    LinearLayout pnlExpressCheckout;

    Dialog dlgLoading = null;

    String returnActivity = "";

    static final int REGISTER_REQUEST = 101;
    static final int EXPRESS_CHECKOUT_REQUEST = 102;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.login_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.login),false,false,false,true, false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_login_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_login, container, false);
            }


            context = getActivity();

            txtEmail = rootView.findViewById(R.id.txtEmail);
            txtPassword = rootView.findViewById(R.id.txtPassword);
            lblOR = rootView.findViewById(R.id.lblOR);
            btnLogin = rootView.findViewById(R.id.btnLogin);
            btnForgotPassword = rootView.findViewById(R.id.btnForgotPassword);
            btnExpressCheckout = rootView.findViewById(R.id.btnExpressCheckout);
            btnRegister = rootView.findViewById(R.id.btnRegister);
            pnlExpressCheckout = rootView.findViewById(R.id.pnlExpressCheckout);



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

//                    startActivity(new Intent(context, ForgotPasswordActivity.class));

                    ForgotPasswordFragment fragment1 = new ForgotPasswordFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "ForgotPasswordFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();
                }
            });
            //
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    startActivityForResult(new Intent(context, RegisterActivity.class), REGISTER_REQUEST);

                    RegisterFragment fragment1 = new RegisterFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "RegisterFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });
            //
            btnExpressCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!GlobalFunctions.getPrefrences(context, "user_id").equalsIgnoreCase("") && !returnActivity.equalsIgnoreCase("")) {

                        try {

                            startActivity(new Intent(context, Class.forName("com.dagla.android." + returnActivity)));

                        } catch (ClassNotFoundException e) {

                            startActivityForResult(new Intent(context, ExpressCheckoutActivity.class), EXPRESS_CHECKOUT_REQUEST);

                        }

                    } else {

                        startActivityForResult(new Intent(context, ExpressCheckoutActivity.class), EXPRESS_CHECKOUT_REQUEST);

                    }

                }
            });
            //
            if (getArguments()!= null) {
                //
                returnActivity = getArguments().getString("returnActivity");
                //
                if (returnActivity.equalsIgnoreCase("CheckoutDeliveryActivity")) {
                    //
                    lblOR.setVisibility(View.VISIBLE);
                    //
                    pnlExpressCheckout.setVisibility(View.VISIBLE);
                }
            }

        }

        return rootView;

    }


    private void login() {

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

        if (GlobalFunctions.hasConnection(context)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("email", txtEmail.getText().toString().trim());
            params.put("password", txtPassword.getText().toString().trim());
            params.put("androidToken", GlobalFunctions.getPrefrences(context, "token"));
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

                            GlobalFunctions.setPrefrences(context, "id", obj.getString("id"));
                            GlobalFunctions.setPrefrences(context, "user_id", obj.getString("user_id"));
                            GlobalFunctions.setPrefrences(context, "name", obj.getString("name"));
                            GlobalFunctions.setPrefrences(context, "email", obj.getString("email"));

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

//                            ((MainActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

//                            GlobalFunctions.showToastSuccess(context, getString(R.string.your_login_was_successful));


                            ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            MyAccountFragment fragment1 = new MyAccountFragment();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container, fragment1, "RegisterFragment")
                                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .addToBackStack(null)
                                    // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                    .commitAllowingStateLoss();


                        } else {

                            GlobalFunctions.showToastError(context, obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(context, getString(R.string.msg_error));

//                    } catch (ClassNotFoundException e) {

                        //
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REGISTER_REQUEST || requestCode == EXPRESS_CHECKOUT_REQUEST) {

            if (resultCode == RESULT_OK) {

                getActivity().finish();

                if (!returnActivity.equalsIgnoreCase("")) {

                    try {

                        startActivity(new Intent(context, Class.forName("com.dagla.android." + returnActivity)));

                    } catch (ClassNotFoundException e) {
                        //
                    }

                }

            }
        }
    }


}
