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
import com.dagla.android.adapter.CartAdapter;
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

public class CartFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    ListView lv;
    TextView lblTotal;
    Button btnCheckout;

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
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_cart_ar),true,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_cart),true,false,false,true, false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_cart_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_cart, container, false);
            }


            lv = rootView.findViewById(R.id.lv);
            lblTotal = rootView.findViewById(R.id.lblTotal);
            btnCheckout = rootView.findViewById(R.id.btnCheckout);

            TextView lblShoppingBag = (TextView) rootView.findViewById(R.id.lblShoppingBag);
            TextView lblTotalTxt = (TextView) rootView.findViewById(R.id.lblTotalTxt);

            //
            if (GlobalFunctions.getCart().equalsIgnoreCase("")) {
                //
                cartIsEmpty();
                //
            } else {
                //
                loadData();
            }
            //
            btnCheckout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {

                        CheckoutDeliveryFragment fragment = new CheckoutDeliveryFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "CheckoutDeliveryFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();
//
                    } else {

                        LoginFragmentFromCheckOut fragment = new LoginFragmentFromCheckOut();
                        Bundle b = new Bundle();
                        b.putString("returnActivity", "CheckoutDeliveryActivity");


                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "LoginFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();
//
                    }
                }
            });

        }

        return rootView;

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("cartIds", GlobalFunctions.getCart());
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getCart", params, new AsyncHttpResponseHandler() {

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
                            arrAdapter = new CartAdapter(getActivity(), arrList, CartFragment.this);
                            //
                            lv.setAdapter(arrAdapter);
                            //
                            lblTotal.setText(obj.getString("total"));
                            //
                            if (arrList.size() == 0) {
                                //
                                GlobalFunctions.clearCart();
                                //
                                cartIsEmpty();
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

    public void quantityMinus(int pos) {

        boolean canUpdate = false;

        String cartId = "";

        try {

            JSONObject obj = new JSONObject(arrList.get(pos));

            cartId = obj.getString("cart_id");

            int quantity = obj.getInt("quantity");
            double price = obj.getDouble("price_per");

            if (quantity > 1) {

                quantity = quantity - 1;

                price = price * quantity;

                obj.put("quantity", quantity);
                obj.put("price", String.format(Locale.US, "%s %.3f", obj.getString("currency"), price));

                arrList.set(pos, obj.toString());

                arrAdapter.notifyDataSetChanged();

                canUpdate = true;

            }

        } catch (JSONException e) {

            //

        }

        if (canUpdate) {

            if (GlobalFunctions.hasConnection(getActivity())) {

                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();

                params.put("cartId", cartId);
                params.put("cartIds", GlobalFunctions.getCart());
                params.put("ran", GlobalFunctions.getRandom());

                client.get(GlobalFunctions.serviceURL + "delCartItemQuantity", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                        String response = new String(bytes);

                        Log.d("onSuccess", response);

                        JSONObject obj;

                        try {

                            obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                            //
                            if (obj.getString("status").equalsIgnoreCase("1")) {

                                lblTotal.setText(obj.getString("total"));

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

    }

    public void quantityPlus(int pos) {

        boolean canUpdate = false;

        String cartId = "";

        try {

            JSONObject obj = new JSONObject(arrList.get(pos));

            cartId = obj.getString("cart_id");

            int quantity = obj.getInt("quantity");
            double price = obj.getDouble("price_per");

            if (quantity < obj.getInt("max_quantity")) {

                quantity = quantity + 1;

                price = price * quantity;

                obj.put("quantity", quantity);
                obj.put("price", String.format(Locale.US, "%s %.3f", obj.getString("currency"), price));

                arrList.set(pos, obj.toString());

                arrAdapter.notifyDataSetChanged();

                canUpdate = true;

            }

        } catch (JSONException e) {

            //

        }

        if (canUpdate) {

            if (GlobalFunctions.hasConnection(getActivity())) {

                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();

                params.put("cartId", cartId);
                params.put("cartIds", GlobalFunctions.getCart());
                params.put("ran", GlobalFunctions.getRandom());

                client.get(GlobalFunctions.serviceURL + "addCartItemQuantity", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                        String response = new String(bytes);

                        Log.d("onSuccess", response);

                        JSONObject obj;

                        try {

                            obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                            //
                            if (obj.getString("status").equalsIgnoreCase("1")) {

                                lblTotal.setText(obj.getString("total"));

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

    }

    public void delCartItem(int pos) {

        String cartId = "";

        try {

            JSONObject obj = new JSONObject(arrList.get(pos));

            cartId = obj.getString("cart_id");

            GlobalFunctions.deleteFromCart(cartId);

            arrList.remove(pos);

            arrAdapter.notifyDataSetChanged();

            if (arrList.size() == 0) {

                cartIsEmpty();
            }

        } catch (JSONException e) {

            //

        }

        if (GlobalFunctions.hasConnection(getActivity())) {

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("cartId", cartId);
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "delCartItem", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            lblTotal.setText(obj.getString("total"));
                            ((MainActivity) getActivity()).setCartCount();
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

    private void cartIsEmpty() {

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        builder.setMessage(R.string.your_cart_is_empty);
//
//        builder.setPositiveButton(getString(R.string.ok).toUpperCase(), new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int id) {
//
////                finish();
//
//                ((MainActivity) getActivity()).getSupportFragmentManager().popBackStackImmediate();
//            }
//        });
//
//        builder.setCancelable(false);
//
//        AlertDialog dialog = builder.create();
//
//        dialog.show();

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            infoDialog(getString(R.string.your_cart_is_empty_ar));
        }else {
            infoDialog(getString(R.string.your_cart_is_empty));
        }


        GlobalFunctions.clearCart();

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
            okBtn.setText("تم");
        }else {
            okBtn.setText("Ok");
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
