package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.CheckoutPaymentActivity;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.MyApplication;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.AddressesAdapter;
import com.dagla.android.adapter.AddressesFromCheckOutAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;

public class CheckoutDeliveryFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    ListView lv;
    TextView lblTotal,lblTotalTxt;
    Button btnAddAddress;

    Dialog dlgLoading = null;

    ArrayList<String> arrList;

    ArrayAdapter<String> arrAdapter;

    static final int ADD_ADDRESS_REQUEST = 102;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.delivery_address_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.delivery_address),false,false,false,true, false ,"0", false);
        }

//        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_checkout_delivery_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_checkout_delivery, container, false);
            }


            GlobalFunctions.setLanguage(getActivity());
            //

            lv = rootView.findViewById(R.id.lv);
            lblTotalTxt = rootView.findViewById(R.id.lblTotalTxt);
            lblTotal = rootView.findViewById(R.id.lblTotal);
            btnAddAddress = rootView.findViewById(R.id.btnAddAddress);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    arg1.setEnabled(false);

                    try {

                        JSONObject obj = new JSONObject(lv.getItemAtPosition(position).toString());

                        ((MyApplication)getActivity().getApplication()).setDeliveryAddressId(obj.getString("address_id"));

//                        getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
//                        CheckoutPaymentFragment fragment = new CheckoutPaymentFragment();
//                        CheckoutPaymentFragmentNew fragment = new CheckoutPaymentFragmentNew();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.fragment_container, fragment, "CheckoutPaymentFragment")
//                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                .addToBackStack(null)
//                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                                .commitAllowingStateLoss();

                    } catch (JSONException e) {

                        Log.d("JSONException", e.getMessage());
                    }

                    arg1.setEnabled(true);

                }
            });
            //
            btnAddAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    startActivityForResult(new Intent(getActivity(), AddAddressActivity.class), ADD_ADDRESS_REQUEST);

                    AddAddressFragmentFromCheckOut fragment1 = new AddAddressFragmentFromCheckOut();
                    LoginFragmentFromCheckOut fragment2 = new LoginFragmentFromCheckOut();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
//                            .remove(fragment2)
//                            .add(R.id.fragment_container, fragment1, "RegisterFragment")
                            .replace(R.id.fragment_container, fragment1, "AddAddressFragmentFromCheckOut")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });
            //
//            loadData();
        loadData2();
//        }

        return rootView;

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

            client.get(GlobalFunctions.serviceURL + "getCheckoutAddresses", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {
                            //
                            arr = obj.getJSONArray("data");
                            //
                            arrList = new ArrayList<String>();
                            //
                            for (int i = 0; i < arr.length(); i++) {
                                //
                                arrList.add(arr.getJSONObject(i).toString());
                            }
                            //
                            arrAdapter = new AddressesFromCheckOutAdapter(getActivity(), arrList, CheckoutDeliveryFragment.this);
                            //
                            lv.setAdapter(arrAdapter);
                            //
                            if (arr.length() == 0) {

                                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                    addAddressDialog(getString(R.string.your_address_book_is_empty_ar));
                                }else {
                                    addAddressDialog(getString(R.string.your_address_book_is_empty));
                                }


//                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                                builder.setMessage(R.string.your_address_book_is_empty);
//
//                                builder.setPositiveButton(getString(R.string.add_new_address).toUpperCase(), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
////                                        startActivityForResult(new Intent(getActivity(), AddAddressActivity.class), ADD_ADDRESS_REQUEST);
//
//                                        AddAddressFragmentFromCheckOut fragment1 = new AddAddressFragmentFromCheckOut();
//                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                        fragmentManager.beginTransaction()
//                                                .replace(R.id.fragment_container, fragment1, "AddAddressFragmentFromCheckOut")
//                                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                                .addToBackStack(null)
//                                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                                                .commitAllowingStateLoss();
//
//                                    }
//                                });
//
//                                builder.setCancelable(false);
//
//                                AlertDialog dialog = builder.create();
//
//                                dialog.show();

                            } else if (arr.length() == 1) {
                                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                    lblTotal.setText("1   "+getString(R.string.address_ar));
                                }else {
                                    lblTotal.setText(String.format(Locale.US, "1 %s", getString(R.string.address)));
                                }


                            } else {
                                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                    lblTotal.setText(""+arr.length()+"   "+ getString(R.string.addresses_ar));
                                }else {
                                    lblTotal.setText(String.format(Locale.US, "%d %s", arr.length(), getString(R.string.addresses)));
                                }

                            }

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

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

    private void loadData2() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
//            }else{
//                params.put("curr", "KWD");
            }

            client.get(GlobalFunctions.serviceURL + "getMyAddresses", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {
                            //
                            arr = obj.getJSONArray("data");
                            //
                            arrList = new ArrayList<String>();
                            //
                            for (int i = 0; i < arr.length(); i++) {
                                //
                                arrList.add(arr.getJSONObject(i).toString());
                            }
                            arrAdapter = new AddressesFromCheckOutAdapter(getActivity(), arrList, CheckoutDeliveryFragment.this);
                            //
                            lv.setAdapter(arrAdapter);
                            //
                            if (arr.length() == 0) {

                                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                    addAddressDialog(getString(R.string.your_address_book_is_empty_ar));
                                }else {
                                    addAddressDialog(getString(R.string.your_address_book_is_empty));
                                }


//                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                                builder.setMessage(R.string.your_address_book_is_empty);
//
//                                builder.setPositiveButton(getString(R.string.add_new_address).toUpperCase(), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
////                                        startActivityForResult(new Intent(getActivity(), AddAddressActivity.class), ADD_ADDRESS_REQUEST);
//
//                                        AddAddressFragmentFromCheckOut fragment1 = new AddAddressFragmentFromCheckOut();
//                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                        fragmentManager.beginTransaction()
//                                                .replace(R.id.fragment_container, fragment1, "AddAddressFragmentFromCheckOut")
//                                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                                .addToBackStack(null)
//                                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                                                .commitAllowingStateLoss();
//
//                                    }
//                                });
//
//                                builder.setCancelable(false);
//
//                                AlertDialog dialog = builder.create();
//
//                                dialog.show();

                            } else if (arr.length() == 1) {
                                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                    lblTotal.setText("1   "+getString(R.string.address_ar));
                                }else {
                                    lblTotal.setText(String.format(Locale.US, "1 %s", getString(R.string.address)));
                                }


                            } else {
                                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                    lblTotal.setText(""+arr.length()+"   "+ getString(R.string.addresses_ar));
                                }else {
                                    lblTotal.setText(String.format(Locale.US, "%d %s", arr.length(), getString(R.string.addresses)));
                                }

                            }

                        } else {

                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                        }

                    }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_ADDRESS_REQUEST) {

            if (resultCode == RESULT_OK) {

                if (data.hasExtra("address_id")) {

                    ((MyApplication)getActivity().getApplication()).setDeliveryAddressId(data.getStringExtra("address_id"));

                    startActivity(new Intent(getActivity(), CheckoutPaymentActivity.class));

                }
            }
        }
    }


    public void addAddressDialog(String msg) {

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

                AddAddressFragmentFromCheckOut fragment1 = new AddAddressFragmentFromCheckOut();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment1, "AddAddressFragmentFromCheckOut")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                        .commitAllowingStateLoss();

            }
        });

        dialog.show();

    }
}
