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
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.CountriesAdapter;
import com.dagla.android.parser.CountriesDetails;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CountriesFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    ListView countriesListView;

    Dialog dlgLoading = null;

    String page_Id,title;


    CountriesDetails countryDetails;
    ArrayList<CountriesDetails> countries_arraylist = new ArrayList<CountriesDetails>();

    CountriesAdapter countriesAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard



        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.select_country_ar),true,false,false,true, false ,"0", true);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.select_country),true,false,false,true, false ,"0", true);
        }

        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.countries_fragment, container, false);
            }else {
                rootView = inflater.inflate(R.layout.countries_fragment, container, false);
            }


            countriesListView = (ListView) rootView.findViewById(R.id.countriesListView);
//
//            String[] country_id = {"1","2","3","4","5","6"};
//            String[] country_name = {"Kuwait","Saudi Arabia","Qatar","Bahrain","Oman", "United Arab Emirates"};
//            Integer[] flags = {R.drawable.flag_kuwait,R.drawable.flag_saudi_arabia,R.drawable.flag_qatar,R.drawable.flag_bahrain,R.drawable.flag_oman,R.drawable.flag_united_arab_emirates};



            loadData();



//            loadData();

        }

        return rootView;

    }



    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

//            countries_arraylist.clear();

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getCurrencyList", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");

                            for (int i = 0; i < arr.length(); i++) {

                                String country_id = ""+arr.getJSONObject(i).get("country_id");
                                String country_code = ""+arr.getJSONObject(i).get("country_code");
                                String country_name_en = ""+arr.getJSONObject(i).get("country_name_en");
                                String country_name_ar = ""+arr.getJSONObject(i).get("country_name_ar");
                                String country_currency = ""+arr.getJSONObject(i).get("country_currency");
                                String country_icon = ""+arr.getJSONObject(i).get("country_icon");

                                countryDetails = new CountriesDetails(country_id,country_code,country_name_en,country_name_ar, country_currency,country_icon);

                                countries_arraylist.add(countryDetails);

                            }


                            countriesAdapter = new CountriesAdapter(getActivity(), countries_arraylist);
                            countriesListView.setAdapter(countriesAdapter);

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
