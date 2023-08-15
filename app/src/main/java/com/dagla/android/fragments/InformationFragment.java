package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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

public class InformationFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    TextView lblInfo;

    Dialog dlgLoading = null;

    String page_Id,title;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard


        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_information_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_information, container, false);
            }


            lblInfo = (TextView) rootView.findViewById(R.id.lblInfo);
            lblInfo.setText("");

            if (getArguments().getString("page_id")!= null) {
                //
                page_Id = getArguments().getString("page_id");
                title = getArguments().getString("title");
                Log.v("PageId",page_Id);

                ((MainActivity) getActivity()).setHeaders(title,false,false,false,true, false ,"0", false);
            }

            loadData();

        }

        return rootView;

    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());
            params.put("page_id", page_Id);
            client.get(GlobalFunctions.serviceURL + "getStaticPagesData" , params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {

                                String desc_en = ""+arr.getJSONObject(i).get("description");
                                String desc_ar = ""+arr.getJSONObject(i).get("description_ar");

                                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        lblInfo.setText(Html.fromHtml(desc_ar, Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        lblInfo.setText(Html.fromHtml(desc_ar));
                                    }
                                }else {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        lblInfo.setText(Html.fromHtml(desc_en, Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        lblInfo.setText(Html.fromHtml(desc_en));
                                    }
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
}
