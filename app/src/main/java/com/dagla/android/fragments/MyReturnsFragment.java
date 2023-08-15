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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.OrdersAdapter;
import com.dagla.android.adapter.ReturnsAdapter;
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

public class MyReturnsFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    ListView lv;

    Dialog dlgLoading = null;

    ArrayList<String> arrList;

    ArrayAdapter<String> arrAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.my_returns_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.my_returns),false,false,false,true, false ,"0", false);
        }


        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_returns_list, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_returns_list, container, false);
            }

            lv = (ListView)rootView.findViewById(R.id.lv);


//            lblTotalTxt.setTypeface(custom_fontnormal);
//            lblTotal.setTypeface(custom_fontbold);

//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//
//                    arg1.setEnabled(false);
//
//                    try {
//
//                        JSONObject obj = new JSONObject(lv.getItemAtPosition(position).toString());
//
////                        Intent intent = new Intent(getActivity(), OrderReceiptActivity.class);
////
////                        intent.putExtra("order_id", obj.getString("order_id"));
////
////                        startActivity(intent);
//
////                        OrderReceiptFragment fragment = new OrderReceiptFragment();
//                        OrdersListFragment fragment = new OrdersListFragment();
//                        Bundle b = new Bundle();
//                        b.putString("order_id", obj.getString("order_id"));
//
//                        fragment.setArguments(b);
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.fragment_container, fragment, "OrderReceiptFragment")
//                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                .addToBackStack(null)
//                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                                .commitAllowingStateLoss();
//
//                    } catch (JSONException e) {
//
//                        Log.d("JSONException", e.getMessage());
//                    }
//
//                    arg1.setEnabled(true);
//
//                }
//            });
            //
            loadData();

        }

        return rootView;

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

//            client.get("https://portal.dagla.com/Staging/services/ajax_v2.aspx?cat=getMyReturns", params, new AsyncHttpResponseHandler() {

            client.get(GlobalFunctions.serviceURL + "getMyReturns", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    if(!response.equals("")){

                        JSONArray arr;
                        JSONObject obj;

                        try {

                            obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                            //
                            if (obj.getString("status").equalsIgnoreCase("1")) {
                                //
                                arr = obj.getJSONArray("Products");
                                //
                                arrList = new ArrayList<String>();
                                //
                                for (int i = 0; i < arr.length(); i++) {

                                    arrList.add(arr.getJSONObject(i).toString());
                                }
                                //
                                arrAdapter = new ReturnsAdapter(getActivity(), arrList);
                                //
                                lv.setAdapter(arrAdapter);
                                //
                                if (arrList.size() == 0) {

//                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                                builder.setMessage(R.string.your_orders_history_is_empty);
//
//                                builder.setPositiveButton(getString(R.string.ok).toUpperCase(), new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//
////                                        getActivity().finish();
//                                        ((MainActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
//
//                                    }
//                                });
//
//                                builder.setCancelable(false);
//
//                                AlertDialog dialog = builder.create();
//
//                                dialog.show();
                                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                        infoDialog(getString(R.string.your_orders_history_is_empty_ar));
                                    }else {
                                        infoDialog(getString(R.string.your_orders_history_is_empty));
                                    }


                                } else {



                                }

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
                ((MainActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();

            }
        });

        dialog.show();

    }
}
