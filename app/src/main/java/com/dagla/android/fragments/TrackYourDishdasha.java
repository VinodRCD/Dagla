package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class TrackYourDishdasha extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    EditText orderNoEdit;
    ImageView img1,img2,img3,img4,img5,img6;
    View viewLeft1,viewLeft2,viewLeft3,viewLeft4,viewLeft5,viewLeft6;
    View viewRight1,viewRight2,viewRight3,viewRight4,viewRight5,viewRight6;
    TextView lblOrderNumber,lblOrderDate,lblOrderStatus;
    ImageView statusImg;
    LinearLayout linearLayout;
    TextView lblOrderFound;

    Button btnTrack;

    Dialog dlgLoading = null;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.order_tracking_ar),false,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.order_tracking),false,false,false,true, false ,"0", false);
        }


        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_track_your_dishdasha_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_track_your_dishdasha, container, false);
            }

            orderNoEdit = (EditText) rootView.findViewById(R.id.orderNoEdit);

            img1 = (ImageView) rootView.findViewById(R.id.img1);
            img2 = (ImageView) rootView.findViewById(R.id.img2);
            img3 = (ImageView) rootView.findViewById(R.id.img3);
            img4 = (ImageView) rootView.findViewById(R.id.img4);
            img5 = (ImageView) rootView.findViewById(R.id.img5);
//            img6 = (ImageView) rootView.findViewById(R.id.img6);

            viewLeft1 = (View) rootView.findViewById(R.id. viewLeft1);
            viewLeft2 = (View) rootView.findViewById(R.id. viewLeft2);
            viewLeft3 = (View) rootView.findViewById(R.id. viewLeft3);
            viewLeft4 = (View) rootView.findViewById(R.id. viewLeft4);
            viewLeft5 = (View) rootView.findViewById(R.id. viewLeft5);
//            viewLeft6 = (View) rootView.findViewById(R.id. viewLeft6);

            viewRight1 = (View) rootView.findViewById(R.id. viewRight1);
            viewRight2 = (View) rootView.findViewById(R.id. viewRight2);
            viewRight3 = (View) rootView.findViewById(R.id. viewRight3);
            viewRight4 = (View) rootView.findViewById(R.id. viewRight4);
            viewRight5 = (View) rootView.findViewById(R.id. viewRight5);
//            viewRight6 = (View) rootView.findViewById(R.id. viewRight6);

            lblOrderNumber = (TextView) rootView.findViewById(R.id.lblOrderNumber);
            lblOrderDate = (TextView) rootView.findViewById(R.id.lblOrderDate);
            lblOrderStatus = (TextView) rootView.findViewById(R.id.lblOrderStatus);

            statusImg = (ImageView) rootView.findViewById(R.id.statusImg);
            btnTrack = (Button) rootView.findViewById(R.id.btnTrack);

            lblOrderFound = (TextView) rootView.findViewById(R.id.lblOrderFound);
            linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);

            status0();



            btnTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    orderTracking();

                }
            });


        }

        return rootView;

    }

    public void status0(){

        img1.setBackgroundResource(R.drawable.blank_icon);
        img2.setBackgroundResource(R.drawable.blank_icon);
        img3.setBackgroundResource(R.drawable.blank_icon);
        img4.setBackgroundResource(R.drawable.blank_icon);
        img5.setBackgroundResource(R.drawable.blank_icon);
//        img6.setBackgroundResource(R.drawable.blank_icon);


        viewLeft1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewLeft2.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft3.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft5.setBackgroundColor(Color.parseColor("#CACACA"));
//        viewLeft6.setBackgroundColor(Color.parseColor("#CACACA"));

        viewRight1.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight2.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight3.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        viewRight6.setBackgroundColor(Color.parseColor("#FFFFFF"));

        statusImg.setBackgroundResource(R.drawable.cutting_icon);



//        lblOrderNumber.setText("");
//        lblOrderDate.setText("");
//        lblOrderStatus.setText("");

        linearLayout.setVisibility(View.GONE);
        lblOrderFound.setVisibility(View.GONE);

    }

    public void status1(){

        img1.setBackgroundResource(R.drawable.right_icon);
        img2.setBackgroundResource(R.drawable.blank_icon);
        img3.setBackgroundResource(R.drawable.blank_icon);
        img4.setBackgroundResource(R.drawable.blank_icon);
        img5.setBackgroundResource(R.drawable.blank_icon);
//        img6.setBackgroundResource(R.drawable.blank_icon);


        viewLeft1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewLeft2.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft3.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft5.setBackgroundColor(Color.parseColor("#CACACA"));
//        viewLeft6.setBackgroundColor(Color.parseColor("#CACACA"));

        viewRight1.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight2.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight3.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        viewRight6.setBackgroundColor(Color.parseColor("#FFFFFF"));

        statusImg.setBackgroundResource(R.drawable.cutting_icon);

//        lblOrderNumber.setText("");
//        lblOrderDate.setText("");
//        lblOrderStatus.setText(getResources().getString(R.string.cutting_department));

//        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//            lblOrderStatus.setText(getResources().getString(R.string.cutting_department_ar));
//        }else {
//            lblOrderStatus.setText(getResources().getString(R.string.cutting_department));
//        }


        linearLayout.setVisibility(View.VISIBLE);
        lblOrderFound.setVisibility(View.GONE);

    }

    public void status2(){

        img1.setBackgroundResource(R.drawable.right_icon);
        img2.setBackgroundResource(R.drawable.right_icon);
        img3.setBackgroundResource(R.drawable.blank_icon);
        img4.setBackgroundResource(R.drawable.blank_icon);
        img5.setBackgroundResource(R.drawable.blank_icon);
//        img6.setBackgroundResource(R.drawable.blank_icon);


        viewLeft1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewLeft2.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft3.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft5.setBackgroundColor(Color.parseColor("#CACACA"));
//        viewLeft6.setBackgroundColor(Color.parseColor("#CACACA"));

        viewRight1.setBackgroundColor(Color.parseColor("#222222"));
        viewRight2.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight3.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        viewRight6.setBackgroundColor(Color.parseColor("#FFFFFF"));

        statusImg.setBackgroundResource(R.drawable.tailoring);

//        lblOrderNumber.setText("");
//        lblOrderDate.setText("");
//        lblOrderStatus.setText(getResources().getString(R.string.tailoring_department));

//        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//            lblOrderStatus.setText(getResources().getString(R.string.tailoring_department_ar));
//        }else {
//            lblOrderStatus.setText(getResources().getString(R.string.tailoring_department));
//        }

        linearLayout.setVisibility(View.VISIBLE);
        lblOrderFound.setVisibility(View.GONE);
    }

    public void status3(){

        img1.setBackgroundResource(R.drawable.right_icon);
        img2.setBackgroundResource(R.drawable.right_icon);
        img3.setBackgroundResource(R.drawable.right_icon);
        img4.setBackgroundResource(R.drawable.blank_icon);
        img5.setBackgroundResource(R.drawable.blank_icon);
//        img6.setBackgroundResource(R.drawable.blank_icon);


        viewLeft1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewLeft2.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft3.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewLeft5.setBackgroundColor(Color.parseColor("#CACACA"));
//        viewLeft6.setBackgroundColor(Color.parseColor("#CACACA"));

        viewRight1.setBackgroundColor(Color.parseColor("#222222"));
        viewRight2.setBackgroundColor(Color.parseColor("#222222"));
        viewRight3.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        viewRight6.setBackgroundColor(Color.parseColor("#FFFFFF"));

        statusImg.setBackgroundResource(R.drawable.quality_icon);

//        lblOrderNumber.setText("");
//        lblOrderDate.setText("");
//        lblOrderStatus.setText(getResources().getString(R.string.quality_control_finishing));

//        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//            lblOrderStatus.setText(getResources().getString(R.string.quality_control_finishing_ar));
//        }else {
//            lblOrderStatus.setText(getResources().getString(R.string.quality_control_finishing));
//        }

        linearLayout.setVisibility(View.VISIBLE);
        lblOrderFound.setVisibility(View.GONE);

    }

    public void status4(){

        img1.setBackgroundResource(R.drawable.right_icon);
        img2.setBackgroundResource(R.drawable.right_icon);
        img3.setBackgroundResource(R.drawable.right_icon);
        img4.setBackgroundResource(R.drawable.right_icon);
        img5.setBackgroundResource(R.drawable.blank_icon);
//        img6.setBackgroundResource(R.drawable.blank_icon);
//

        viewLeft1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewLeft2.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft3.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft4.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft5.setBackgroundColor(Color.parseColor("#CACACA"));
//        viewLeft6.setBackgroundColor(Color.parseColor("#CACACA"));

        viewRight1.setBackgroundColor(Color.parseColor("#222222"));
        viewRight2.setBackgroundColor(Color.parseColor("#222222"));
        viewRight3.setBackgroundColor(Color.parseColor("#222222"));
        viewRight4.setBackgroundColor(Color.parseColor("#CACACA"));
        viewRight5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        viewRight6.setBackgroundColor(Color.parseColor("#FFFFFF"));

        statusImg.setBackgroundResource(R.drawable.dispatched_icon);

//        lblOrderNumber.setText("");
//        lblOrderDate.setText("");
//        lblOrderStatus.setText(getResources().getString(R.string.being_dispatched));

//        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//            lblOrderStatus.setText(getResources().getString(R.string.being_dispatched_ar));
//        }else {
//            lblOrderStatus.setText(getResources().getString(R.string.being_dispatched));
//        }


        linearLayout.setVisibility(View.VISIBLE);
        lblOrderFound.setVisibility(View.GONE);

    }

    public void status5(){

        img1.setBackgroundResource(R.drawable.right_icon);
        img2.setBackgroundResource(R.drawable.right_icon);
        img3.setBackgroundResource(R.drawable.right_icon);
        img4.setBackgroundResource(R.drawable.right_icon);
        img5.setBackgroundResource(R.drawable.right_icon);
//        img6.setBackgroundResource(R.drawable.blank_icon);


        viewLeft1.setBackgroundColor(Color.parseColor("#FFFFFF"));
        viewLeft2.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft3.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft4.setBackgroundColor(Color.parseColor("#222222"));
        viewLeft5.setBackgroundColor(Color.parseColor("#222222"));
//        viewLeft6.setBackgroundColor(Color.parseColor("#CACACA"));

        viewRight1.setBackgroundColor(Color.parseColor("#222222"));
        viewRight2.setBackgroundColor(Color.parseColor("#222222"));
        viewRight3.setBackgroundColor(Color.parseColor("#222222"));
        viewRight4.setBackgroundColor(Color.parseColor("#222222"));
        viewRight5.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        viewRight6.setBackgroundColor(Color.parseColor("#FFFFFF"));

        statusImg.setBackgroundResource(R.drawable.dispatched_icon);


//        lblOrderNumber.setText("");
//        lblOrderDate.setText("");
//        lblOrderStatus.setText(getResources().getString(R.string.delivered));

//        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
//            lblOrderStatus.setText(getResources().getString(R.string.delivered_ar));
//        }else {
//            lblOrderStatus.setText(getResources().getString(R.string.delivered));
//        }

        linearLayout.setVisibility(View.VISIBLE);
        lblOrderFound.setVisibility(View.GONE);
    }

//    public void status6(){
//
//        img1.setBackgroundResource(R.drawable.right_icon);
//        img2.setBackgroundResource(R.drawable.right_icon);
//        img3.setBackgroundResource(R.drawable.right_icon);
//        img4.setBackgroundResource(R.drawable.right_icon);
//        img5.setBackgroundResource(R.drawable.right_icon);
////        img6.setBackgroundResource(R.drawable.right_icon);
//
//
//        viewLeft1.setBackgroundColor(Color.parseColor("#FFFFFF"));
//        viewLeft2.setBackgroundColor(Color.parseColor("#222222"));
//        viewLeft3.setBackgroundColor(Color.parseColor("#222222"));
//        viewLeft4.setBackgroundColor(Color.parseColor("#222222"));
//        viewLeft5.setBackgroundColor(Color.parseColor("#222222"));
////        viewLeft6.setBackgroundColor(Color.parseColor("#222222"));
//
//        viewRight1.setBackgroundColor(Color.parseColor("#222222"));
//        viewRight2.setBackgroundColor(Color.parseColor("#222222"));
//        viewRight3.setBackgroundColor(Color.parseColor("#222222"));
//        viewRight4.setBackgroundColor(Color.parseColor("#222222"));
//        viewRight5.setBackgroundColor(Color.parseColor("#222222"));
////        viewRight6.setBackgroundColor(Color.parseColor("#FFFFFF"));
//
//        statusImg.setBackgroundResource(R.drawable.dispatched_icon);
//
//        linearLayout.setVisibility(View.VISIBLE);
//        lblOrderFound.setVisibility(View.GONE);
//
//    }


    private void orderTracking() {

        if (GlobalFunctions.isEmptyText(orderNoEdit)) {
            orderNoEdit.requestFocus();
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                orderNoEdit.setError(getString(R.string.enter_invoice_number_ar));
            }else {
                orderNoEdit.setError(getString(R.string.enter_invoice_number));
            }

            return;
        }

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("search_keyword", orderNoEdit.getText().toString());

            client.get("https://www.invoice.dagla.com/orders/fetch?" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = new JSONObject(response);

                        if (obj.getString("status").equalsIgnoreCase("ok")) {



                            obj1 = new JSONObject(obj.getString("data"));
                            obj2 = new JSONObject(obj1.getString("order"));

                            String order_no = obj2.getString("order_no");
                            String order_status = obj2.getString("order_status");
                            String ordered_at = obj2.getString("ordered_at");
                            String tailoring_status = obj2.getString("tailoring_status");
                            String tailoring_status_value = obj2.getString("tailoring_status_value");
                            String tailoring_status_value_ar = obj2.getString("tailoring_status_value_ar");

//                            String new_desc_ar = obj.getString("RQDishNewDescAR");
//                            String ex_thanks_en = obj.getString("RQDishExThanksEN");
//                            String ex_thanks_ar = obj.getString("RQDishExThanksAR");
//                            String new_thanks_en = obj.getString("RQDishNewThanksEN");
//                            String new_thanks_ar = obj.getString("RQDishNewThanksAR");

                            String[] separated = ordered_at.split(" ");
//                            separated[0]; // this will contain "Fruit"
//                            separated[1]; // this will contain " they taste good"

                            lblOrderNumber.setText(order_no);
                            lblOrderDate.setText(separated[0]);

                            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                lblOrderStatus.setText(tailoring_status_value_ar);
                            }else {
                                lblOrderStatus.setText(tailoring_status_value);
                            }


                            if(tailoring_status.equals("1")){
                                status1();
                            }else if(tailoring_status.equals("2")){
                                status2();
                            }else if(tailoring_status.equals("3")){
                                status3();
                            }else if(tailoring_status.equals("4")){
                                status3();
                            }else if(tailoring_status.equals("5")){
                                status4();
                            }else if(tailoring_status.equals("6")){
                                status5();
                            }



                        } else if(obj.getString("status").equalsIgnoreCase("error"))  {

                            linearLayout.setVisibility(View.GONE);
                            lblOrderFound.setVisibility(View.VISIBLE);

//                            GlobalFunctions.showToastError(getActivity(), obj.getString("msg"));

                        }else  {

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
}
