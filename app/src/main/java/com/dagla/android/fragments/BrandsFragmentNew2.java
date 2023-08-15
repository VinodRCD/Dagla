package com.dagla.android.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.AlphabetsAdapter;
import com.dagla.android.adapter.SortAdapter;
import com.dagla.android.parser.CharacterParser;
import com.dagla.android.parser.Glossary;
import com.dagla.android.parser.PinyinComparator;
import com.dagla.android.parser.SortModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class BrandsFragmentNew2 extends Fragment {


    View rootView;
    Context context;
    Bundle savedInstanceState;

    Dialog dlgLoading = null;

    ListView lv,alphabetsListview;
    private FrameLayout mTitleLayout;
    private TextView mTitleText;
    private RelativeLayout mSectionToastLayout;
    private TextView mSectionToastText;
    EditText txtSearch;

    private ArrayList<Glossary> glossaries = new ArrayList<Glossary>();
    private String alphabet = "#0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private AlphabetIndexer mIndexer;
    private SortAdapter adapter;
    private int lastSelectedPosition = -1;

//    BrandsAdapterNew arrAdapter;

    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;
    private PinyinComparator pinyinComparator;

//    String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//            "W", "X", "Y", "Z", "#" };

    AlphabetsAdapter adapter2;

    ArrayList<String> arrBrandId1;
    ArrayList<String> arrBrandNames1;
    ArrayList<String> arrBrandKeys1;

    ArrayList<String> arrAlphabets;

    String[] alphabetsArr;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        context = getActivity();

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_brands__ar), true, true, true, false, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_brands), true, true, true, false, false ,"0", false);
        }


        if (rootView == null) {

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_brands_new_2_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_brands_new_2, container, false);
            }


            characterParser = CharacterParser.getInstance();

            pinyinComparator = new PinyinComparator();

            lv = (ListView) rootView.findViewById(R.id.lv);
            mTitleLayout = (FrameLayout) rootView.findViewById(R.id.title_layout);
            mTitleText = (TextView) rootView.findViewById(R.id.title_text);
            mSectionToastLayout = (RelativeLayout) rootView.findViewById(R.id.section_toast_layout);
            mSectionToastText = (TextView) rootView.findViewById(R.id.section_toast_text);
//            txtSearch = (EditText) rootView.findViewById(R.id.txtSearch);


            alphabetsListview = (ListView) rootView.findViewById(R.id.alphabetsListview);

//            adapter2 = new AlphabetsAdapter(getActivity(), b);
//            alphabetsListview.setAdapter(adapter2);

            alphabetsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(getActivity(), adapter2.getItem(i).toString(), Toast.LENGTH_SHORT).show();
                    int position = adapter.getPositionForSection(adapter2.getItem(i).toString().charAt(0));
                    if (i != -1) {
                        lv.setSelection(position);
                    }
                }
            });

//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    //Using adapter.getItem to (position) to obtain the object corresponding to the current position
//
//                    if(view.equals(R.id.brand_name_layout1)){
////                        Toast.makeText(getActivity(), ((SortModel) adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
//
//
//                        ProductsFragment fragment = new ProductsFragment();
//                        Bundle b = new Bundle();
//                        b.putString("brand_id", ((SortModel) adapter.getItem(position)).getId());
//                        b.putString("title", ((SortModel) adapter.getItem(position)).getName());
//                        fragment.setArguments(b);
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.fragment_container, fragment, "ProductsFragment")
//                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                .addToBackStack(null)
//                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                                .commitAllowingStateLoss();
//
//
//                    }
//
//                }
//            });

//            ArrayList<String> arrNames = new ArrayList<String>();

            arrBrandNames1 = new ArrayList<String>();
            arrBrandKeys1 = new ArrayList<String>();
            arrBrandId1 = new ArrayList<String>();

            arrAlphabets = new ArrayList<String>();

//            arrNames.add("Acne Studios");
//            arrNames.add("Alexander McQueen");
//            arrNames.add("Aquazzura");
//            arrNames.add("Balenciaga");
//            arrNames.add("Balmain");
//            arrNames.add("Balu");
//            arrNames.add("Bottega Veneta");
//            arrNames.add("Burberry");
//            arrNames.add("Chloe");
//            arrNames.add("Etro");
//            arrNames.add("Fendi");
//            arrNames.add("Gianvito Rossi");
//            arrNames.add("Givenchy");
//            arrNames.add("Gucci");
//            arrNames.add("Isabel Marant");
//            arrNames.add("J Crew");
//            arrNames.add("Jimmy Choo");
//            arrNames.add("Joseph");
//            arrNames.add("Lanvin");
//            arrNames.add("Marni");
//            arrNames.add("Miu Miu");
//            arrNames.add("Oscar de le Renta");
//            arrNames.add("Prada");
//            arrNames.add("Saint Laurent");
//            arrNames.add("Self-Portrait");
//            arrNames.add("Stella McCartney");
//            arrNames.add("The Row");
//            arrNames.add("Tom Ford");
//            arrNames.add("Row");
//            arrNames.add("Ford");
//            arrNames.add("Renta");
//            arrNames.add("Choo");
//            arrNames.add("1 Renta");
//            arrNames.add("2 Choo");
//            arrNames.add("#Renta");
//            arrNames.add("*Choo");


//            String[] listArr = new String[arrNames.size()];
//            listArr = arrNames.toArray(listArr);
//
//
//            SourceDateList = filledData(listArr);
//
//            // According to the A-Z sort of source data
//            Collections.sort(SourceDateList, pinyinComparator);
//            adapter = new SortAdapter(getActivity(), SourceDateList);
//            lv.setAdapter(adapter);

            loadData();


//            //To filter the search according to the input box to input value change
//            txtSearch.addTextChangedListener(new TextWatcher() {
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    //When the input box is empty, updates to the original list, or to filter the data list
//                    filterData(s.toString());
//                }
//
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count,
//                                              int after) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });



        }

        return rootView;
    }

    /**
     * Filling the data for ListView
     * @param date
     * @return
     */
    private List<SortModel> filledData(String [] date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            //Chinese characters into pinyin
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // Regular expressions, judge whether English letter initials
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }


    private List<SortModel> filledData2(String [] brand_id, String [] brand_name,String [] brand_key){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<brand_name.length; i++){
            SortModel sortModel = new SortModel();
            sortModel.setId(brand_id[i]);
            sortModel.setName(brand_name[i]);

            //Chinese characters into pinyin
//            String pinyin = characterParser.getSelling(brand_name[i]);
//            String sortString = pinyin.substring(0, 1).toUpperCase();

            // Regular expressions, judge whether English letter initials
//            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(brand_key[i].toUpperCase());
//            }else{
//                sortModel.setSortLetters("#");
//            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * According to the values in the input box to filter the data and update ListView
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(
                        filterStr.toString().toUpperCase()) != -1
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // Sorted according to A-Z
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);

    }



    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getBrands", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr, arr1, arr2;
                    JSONObject obj, obj1, obj2;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            arr = obj.getJSONArray("data");


                            for (int i = 0; i < arr.length(); i++) {


                                arr1 = arr.getJSONObject(i).getJSONArray("Brands");

                                arrAlphabets.add(arr.getJSONObject(i).getString("Key"));

                                for (int j = 0; j < arr1.length(); j++) {

                                    arrBrandId1.add(arr1.getJSONObject(j).getString("brand_id"));
                                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                                        arrBrandNames1.add(arr1.getJSONObject(j).getString("brand_name_ar"));
                                    }else {
                                        arrBrandNames1.add(arr1.getJSONObject(j).getString("brand_name"));
                                    }

                                    arrBrandKeys1.add(arr.getJSONObject(i).getString("Key"));

                                }

                            }

//                            String[] listArr = new String[arrNames1.size()];
//                            listArr = arrNames1.toArray(listArr);
//
//                            SourceDateList = filledData(listArr);


                            String[] listArrBrands = new String[arrBrandNames1.size()];
                            listArrBrands = arrBrandNames1.toArray(listArrBrands);

                            String[] listArrBrandsKey = new String[arrBrandKeys1.size()];
                            listArrBrandsKey = arrBrandKeys1.toArray(listArrBrandsKey);

                            String[] listArrBrandsId = new String[arrBrandId1.size()];
                            listArrBrandsId = arrBrandId1.toArray(listArrBrandsId);

                            SourceDateList = filledData2(listArrBrandsId,listArrBrands,listArrBrandsKey);


                            // According to the A-Z sort of source data
//                            Collections.sort(SourceDateList, pinyinComparator);
                            adapter = new SortAdapter(getActivity(), SourceDateList);
                            lv.setAdapter(adapter);


                            alphabetsArr = arrAlphabets.toArray(new String[arrAlphabets.size()]);

                            adapter2 = new AlphabetsAdapter(getActivity(), alphabetsArr);
                            alphabetsListview.setAdapter(adapter2);

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
