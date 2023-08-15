package com.dagla.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
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
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.AreasAdapter;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class TailoringFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    Button btnHomeVisit,btnOrderTracking;

    Dialog dlgLoading = null;
    private static final String TAG_STATUS = "status";
    ArrayList<HashMap<String, String>> singleCategoryList;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.tailoring_ar),true,false,false,false, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.tailoring),true,false,false,false, false ,"0", false);
        }
        singleCategoryList = new ArrayList<HashMap<String, String>>();


        //new URL().execute();

        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_tailoring_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_tailoring, container, false);
            }


            btnHomeVisit = rootView.findViewById(R.id.btnHomeVisit);
            btnOrderTracking = rootView.findViewById(R.id.btnOrderTracking);


            btnHomeVisit.setVisibility(View.VISIBLE);
            btnOrderTracking.setVisibility(View.VISIBLE);

            btnHomeVisit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HomeVisitServiceFragment fragment1 = new HomeVisitServiceFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
//                            .remove(fragment2)
//                            .add(R.id.fragment_container, fragment1, "RegisterFragment")
                            .replace(R.id.fragment_container, fragment1, "HomeVisitServiceFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });

            btnOrderTracking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    TrackYourDishdasha fragment1 = new TrackYourDishdasha();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
//                            .remove(fragment2)
//                            .add(R.id.fragment_container, fragment1, "RegisterFragment")
                            .replace(R.id.fragment_container, fragment1, "TrackYourDishdasha")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();


                }
            });





        }

        return rootView;

    }

    class URL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
           /* pDialog = ProgressDialog.show(ProfileActivity.this, null, null, true, false);
            pDialog.setContentView(R.layout.progress_layout);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));*/


        }


        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            HttpClient Client = new DefaultHttpClient();

            String URL = "https://portal.dagla.com/services/ajax_v2.aspx?app=ios&lang=en&ver=1.0&cat=requestvisit&value&ran=71";
            // getting JSON string from URL

            try {

                HttpGet httpget = new HttpGet(URL);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();

                String response = Client.execute(httpget, responseHandler);

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    // creating new HashMap
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put(TAG_STATUS, status);

                    // adding HashList to ArrayList
                    singleCategoryList.add(map);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            } catch (Exception ex) {
                Log.e("exception:", ex.toString());
                ex.printStackTrace();

            }
            return null;

        }

        protected void onPostExecute(String file_url) {

            //pDialog.dismiss();


            if(singleCategoryList.get(0).get("status").equals("1"))
            {

                btnHomeVisit.setClickable(true);
                btnHomeVisit.setBackgroundResource(R.drawable.bg_1);


            }
            else
            {
                btnHomeVisit.setClickable(false);
                btnHomeVisit.setBackgroundResource(R.drawable.buttongray);


            }

        }
    }



}
