package com.dagla.android.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dagla.android.ColorsAdapter;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.SizesAdapter;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.CartAdapterNew;
import com.dagla.android.adapter.ImagesAdapter;
import com.dagla.android.adapter.ItemsAdapter;
import com.dagla.android.adapter.OrderListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
//import ly.count.android.sdk.Countly;

public class OrdersListFragment extends Fragment {

    View rootView;
//    Context context;
    Bundle savedInstanceState;

    ListView lv;
    TextView lblItemsQty,lblSubTotal,lblDeliveryCharge,lblTotal;

    Dialog dlgLoading = null;

    Dialog return_dialog;
    Dialog exchange_dialog;

    ArrayList<String> arrList;

    ArrayAdapter<String> arrAdapter;

    String order_Id;
    String detailed_Order_Id = "";
    String product_Id = "";
    String refund_To = "W";
    String return_Reason = "";
    String bank_Name = "";
    String account_Number = "";
    String iban_Number = "";
    String account_Holder_Name = "";
    String size_Product_Id = "";
    String color_Product_Id = "";

    ArrayList<String>  arrSizes = new ArrayList<String>();

    ArrayAdapter<String> adapterSizes;

    ArrayList<String>  arrColors = new ArrayList<String>();

    ArrayAdapter<String> adapterColors;

    String exchange_Type = "";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_cart_ar),true,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_cart),true,false,false,true, false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_orders_list_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_orders_list, container, false);
            }

            if (getArguments().getString("order_id")!= null) {

                order_Id = getArguments().getString("order_id");

            }


            lv = rootView.findViewById(R.id.lv);

            lblItemsQty = rootView.findViewById(R.id.lblItemsQty);
            lblSubTotal = rootView.findViewById(R.id.lblSubTotal);
            lblDeliveryCharge = rootView.findViewById(R.id.lblDeliveryCharge);
            lblTotal = rootView.findViewById(R.id.lblTotal);


//            loadData();
            loadData2();


        }

        return rootView;

    }


    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

//            http://portal.dagla.com/services/ajax_v1.aspx?app=ios&lang=en&ver=1.0&cat=getMyOrdersDetails&orderId=If0aYb9Nf3I=&ran=71

//            params.put("cartIds", "VgP9oNPGWnA=");
            params.put("orderId", order_Id);
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

            client.get(GlobalFunctions.serviceURL + "getMyOrdersDetails", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj,obj1;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {


                            String data = obj.getString("data");

                            obj1 = new JSONObject(data);

                            String order_id = obj1.getString("order_id");
                            String order_id_custom = obj1.getString("order_id_custom");

                            lblItemsQty.setText(" "+obj1.getString("items_count"));
                            lblSubTotal.setText(" "+obj1.getString("sub_total"));
                            lblDeliveryCharge.setText(" "+obj1.getString("delivery_charges"));
                            lblTotal.setText(" "+obj1.getString("total_price"));
                            //
                            arr = obj1.getJSONArray("Products");
                            //
                            arrList = new ArrayList<String>();
                            //
                            for (int i = 0; i < arr.length(); i++) {
                                //
                                arrList.add(arr.getJSONObject(i).toString());
                            }
//                            //
                            arrAdapter = new OrderListAdapter(getActivity(), arrList, OrdersListFragment.this);
//                            //
                            lv.setAdapter(arrAdapter);



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

//            http://portal.dagla.com/services/ajax_v1.aspx?app=ios&lang=en&ver=1.0&cat=getMyOrdersDetails&orderId=If0aYb9Nf3I=&ran=71

//            params.put("orderId", order_Id);
//            params.put("ran", GlobalFunctions.getRandom());

            params.put("orderId", order_Id);
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

//            client.get("https://portal.dagla.com/Staging/services/ajax_v2.aspx?cat=getMyOrdersDetails", params, new AsyncHttpResponseHandler() {

            client.get(GlobalFunctions.serviceURL + "getMyOrdersDetails", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj,obj1;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {


                            String data = obj.getString("data");

                            obj1 = new JSONObject(data);

                            String order_id = obj1.getString("order_id");
                            String order_id_custom = obj1.getString("order_id_custom");

                            lblItemsQty.setText(" "+obj1.getString("items_count"));
                            lblSubTotal.setText(" "+obj1.getString("sub_total"));
                            lblDeliveryCharge.setText(" "+obj1.getString("delivery_charges"));
                            lblTotal.setText(" "+obj1.getString("total_price"));
                            //
                            arr = obj1.getJSONArray("Products");
                            //
                            arrList = new ArrayList<String>();
                            //
                            for (int i = 0; i < arr.length(); i++) {
                                //
                                arrList.add(arr.getJSONObject(i).toString());
                            }
//
                            if(arrAdapter==null){
                                arrAdapter = new OrderListAdapter(getActivity(), arrList, OrdersListFragment.this);

                                lv.setAdapter(arrAdapter);
                            }else {
                                arrAdapter.notifyDataSetChanged();
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






    public void returnsDialog(Activity context, String order_id) {

        return_dialog = new Dialog(context);
        return_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            return_dialog.setContentView(R.layout.returns_dialog_box_ar);
        }else {
            return_dialog.setContentView(R.layout.returns_dialog_box);
        }

        WindowManager.LayoutParams layoutParams = return_dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        return_dialog.getWindow().setAttributes(layoutParams);
        return_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return_dialog.setCancelable(false);
        return_dialog.setCanceledOnTouchOutside(false);

        final EditText reasonEditText = (EditText) return_dialog.findViewById(R.id.reasonEditText);
        final ImageView imgWallet = (ImageView) return_dialog.findViewById(R.id.imgWallet);
        final ImageView imgBank = (ImageView) return_dialog.findViewById(R.id.imgBank);

        final Button btnYes = (Button) return_dialog.findViewById(R.id.btnYes);
        final Button btnNo = (Button) return_dialog.findViewById(R.id.btnNo);

        final LinearLayout bankDetailsLayout = (LinearLayout) return_dialog.findViewById(R.id.bankDetailsLayout);

        detailed_Order_Id = order_id;

//        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//
//        }else {
//
//        }

        imgWallet.setBackgroundResource(R.drawable.radio_btn_2);
        imgBank.setBackgroundResource(R.drawable.radio_btn_1);
        bankDetailsLayout.setVisibility(View.GONE);

        imgWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                dialog.dismiss();

                refund_To = "W";
                imgWallet.setBackgroundResource(R.drawable.radio_btn_2);
                imgBank.setBackgroundResource(R.drawable.radio_btn_1);
                bankDetailsLayout.setVisibility(View.GONE);

            }
        });

        imgBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                dialog.dismiss();
                refund_To = "B";
                imgWallet.setBackgroundResource(R.drawable.radio_btn_1);
                imgBank.setBackgroundResource(R.drawable.radio_btn_2);
                bankDetailsLayout.setVisibility(View.VISIBLE);

            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                return_Reason = reasonEditText.getText().toString();

//                return_dialog.dismiss();
                returnTask();

            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                return_dialog.dismiss();
            }
        });

        return_dialog.show();
    }


    public void exchangeDialog(Activity context, String order_id, String product_id) {

        exchange_dialog = new Dialog(context);
        exchange_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            exchange_dialog.setContentView(R.layout.exchange_dialog_box_ar);
        }else {
            exchange_dialog.setContentView(R.layout.exchange_dialog_box);
        }

        WindowManager.LayoutParams layoutParams = exchange_dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        exchange_dialog.getWindow().setAttributes(layoutParams);
        exchange_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        exchange_dialog.setCancelable(false);
        exchange_dialog.setCanceledOnTouchOutside(false);


        final RelativeLayout sizeLayout = (RelativeLayout) exchange_dialog.findViewById(R.id.sizeLayout);
        final RelativeLayout colorLayout = (RelativeLayout) exchange_dialog.findViewById(R.id.colorLayout);

        final TextView lblSize = (TextView) exchange_dialog.findViewById(R.id.lblSize);
        final TextView lblColor = (TextView) exchange_dialog.findViewById(R.id.lblColor);

        final Button btnYes = (Button) exchange_dialog.findViewById(R.id.btnYes);
        final Button btnNo = (Button) exchange_dialog.findViewById(R.id.btnNo);


        detailed_Order_Id = order_id;
        product_Id = product_id;
        size_Product_Id = "";
//        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//
//        }else {
//
//        }

        if(arrSizes.size()>1){
            sizeLayout.setVisibility(View.VISIBLE);

        }else {
            sizeLayout.setVisibility(View.GONE);
        }

        if(arrColors.size()>1){
            colorLayout.setVisibility(View.VISIBLE);
        }else {
            colorLayout.setVisibility(View.GONE);
        }


        sizeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                dialog.dismiss();

                if(arrSizes.size()>1){

                    exchange_Type = "S";

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        builder.setTitle(R.string.size_ar);
                    }else {
                        builder.setTitle(R.string.size);
                    }


                    builder.setAdapter(adapterSizes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {

                                JSONObject obj = new JSONObject(arrSizes.get(which));

                                lblSize.setText(obj.getString("size_name"));
                                size_Product_Id = obj.getString("product_id");
                                dialog.dismiss();


                            } catch (JSONException e) {

                                Log.d("JSONException", e.getMessage());
                            }

                        }
                    });

                    builder.show();

                }else {
                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        infoDialog(getString(R.string.no_sizes_found_ar));
                    }else {
                        infoDialog(getString(R.string.no_sizes_found));
                    }
                }


            }
        });

        colorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                dialog.dismiss();
                if(arrColors.size()>1){

                    exchange_Type = "C";

                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        builder.setTitle(R.string.color_ar);
                    }else {
                        builder.setTitle(R.string.color);
                    }


                    builder.setAdapter(adapterColors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            try {

                                JSONObject obj = new JSONObject(arrColors.get(which));

                                lblColor.setText(obj.getString("color_name"));
                                color_Product_Id = obj.getString("product_id");
                                dialog.dismiss();


                            } catch (JSONException e) {

                                Log.d("JSONException", e.getMessage());
                            }

                        }
                    });

                    builder.show();

                }else {
                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        infoDialog(getString(R.string.no_colors_found_ar));
                    }else {
                        infoDialog(getString(R.string.no_colors_found));
                    }
                }

            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                exchange_dialog.dismiss();

//                if(arrSizes.size()>1){

                if(lblColor.getText().toString().equals("")&&lblSize.getText().toString().equals("")){
                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        infoDialog(getString(R.string.please_select_the_size_or_color_ar));
                    }else {
                        infoDialog(getString(R.string.please_select_the_size_or_color));
                    }
                    return;
                }

                    exchangeTask();
//                }else {
//                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//                        infoDialog(getString(R.string.no_sizes_found_ar));
//                    }else {
//                        infoDialog(getString(R.string.no_sizes_found));
//                    }
//                }


            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exchange_dialog.dismiss();
            }
        });



        exchange_dialog.show();

        sizes();
    }


    private void returnTask() {

//        if (return_Reason.equals("")) {
//
//
//        }

        if(return_Reason.equals("")){
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                infoDialog(getString(R.string.please_specify_your_reason_for_product_returns_ar_));
            }else {
                infoDialog(getString(R.string.please_specify_your_reason_for_product_returns_));
            }
            return;
        }

        if(refund_To.equals("B")){

            if(bank_Name.equals("")){
                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                    infoDialog(getString(R.string.please_enter_bank_name_ar));
                }else {
                    infoDialog(getString(R.string.please_enter_bank_name));
                }
                return;
            }

            if(account_Number.equals("")){
                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                    infoDialog(getString(R.string.please_enter_account_number_ar));
                }else {
                    infoDialog(getString(R.string.please_enter_account_number));
                }
                return;
            }

            if(iban_Number.equals("")){
                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                    infoDialog(getString(R.string.please_enter_iban_number_ar));
                }else {
                    infoDialog(getString(R.string.please_enter_iban_number));
                }
                return;
            }

            if(account_Holder_Name.equals("")){
                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                    infoDialog(getString(R.string.please_enter_account_holder_name_ar));
                }else {
                    infoDialog(getString(R.string.please_enter_account_holder_name));
                }
                return;
            }
        }


        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("rettype", refund_To);
            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("orderID", detailed_Order_Id);
            params.put("remark", return_Reason);
            params.put("ran", GlobalFunctions.getRandom());

            if(refund_To.equals("B")){
                params.put("bankname", bank_Name);
                params.put("accountno", account_Number);
                params.put("iban", iban_Number);
                params.put("holdername", account_Holder_Name);
            }

//            client.get("https://portal.dagla.com/Staging/services/ajax_v2.aspx?cat=returnOrder", params, new AsyncHttpResponseHandler() {

            client.get(GlobalFunctions.serviceURL + "returnOrder", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                    //



                    hideLoading();

                    return_dialog.dismiss();

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

                            successDialog(obj.getString("msg"));

                        } else {

                            errorDialog(obj.getString("msg"));

                        }
                        //


                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));

//                    } catch (ClassNotFoundException e) {

                        //
                    }
                    //
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                }
            });

        } else {

            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));

        }

    }


    public void infoDialog(String msg) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.info_dailog_box);
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
            okBtn.setText(getString(R.string.ok_ar));
        }else {
            okBtn.setText(getString(R.string.ok));
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();

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

                loadData2();


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


    private void sizes() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            arrSizes.clear();
            arrColors.clear();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();


            params.put("orderID", detailed_Order_Id);
            params.put("productId", product_Id);
            params.put("ran", GlobalFunctions.getRandom());

//            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
//                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
//            }

//            client.get("https://portal.dagla.com/Staging/services/ajax_v2.aspx?cat=getExchangeBy", params, new AsyncHttpResponseHandler() {


            client.get(GlobalFunctions.serviceURL + "getExchangeBy", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr,arr1;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            String lang = GlobalFunctions.getLang(getActivity());

                            String orderid = obj.getString("orderid");

                            arr = obj.getJSONArray("sizes");

//                            arrSizes = new ArrayList<String>();

                            if (arr.length() > 0) {

                                for (int i=0; i<arr.length(); i++) {

                                    arrSizes.add(arr.getJSONObject(i).toString());

                                }
                            }

                            adapterSizes = new SizesAdapter(getActivity(), arrSizes);

                            arr1 = obj.getJSONArray("colors");

//                            arrSizes = new ArrayList<String>();

                            if (arr1.length() > 0) {

                                for (int i=0; i<arr1.length(); i++) {

                                    arrColors.add(arr1.getJSONObject(i).toString());

                                }
                            }

                            adapterColors = new ColorsAdapter(getActivity(), arrColors);

                        } else {

//                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

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


    private void exchangeTask() {


        if(exchange_Type.equals("S")&&size_Product_Id.equals("")){
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                infoDialog(getString(R.string.please_select_the_size_ar));
            }else {
                infoDialog(getString(R.string.please_select_the_size));
            }
            return;
        }

        if(exchange_Type.equals("C")&&color_Product_Id.equals("")){
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                infoDialog(getString(R.string.please_select_the_color_ar));
            }else {
                infoDialog(getString(R.string.please_select_the_color));
            }
            return;
        }



        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            https://portal.dagla.com/Staging/services/ajax_v2.aspx?cat=exchangeOrder&orderID=C/c4nW0E4Q4=
            // &userId=7yfr6aCwW0c=&exctype=S&productID=EAl4hWoeP+U=&ran=1

            params.put("exctype", exchange_Type);
            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("orderID", detailed_Order_Id);
            if(exchange_Type.equals("S")){
                params.put("productID", size_Product_Id);
            }else {
                params.put("productID", color_Product_Id);
            }
            params.put("ran", GlobalFunctions.getRandom());


//            client.get("https://portal.dagla.com/Staging/services/ajax_v2.aspx?cat=exchangeOrder", params, new AsyncHttpResponseHandler() {

            client.get(GlobalFunctions.serviceURL + "exchangeOrder", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    exchange_dialog.dismiss();

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

                            successDialog(obj.getString("msg"));

                        } else {

                            errorDialog(obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));

//                    } catch (ClassNotFoundException e) {

                        //
                    }
                    //
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_error));
                }
            });

        } else {

            GlobalFunctions.showToastError(getActivity(), getString(R.string.msg_no_internet));

        }

    }
}
