package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.CategoriesAdapter;
import com.dagla.android.adapter.CategoriesAdapterNew;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class CategoriesFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    ListView lv;

//    BaseAdapter arrAdapter;

//    ArrayList arrImages;
//    ArrayList arrNames;

    ArrayAdapter<String> arrAdapter;

    Dialog dlgLoading = null;
    ArrayList<String> arrList;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        context = getActivity();

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_categories_ar),true,true,true,false, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_categories),true,true,true,false, false ,"0", false);
        }

        GlobalFunctions.initImageLoader(context);

        if (rootView == null) {
//            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_categories, container, false);
//            }else {
//                rootView = inflater.inflate(R.layout.fragment_categories, container, false);
//            }


            lv = rootView.findViewById(R.id.lv);

//            arrImages = new ArrayList<Integer>();
//
//            arrImages.add(R.drawable.categories_banners_1);
//            arrImages.add(R.drawable.categories_banners_2);
//            arrImages.add(R.drawable.categories_banners_3);
//            arrImages.add(R.drawable.categories_banners_4);
//
//            arrNames = new ArrayList<String>();
//
//            arrNames.add("Clothing");
//            arrNames.add("Accessories");
//            arrNames.add("Footwear");
//            arrNames.add("Baggage");

//            DisplayMetrics dm = new DisplayMetrics();
//
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            arrAdapter = new CategoriesAdapter(getActivity(), arrNames, arrImages, dm.widthPixels);
//
//            lv.setAdapter(arrAdapter);

            loadData();

        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                arg1.setEnabled(false);

                try {

                    JSONObject obj = new JSONObject(lv.getItemAtPosition(position).toString());

                    if (!obj.getString("cat_id").equalsIgnoreCase("")) {

                        SubCategoriesFragment fragment = new SubCategoriesFragment();
                        Bundle b = new Bundle();
                        b.putString("cat_id", obj.getString("cat_id"));
                        if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
                            b.putString("title", obj.getString("banner_name_ar"));
                        }else{
                            b.putString("title", obj.getString("banner_name"));
                        }

                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "SubCategoriesFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();
                    }

                } catch (JSONException e) {

                    Log.d("JSONException", e.getMessage());
                }

                arg1.setEnabled(true);

            }
        });


        return rootView;

    }


    private void loadData() {

        if (GlobalFunctions.hasConnection(context)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getCategoryData", params, new AsyncHttpResponseHandler() {

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

                            arr = obj.getJSONArray("banners");

                            arrList = new ArrayList<String>();

                            for (int i = 0; i < arr.length(); i++) {

                                arrList.add(arr.getJSONObject(i).toString());
                            }

//                            DisplayMetrics dm = new DisplayMetrics();

//                            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

//                            arrAdapter = new CategoriesAdapterNew(getActivity(), arrList, CategoriesFragment.this);
                            arrAdapter = new CategoriesAdapter(getActivity(), arrList);

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

    public void showLoading() {

        if (dlgLoading == null) {

            dlgLoading = GlobalFunctions.showLoading(getActivity());

        } else {

            dlgLoading.show();
        }

    }

    public void hideLoading() {

        dlgLoading.dismiss();

    }
}


//Intro 750*1334

//Home Banners 750*640

//Cate 750*360