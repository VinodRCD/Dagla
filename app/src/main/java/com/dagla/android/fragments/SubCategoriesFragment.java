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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.BuildConfig;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.SubCategoriesAdapter;
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

public class SubCategoriesFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    TextView categoryName;
    ListView lv;

//    BaseAdapter arrAdapter;

//    ArrayList arrImages;
//    ArrayList arrNames;

    ArrayAdapter<String> arrAdapter;

    Dialog dlgLoading = null;
    ArrayList<String> arrList;

    String category_Name,category_Id;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard
        context = getActivity();


        if (getArguments().getString("cat_id")!= null) {
            //
            category_Id = getArguments().getString("cat_id");
            Log.v("CategoryId",category_Id);
        }

        if (getArguments().getString("title")!= null) {
            category_Name = getArguments().getString("title");
        }

        if (getArguments().getString("cat_id")!= null&&getArguments().getString("title")!= null) {
            HashMap<String, String> segmentation = new HashMap<String, String>();
            segmentation.put("Category Name", ""+category_Name);
            segmentation.put("Category Id", ""+category_Id);

//            Countly.sharedInstance().recordEvent("Categories Viewed Mostly", segmentation, 1);
        }



        ((MainActivity) getActivity()).setHeaders(category_Name,false,false,false,true, false ,"0", false);
        if (rootView == null) {

//            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_sub_categories, container, false);
//            }else {
//                rootView = inflater.inflate(R.layout.fragment_sub_categories, container, false);
//            }


            lv = rootView.findViewById(R.id.lv);
//            categoryName = (TextView)rootView.findViewById(R.id.categoryName);
//            categoryName.setText(category_Name);

            loadData();

        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                arg1.setEnabled(false);

                String s = arrList.get(position);

                try {

                    final JSONObject obj = new JSONObject(s);


                    if (!obj.getString("cat_id").equalsIgnoreCase("")) {

                        ProductsFragment fragment = new ProductsFragment();
                        Bundle b = new Bundle();
                        b.putString("cat_id", category_Id);
//                        if(position==0){
//                            b.putString("sub_cat_id", "0");
//                            if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
//                                b.putString("title", category_Name);
//                            }else{
//                                b.putString("title", category_Name);
//                            }
//                        }else {
                            b.putString("sub_cat_id", obj.getString("cat_id"));
                            if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
                                b.putString("title", obj.getString("cat_ar"));
                            }else{
                                b.putString("title", obj.getString("cat_en"));
                            }
//                        }



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

                    Log.d("SubCategoriesFragment", "JSONException:" + e.getMessage());

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
            params.put("cat_id", category_Id);

            client.get(GlobalFunctions.serviceURL + "getSubCategoryData", params, new AsyncHttpResponseHandler() {

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

                            arr = obj.getJSONArray("sub_cats");

                            arrList = new ArrayList<String>();

                            for (int i = 0; i < arr.length(); i++) {

                                arrList.add(arr.getJSONObject(i).toString());
                            }


                            arrAdapter = new SubCategoriesAdapter(getActivity(), arrList);

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
