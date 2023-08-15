package com.dagla.android.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dagla.android.BlurBuilder;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.SizesAdapter;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.ImagesAdapter2;
import com.dagla.android.adapter.ItemsAdapter;
import com.dagla.android.adapter.ProductColorsAdapter;
import com.dagla.android.adapter.ProductSizesAdapter;
import com.dagla.android.adapter.RelatedColorsAdapter;
import com.dagla.android.adapter.RelatedItemsAdapter;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
//import ly.count.android.sdk.Countly;

public class ProductDescriptionFromHomeFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    ViewPager vPagerImages;
    ImageView imgOverlay, imgPlus, imgWishlist;
    TabLayout tabDots;
    TextView lblName, lblPrice, lblDiscount, lblDescription, lblNew, lblSale, lblSizeGuide;
    EditText txtSize;
    Button btnAddToCart;
    LinearLayout pnlRelated,pnlRelatedColors;
    RelativeLayout pnlSize, pnlDescriptionHeader, pnlDescription;
    RecyclerView rvRelatedItems;
    TextView lblBrand,lblColor,lblStock;
    Button btnAddToWishlist;
    ViewPager vPagerImages1,vPagerImages2;
    TabLayout tabDots1,tabDots2;

    ImageView wishlistImg;
    TextView lblWishlistTxt;
    RelativeLayout wishlistLayout;

    Dialog dlgLoading = null;

    JSONObject objMain;

    PagerAdapter adapterImages;

    ArrayList<String> arrImages, arrItems, arrSizes;

    RecyclerView.Adapter adapterItems;

    ArrayAdapter<String> adapterSizes;

    GridLayoutManager layoutManager;

    DisplayMetrics displaymetrics;

    String variationProductId = "";

    String product_Id;

    ArrayList productsArrayList1 = new ArrayList<String>();

    String brand_Id;

    ArrayList<String> productNameArrList1;
    ArrayList<String> productIdArrList1;
    ArrayList<String> productPriceArrList1;
    ArrayList<String> picArrList1;

    ArrayList<HashMap<String, String>> arrayList1;
    ArrayList<HashMap<String, String>> arrayList2;
    ArrayList<HashMap<String, String>> arrayList3;
    ArrayList<HashMap<String, String>> arrayList4;

    RelatedColorsAdapter relatedColorsAdapter;


    LinearLayout colorLayout,sizeLayout;
    RecyclerView color_recyclerView,size_recyclerView;

    ArrayList<String> colorsArrList;
    ArrayList<String> sizesArrList;
    ProductColorsAdapter productColorsAdapter;
    ProductSizesAdapter productSizesAdapter;

    RelatedItemsAdapter relatedItemsAdapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        if (getArguments().getString("title")!= null) {
            ((MainActivity) getActivity()).setHeaders(getArguments().getString("title"),true,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(" ",true,false,false,true, false ,"0", false);
        }


//        if (rootView == null) {
        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            rootView = inflater.inflate(R.layout.fragment_product_description_ar, container, false);
        }else {
            rootView = inflater.inflate(R.layout.fragment_product_description, container, false);
        }

        colorLayout = rootView.findViewById(R.id.colorLayout);
        sizeLayout = rootView.findViewById(R.id.sizeLayout);

        color_recyclerView = rootView.findViewById(R.id.color_recyclerView);
        size_recyclerView = rootView.findViewById(R.id.size_recyclerView);

        colorsArrList = new ArrayList<String>();
        sizesArrList = new ArrayList<String>();
        productNameArrList1 = new ArrayList<String>();
        productIdArrList1 = new ArrayList<String>();
        productPriceArrList1 = new ArrayList<String>();
        picArrList1 = new ArrayList<String>();

        arrayList1 = new ArrayList<HashMap<String, String>>();
        arrayList2 = new ArrayList<HashMap<String, String>>();
        arrayList3 = new ArrayList<HashMap<String, String>>();
        arrayList4 = new ArrayList<HashMap<String, String>>();

        vPagerImages1 = rootView.findViewById(R.id.vPagerImages1);
        tabDots1 = rootView.findViewById(R.id.tabDots1);
        pnlRelatedColors = rootView.findViewById(R.id.pnlRelatedColors);

        vPagerImages2 = rootView.findViewById(R.id.vPagerImages2);
        tabDots2 = rootView.findViewById(R.id.tabDots2);

            vPagerImages = rootView.findViewById(R.id.vPagerImages);
            imgOverlay = rootView.findViewById(R.id.imgOverlay);
            imgPlus = rootView.findViewById(R.id.imgPlus);
            tabDots = rootView.findViewById(R.id.tabDots);
            lblName = rootView.findViewById(R.id.lblName);
            lblPrice = rootView.findViewById(R.id.lblPrice);
            lblDiscount = rootView.findViewById(R.id.lblDiscount);
            lblDescription = rootView.findViewById(R.id.lblDescription);
            lblNew = rootView.findViewById(R.id.lblNew);
            lblSale = rootView.findViewById(R.id.lblSale);
            txtSize = rootView.findViewById(R.id.txtSize);
            btnAddToCart = rootView.findViewById(R.id.btnAddToCart);
            rvRelatedItems = rootView.findViewById(R.id.rvRelatedItems);
            pnlSize = rootView.findViewById(R.id.pnlSize);
            pnlRelated = rootView.findViewById(R.id.pnlRelated);
            pnlDescriptionHeader = rootView.findViewById(R.id.pnlDescriptionHeader);
            pnlDescription = rootView.findViewById(R.id.pnlDescription);
            imgWishlist = rootView.findViewById(R.id.imgWishlist);

        wishlistImg =rootView.findViewById(R.id.wishlistImg);
        lblWishlistTxt = rootView.findViewById(R.id.lblWishlistTxt);
        wishlistLayout = rootView.findViewById(R.id.wishlistLayout);
        wishlistLayout.setVisibility(View.GONE);

        lblBrand = rootView.findViewById(R.id.lblBrand);
        lblColor = rootView.findViewById(R.id.lblColor);
        lblStock = rootView.findViewById(R.id.lblStock);
        btnAddToWishlist = rootView.findViewById(R.id.btnAddToWishlist);

        lblSizeGuide = rootView.findViewById(R.id.lblSizeGuide);
        lblSizeGuide.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        lblSizeGuide.setVisibility(View.GONE);

//            lblName.setTypeface(custom_fontnormal);
//            lblPrice.setTypeface(custom_fontbold);
//            lblDiscount.setTypeface(custom_fontnormal);
//            lblDescription.setTypeface(custom_fontnormal);
//            lblNew.setTypeface(custom_fontnormal);
//            lblSale.setTypeface(custom_fontnormal);
//            txtSize.setTypeface(custom_fontnormal);
//            btnAddToCart.setTypeface(custom_fontnormal);

            TextView lblDescriptionTxt = (TextView) rootView.findViewById(R.id.lblDescriptionTxt);
            TextView lblRelatedItemsTxt = (TextView) rootView.findViewById(R.id.lblRelatedItemsTxt);

//            lblDescriptionTxt.setTypeface(custom_fontbold);
//            lblRelatedItemsTxt.setTypeface(custom_fontbold);

            //
            lblDiscount.setPaintFlags(lblDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //
            layoutManager = new GridLayoutManager(getActivity(), 2);
            //
            displaymetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            //
//            float screenWidth = displaymetrics.widthPixels;
//            //
//            ViewGroup.LayoutParams params1 = vPagerImages.getLayoutParams();
//            ViewGroup.LayoutParams params2 = imgOverlay.getLayoutParams();
//            //
//            params1.height = (int)screenWidth;
//            params2.height = (int)screenWidth;
//            //
//            vPagerImages.setLayoutParams(params1);
//            imgOverlay.setLayoutParams(params2);
            //
            if (getArguments().getString("product_id")!= null) {
                //
//                Bundle b = getArguments().getExtras();
                //
                product_Id = getArguments().getString("product_id");

                Log.v("product_Id",product_Id);
//                //
//                try {
//
//                    objMain = new JSONObject(s);
//
////                    ab.setTitle(objMain.getString("name"));
//
//                } catch (JSONException e) {
//                    //
//                }
                //
//                setInitialData();


                loadData(product_Id);
            }


            imgPlus.animate().rotation(45f);

            pnlDescription.setVisibility(View.VISIBLE);

            pnlDescriptionHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    if (pnlDescription.getVisibility() == View.VISIBLE) {
//
//                        pnlDescription.setVisibility(View.GONE);
//
//                        imgPlus.animate().rotation(0f);
//
//                    } else {
//
//                        imgPlus.animate().rotation(45f);
//
//                        pnlDescription.setVisibility(View.VISIBLE);
//                    }

                }
            });
            //
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addToCart();
                }
            });
            //
            txtSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                                variationProductId = obj.getString("product_id");

                                txtSize.setText(obj.getString("size_name"));

                                if (!obj.getBoolean("in_stock")) {

                                    btnAddToCart.setEnabled(false);

                                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                        btnAddToCart.setText(getString(R.string.sold_out_ar));
                                        lblStock.setText("نفذت");
                                    }else {
                                        btnAddToCart.setText(getString(R.string.sold_out));
                                        lblStock.setText("Out of Stock");
                                    }

                                }else {

                                    btnAddToCart.setEnabled(true);

                                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                        btnAddToCart.setText(getString(R.string.add_to_shopping_bag_ar));
                                        lblStock.setText("متوفر");
                                    }else {
                                        btnAddToCart.setText(getString(R.string.add_to_shopping_bag));
                                        lblStock.setText("In Stock");
                                    }
                                }




                                dialog.dismiss();

                                if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {
                                    loadWishlistData();
                                }

                            } catch (JSONException e) {

                                Log.d("JSONException", e.getMessage());
                            }

                        }
                    });

                    builder.show();

                }
            });

        btnAddToWishlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {
                        addToWishlist();
                    }else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                        builder.setMessage("Please login to add to wishlist");
//
//                        builder.setPositiveButton(getString(R.string.yes).toUpperCase(), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
////                                LoginFragment fragment1 = new LoginFragment();
////                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                                fragmentManager.beginTransaction()
////                                        .replace(R.id.fragment_container, fragment1, "LoginFragment")
////                                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
////                                        .addToBackStack(null)
////                                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
////                                        .commitAllowingStateLoss();
//
//                            }
//                        });
//
////                        builder.setNegativeButton(getString(R.string.no).toUpperCase(), new DialogInterface.OnClickListener() {
////                            public void onClick(DialogInterface dialog, int id) {
////
////
////                            }
////                        });
//
//                        AlertDialog dialog = builder.create();
//
//                        dialog.show();

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            infoDialog("الرجاء تسجيل الدخول للإضافة إلى قائمة الأمنيات");
                        }else {
                            infoDialog("Please login to add to wishlist");
                        }
                    }


                }
            });

        wishlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {
                    addToWishlist();
                }else {

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                        infoDialog("الرجاء تسجيل الدخول للإضافة إلى قائمة الأمنيات");
                    }else {
                        infoDialog("Please login to add to wishlist");
                    }
                }
            }
        });

        lblBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductsFragment fragment = new ProductsFragment();
                Bundle b = new Bundle();
                b.putString("brand_id", brand_Id);
                b.putString("title", lblBrand.getText().toString());
                fragment.setArguments(b);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, "ProductDescriptionFromHomeFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                        .commitAllowingStateLoss();

            }
        });

        lblSizeGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Information2Fragment fragment = new Information2Fragment();
                Bundle b = new Bundle();
                b.putString("page_id", "uT4LCYxkiFY=");
                if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                    b.putString("title", getResources().getString(R.string.view_size_guide_ar));
                }else {
                    b.putString("title", getResources().getString(R.string.view_size_guide));
                }

                fragment.setArguments(b);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, "Information2Fragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                        .commitAllowingStateLoss();

            }
        });
//        }

        return rootView;

    }

    private void setInitialData() {

        try {

            lblName.setText(objMain.getString("name"));

            lblPrice.setText(objMain.getString("price"));

            ImageLoader.getInstance().loadImage(objMain.getString("pic"), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    Bitmap blurredBitmap = BlurBuilder.blur(getActivity(), loadedImage);

                    imgOverlay.setImageBitmap(blurredBitmap);

                }
            });

        } catch (JSONException e) {
            //
        }

    }

    public void loadData(String productId) {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            productNameArrList1.clear();
            productIdArrList1.clear();
            productPriceArrList1.clear();
            picArrList1.clear();

            arrayList1.clear();
            arrayList2.clear();
            arrayList3.clear();
            arrayList4.clear();

            colorsArrList.clear();
            sizesArrList.clear();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

//            try {
                params.put("productId", productId);
//            } catch (JSONException e) {
//                //
//            }

            params.put("ran", GlobalFunctions.getRandom());

            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }

            client.get(GlobalFunctions.serviceURL + "getItemDetails", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONArray arr,arr3;
                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            String lang = GlobalFunctions.getLang(getActivity());

                            lblName.setText(obj.getString("name"));
                            brand_Id = obj.getString("brand_id");
                            lblBrand.setText(obj.getString("brand_name"));

                            if (lang.equalsIgnoreCase("ar")) {
                                lblColor.setText(obj.getString("color_name")+" - اللون");
//                                String price = obj.getString("price");
//                                price =  price.replace("د.ك", "");
//                                lblPrice.setText("د.ك "+price);
//
//                                if(!obj.getString("old_price").equals("")){
//                                    String dis_price = obj.getString("old_price");
//                                    dis_price =  dis_price.replace("د.ك", "");
//                                    lblDiscount.setText("د.ك "+dis_price);
//                                }

                                lblPrice.setText(obj.getString("price"));
                                lblDiscount.setText(obj.getString("old_price"));


                            }else {
                                lblColor.setText("Color - "+obj.getString("color_name"));
                                lblPrice.setText(obj.getString("price"));
                                lblDiscount.setText(obj.getString("old_price"));

                            }


                            if(obj.getBoolean("in_stock")){
                                if (lang.equalsIgnoreCase("ar")) {
                                    lblStock.setText("متوفر");
                                }else {
                                    lblStock.setText("In Stock");
                                }
                            }else {
                                if (lang.equalsIgnoreCase("ar")) {
                                    lblStock.setText("نفذت");
                                }else {
                                    lblStock.setText("Out of Stock");
                                }
                            }

                            HashMap<String, String> segmentation = new HashMap<String, String>();
//                            String versionName = BuildConfig.VERSION_NAME;
//                segmentation.put("User Id",GlobalFunctions.getPrefrences(getActivity(), "user_id"));
//                segmentation.put("App Version", ""+versionName);
                            segmentation.put("Product Name", ""+lblName.getText().toString());
                            segmentation.put("Product Id", ""+product_Id);

//                            Countly.sharedInstance().recordEvent("Products Viewed Mostly", segmentation, 1);


                            lblSale.setVisibility(View.GONE);
                            lblNew.setVisibility(View.GONE);

//                            if (!obj.getString("color_name").equalsIgnoreCase("")) {
//                                lblColor.setVisibility(View.VISIBLE);
//                            }else {
//                                lblColor.setVisibility(View.GONE);
//                            }

//                            ImageLoader.getInstance().loadImage(obj.getString("pic"), new SimpleImageLoadingListener() {
//                                @Override
//                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
//                                    Bitmap blurredBitmap = BlurBuilder.blur(getActivity(), loadedImage);
//
//                                    imgOverlay.setImageBitmap(blurredBitmap);
//
//                                }
//                            });

                            if (!lblDiscount.getText().toString().equalsIgnoreCase("")) {

//                                lblSale.setVisibility(View.VISIBLE);
                                lblSale.setVisibility(View.GONE);

                            } else if (obj.getBoolean("is_new")) {

//                                lblNew.setVisibility(View.VISIBLE);
                                lblNew.setVisibility(View.GONE);
                            }

                            arr = obj.getJSONArray("sizes");

                            arrSizes = new ArrayList<String>();

                            if (arr.length() > 0) {

                                pnlSize.setVisibility(View.GONE);
                                lblSizeGuide.setVisibility(View.GONE);

                                JSONObject objSize = arr.getJSONObject(0);

                                txtSize.setText(objSize.getString("size_name"));

                                for (int i=0; i<arr.length(); i++) {

                                    arrSizes.add(arr.getJSONObject(i).toString());
                                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                        sizesArrList.add(arr.getJSONObject(i).getString("size_name_ar"));
                                    }else {
                                        sizesArrList.add(arr.getJSONObject(i).getString("size_name"));
                                    }
                                }
                            }

                            adapterSizes = new SizesAdapter(getActivity(), arrSizes);

                            lblDescription.setText(obj.getString("description"));

                            arr = obj.getJSONArray("pics");

                            arrImages = new ArrayList<String>();

//                            String lang = GlobalFunctions.getLang(getActivity());
//
//                            if (lang.equalsIgnoreCase("ar")) {
//
//                                for (int i = arr.length() - 1; i >= 0; i--) {
//
//                                    arrImages.add(arr.getJSONObject(i).getString("pic"));
//
//                                }
//
//                            } else {

                                for (int i = 0; i < arr.length(); i++) {

                                    arrImages.add(arr.getJSONObject(i).getString("pic"));

                                }

//                            }

                            adapterImages = new ImagesAdapter2(getActivity(), arrImages, ProductDescriptionFromHomeFragment.this);

                            vPagerImages.setAdapter(adapterImages);

                            tabDots.setupWithViewPager(vPagerImages, true);

//                            if (lang.equalsIgnoreCase("ar")) {
//
//                                vPagerImages.setCurrentItem(arrImages.size()-1);
//                            }

                            if (!obj.getBoolean("in_stock")) {

                                btnAddToCart.setEnabled(false);

                                if (lang.equalsIgnoreCase("ar")) {
                                    btnAddToCart.setText(getString(R.string.sold_out_ar));
                                }else {
                                    btnAddToCart.setText(getString(R.string.sold_out));
                                }

                            }

                            arr = obj.getJSONArray("related");

                            if (arr.length() > 0) {

                                arrItems = new ArrayList<String>();

                                for (int i = 0; i < arr.length(); i++) {

                                    arrItems.add(arr.getJSONObject(i).toString());

                                    productIdArrList1.add(arr.getJSONObject(i).getString("product_id"));
                                    productNameArrList1.add(arr.getJSONObject(i).getString("name"));
                                    productPriceArrList1.add(arr.getJSONObject(i).getString("price"));
                                    picArrList1.add(arr.getJSONObject(i).getString("pic"));

                                }

//                                adapterItems = new ItemsAdapter(getActivity(), arrItems, displaymetrics.widthPixels);
//
////                                rvRelatedItems.setLayoutManager(layoutManager);
//
//                                LinearLayoutManager horizontalLayoutManagaer
//                                        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//                                rvRelatedItems.setLayoutManager(horizontalLayoutManagaer);
//
//
//                                rvRelatedItems.setAdapter(adapterItems);
//                                rvRelatedItems.setNestedScrollingEnabled(false);

                                ((MainActivity) getActivity()).setHeaders(obj.getString("name"),true,false,false,true, false ,"0", false);

                            } else {

                                pnlRelated.setVisibility(View.GONE);

                            }


                            arr3 = obj.getJSONArray("relatedColors");

                            if(arr3.length()>0){
                                pnlRelatedColors.setVisibility(View.GONE);
                            }else {
                                pnlRelatedColors.setVisibility(View.GONE);
                            }

                            for (int j = 0; j < arr3.length(); j++) {
                                colorsArrList.add(arr3.getJSONObject(j).getString("color_code"));
//                                productIdArrList1.add(arr3.getJSONObject(j).getString("product_id"));
//                                productNameArrList1.add(arr3.getJSONObject(j).getString("name"));
//                                productPriceArrList1.add(arr3.getJSONObject(j).getString("price"));
//                                picArrList1.add(arr3.getJSONObject(j).getString("pic"));
                            }

                            color_recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                            productColorsAdapter = new ProductColorsAdapter(requireActivity(), colorsArrList);
                            color_recyclerView.setAdapter(productColorsAdapter);

                            productColorsAdapter.setOnClickListener(new ProductColorsAdapter.ClickListener() {
                                @Override
                                public void OnItemClick(int position, View v) {

                                    productColorsAdapter.Selected(position);
                                }
                            });

                            size_recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                            productSizesAdapter = new ProductSizesAdapter(requireActivity(), sizesArrList);
                            size_recyclerView.setAdapter(productSizesAdapter);

                            productSizesAdapter.setOnClickListener(new ProductSizesAdapter.ClickListener() {
                                @Override
                                public void OnItemClick(int position, View v) {

                                    productSizesAdapter.Selected(position);
                                }
                            });


                            if(colorsArrList.size()>0){
                                colorLayout.setVisibility(View.VISIBLE);
                            }else {
                                colorLayout.setVisibility(View.GONE);
                            }

                            if(sizesArrList.size()>0){
                                sizeLayout.setVisibility(View.VISIBLE);
                            }else {
                                sizeLayout.setVisibility(View.GONE);
                            }

                            if(productNameArrList1.size()>=1  || productNameArrList1.size()>=2){

                                HashMap<String, String> productName1Map1 = new HashMap<String, String>();
                                HashMap<String, String> productImage1Map1 = new HashMap<String, String>();
                                HashMap<String, String> productId1Map1 = new HashMap<String, String>();
                                HashMap<String, String> productPrice1Map1 = new HashMap<String, String>();

                                if (productNameArrList1.size()>=2){

                                    productName1Map1.put("Key1",productNameArrList1.get(0).toString());
                                    productImage1Map1.put("Key1",picArrList1.get(0).toString());
                                    productId1Map1.put("Key1",productIdArrList1.get(0).toString());
                                    productPrice1Map1.put("Key1",productPriceArrList1.get(0).toString());

                                    productName1Map1.put("Key2",productNameArrList1.get(1).toString());
                                    productImage1Map1.put("Key2",picArrList1.get(1).toString());
                                    productId1Map1.put("Key2",productIdArrList1.get(1).toString());
                                    productPrice1Map1.put("Key2",productPriceArrList1.get(1).toString());
                                }else if (productNameArrList1.size()>=1){

                                    productName1Map1.put("Key1",productNameArrList1.get(0).toString());
                                    productImage1Map1.put("Key1",picArrList1.get(0).toString());
                                    productId1Map1.put("Key1",productIdArrList1.get(0).toString());
                                    productPrice1Map1.put("Key1",productPriceArrList1.get(0).toString());

                                    productName1Map1.put("Key2","");
                                    productImage1Map1.put("Key2","");
                                    productId1Map1.put("Key2","");
                                    productPrice1Map1.put("Key2","");
                                }


                                arrayList1.add(productName1Map1);
                                arrayList2.add(productImage1Map1);
                                arrayList3.add(productId1Map1);
                                arrayList4.add(productPrice1Map1);

                            }

                            if(productNameArrList1.size()>=3  || productNameArrList1.size()>=4){

                                HashMap<String, String> productName1Map2 = new HashMap<String, String>();
                                HashMap<String, String> productImage1Map2 = new HashMap<String, String>();
                                HashMap<String, String> productId1Map2 = new HashMap<String, String>();
                                HashMap<String, String> productPrice1Map2 = new HashMap<String, String>();

                                if (productNameArrList1.size()>=4){
                                    productName1Map2.put("Key1",productNameArrList1.get(2).toString());
                                    productImage1Map2.put("Key1",picArrList1.get(2).toString());
                                    productId1Map2.put("Key1",productIdArrList1.get(2).toString());
                                    productPrice1Map2.put("Key1",productPriceArrList1.get(2).toString());

                                    productName1Map2.put("Key2",productNameArrList1.get(3).toString());
                                    productImage1Map2.put("Key2",picArrList1.get(3).toString());
                                    productId1Map2.put("Key2",productIdArrList1.get(3).toString());
                                    productPrice1Map2.put("Key2",productPriceArrList1.get(3).toString());
                                }else if (productNameArrList1.size()>=3){
                                    productName1Map2.put("Key1",productNameArrList1.get(2).toString());
                                    productImage1Map2.put("Key1",picArrList1.get(2).toString());
                                    productId1Map2.put("Key1",productIdArrList1.get(2).toString());
                                    productPrice1Map2.put("Key1",productPriceArrList1.get(2).toString());

                                    productName1Map2.put("Key2","");
                                    productImage1Map2.put("Key2","");
                                    productId1Map2.put("Key2","");
                                    productPrice1Map2.put("Key2","");
                                }


                                arrayList1.add(productName1Map2);
                                arrayList2.add(productImage1Map2);
                                arrayList3.add(productId1Map2);
                                arrayList4.add(productPrice1Map2);

                            }

                            if(productNameArrList1.size()>=5  || productNameArrList1.size()>=6){

                                HashMap<String, String> productName1Map3 = new HashMap<String, String>();
                                HashMap<String, String> productImage1Map3 = new HashMap<String, String>();
                                HashMap<String, String> productId1Map3 = new HashMap<String, String>();
                                HashMap<String, String> productPrice1Map3 = new HashMap<String, String>();

                                if (productNameArrList1.size()>=6){
                                    productName1Map3.put("Key1",productNameArrList1.get(4).toString());
                                    productImage1Map3.put("Key1",picArrList1.get(4).toString());
                                    productId1Map3.put("Key1",productIdArrList1.get(4).toString());
                                    productPrice1Map3.put("Key1",productPriceArrList1.get(4).toString());

                                    productName1Map3.put("Key2",productNameArrList1.get(5).toString());
                                    productImage1Map3.put("Key2",picArrList1.get(5).toString());
                                    productId1Map3.put("Key2",productIdArrList1.get(5).toString());
                                    productPrice1Map3.put("Key2",productPriceArrList1.get(5).toString());
                                }else if (productNameArrList1.size()>=5){
                                    productName1Map3.put("Key1",productNameArrList1.get(4).toString());
                                    productImage1Map3.put("Key1",picArrList1.get(4).toString());
                                    productId1Map3.put("Key1",productIdArrList1.get(4).toString());
                                    productPrice1Map3.put("Key1",productPriceArrList1.get(4).toString());

                                    productName1Map3.put("Key2","");
                                    productImage1Map3.put("Key2","");
                                    productId1Map3.put("Key2","");
                                    productPrice1Map3.put("Key2","");
                                }


                                arrayList1.add(productName1Map3);
                                arrayList2.add(productImage1Map3);
                                arrayList3.add(productId1Map3);
                                arrayList4.add(productPrice1Map3);
                            }


                            relatedColorsAdapter = new RelatedColorsAdapter(getActivity(), arrayList1, arrayList2, arrayList3, arrayList4);
                            vPagerImages1.setAdapter(relatedColorsAdapter);
                            tabDots1.setupWithViewPager(vPagerImages1, true);

                            relatedItemsAdapter = new RelatedItemsAdapter(getActivity(), arrayList1, arrayList2, arrayList3, arrayList4,ProductDescriptionFromHomeFragment.this,"Home");
                            vPagerImages2.setAdapter(relatedItemsAdapter);
                            tabDots2.setupWithViewPager(vPagerImages2, true);

                            if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {
                                loadWishlistData();
                            }

                            if(obj.getBoolean("in_stock")&&!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")){
                                wishlistLayout.setVisibility(View.VISIBLE);
                            }else {
                                wishlistLayout.setVisibility(View.GONE);
                            }

                        } else {

                            btnAddToCart.setEnabled(false);

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
//            GlobalFunctions.errorDialog(getActivity(),getString(R.string.msg_no_internet));
        }

    }

    private void loadWishlistData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            Log.v("UserID",GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getUserWishList", params, new AsyncHttpResponseHandler() {

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
                            productsArrayList1.clear();
                            for (int i = 0; i < arr.length(); i++) {

//                                arrItems.add(arr.getJSONObject(i).getString("product_id").toString());
                                productsArrayList1.add(arr.getJSONObject(i).getString("product_id").toString());
                            }

                        } else {



                        }

                        if (!variationProductId.equalsIgnoreCase("")) {

                            if(productsArrayList1.contains(variationProductId)){
                                imgWishlist.setImageResource(R.drawable.wishlist_2_icon);
                                wishlistImg.setImageResource(R.drawable.wishlist_2_icon);
                                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                    lblWishlistTxt.setText("أضيف لقائمة الأماني");
                                }else {
                                    lblWishlistTxt.setText("Added to Wishlist");
                                }
                            }else {
                                imgWishlist.setImageResource(R.drawable.wishlist_1_icon);
                                wishlistImg.setImageResource(R.drawable.wishlist_1_icon);
                                if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                    lblWishlistTxt.setText("قائمة الامنيات");
                                }else {
                                    lblWishlistTxt.setText("Add to Wishlist");
                                }
                            }

                        } else {

//                            try {

                                String pro_id = product_Id;

                                if(productsArrayList1.contains(pro_id)){

                                    imgWishlist.setImageResource(R.drawable.wishlist_2_icon);
                                    wishlistImg.setImageResource(R.drawable.wishlist_2_icon);
                                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                        lblWishlistTxt.setText("أضيف لقائمة الأماني");
                                    }else {
                                        lblWishlistTxt.setText("Added to Wishlist");
                                    }
                                }else {
                                    imgWishlist.setImageResource(R.drawable.wishlist_1_icon);
                                    wishlistImg.setImageResource(R.drawable.wishlist_1_icon);
                                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                        lblWishlistTxt.setText("قائمة الامنيات");
                                    }else {
                                        lblWishlistTxt.setText("Add to Wishlist");
                                    }
                                }


//                            } catch (JSONException e) {
//                                //
//                            }

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

    private void addToCart() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            if (!variationProductId.equalsIgnoreCase("")) {

                params.put("productId", variationProductId);

            } else {

//                try {
//                    params.put("productId", objMain.getString("product_id"));
//                } catch (JSONException e) {
//                    //
//                }

                params.put("productId", product_Id);

            }

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("cartIds", GlobalFunctions.getCart());
            params.put("qty", "1");
            params.put("ran", GlobalFunctions.getRandom());
            if(!GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency").equals("")){
                params.put("curr", GlobalFunctions.getPrefrences(getActivity(), "CountryCurrency"));
            }
            Log.v("CartUrl",""+params);

            client.get(GlobalFunctions.serviceURL + "addToCart", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

                            if (!obj.getString("cart_id").equalsIgnoreCase("0")) {

                                GlobalFunctions.addToCart(obj.getString("cart_id"));
                            }

                            ((MainActivity) getActivity()).setCartCount();

                            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                successDialog(obj.getString("msg"),getString(R.string.view_cart_ar), getString(R.string.continue_shopping_ar));
                            }else {
                                successDialog(obj.getString("msg"),getString(R.string.view_cart), getString(R.string.continue_shopping));
                            }



//                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//                            builder.setMessage(obj.getString("msg"));
//                            ((MainActivity) getActivity()).setCartCount();
//                            builder.setPositiveButton(getString(R.string.view_cart).toUpperCase(), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
////                                    startActivity(new Intent(getActivity(), CartActivity.class));
//
//                                    CartFragment fragment = new CartFragment();
//                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                                    fragmentManager.beginTransaction()
//                                            .replace(R.id.fragment_container, fragment, "CartFragment")
//                                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                                            .addToBackStack(null)
//                                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                                            .commitAllowingStateLoss();
//
//                                }
//                            });
//
//                            builder.setNegativeButton(getString(R.string.continue_shopping).toUpperCase(), new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//
////                                    finish();
//
//                                    getFragmentManager().popBackStack();
//                                }
//                            });
//
//                            AlertDialog dialog = builder.create();
//
//                            dialog.show();

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

    private void addToWishlist() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("userId", GlobalFunctions.getPrefrences(getActivity(), "user_id"));
            params.put("ran", GlobalFunctions.getRandom());

            if (!variationProductId.equalsIgnoreCase("")) {

                params.put("productId", variationProductId);

            } else {

//                try {
                    params.put("productId", product_Id);
//                } catch (JSONException e) {
//                    //
//                }

            }



            Log.v("WishlistUrl",""+params);

            client.get(GlobalFunctions.serviceURL + "AddtoWishList", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {

                    hideLoading();

                    String response = new String(bytes);

                    Log.d("onSuccess", response);

                    JSONObject obj;

                    try {

                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");

                        if (obj.getString("status").equalsIgnoreCase("1")) {

//                            GlobalFunctions.showToastSuccess(getActivity(), obj.getString("msg"));
//                            successDialog(obj.getString("msg"));
                            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                successDialog2(obj.getString("msg"),getString(R.string.view_wishlist_ar), getString(R.string.ok_ar));
                            }else {
                                successDialog2(obj.getString("msg"),getString(R.string.view_wishlist), getString(R.string.ok));
                            }

                            if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {
                                loadWishlistData();
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

    public void hideOverlay() {

        imgOverlay.setVisibility(View.GONE);

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

//    public void showGallery(int position) {
//
//        final Dialog dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_layout_for_gallery_image);
//        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
//        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        dialog.getWindow().setAttributes(layoutParams);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(true);
//        ViewPager pager_post_photo = (ViewPager) dialog.findViewById(R.id.pager_post_photo);
//
////        String[] photosArr = arrImages.toArray(new String[arrImages.size()]);
//
//        Adapter_Gallery adapter_gallery = new Adapter_Gallery(getActivity(), arrImages);
//        pager_post_photo.setAdapter(adapter_gallery);
//        pager_post_photo.setCurrentItem(position);
//
//        TabLayout tabDots = dialog.findViewById(R.id.tabDots);
//        tabDots.setupWithViewPager(pager_post_photo, true);
//
//        Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
//
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//
//
//        dialog.show();
//
//    }


    public void successDialog(String msg, String button1, String button2) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            dialog.setContentView(R.layout.sucess_dialog_box_ar);
        }else {
            dialog.setContentView(R.layout.sucess_dialog_box);
        }
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

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        Button cancelBtn= (Button) dialog.findViewById(R.id.cancelBtn);

        okBtn.setText(button1);
        cancelBtn.setText(button2);

        alertMessage.setText(msg);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

//                CartFragment fragment = new CartFragment();
                CartFragmentNew fragment = new CartFragmentNew();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, "SubCategoriesFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                        .commitAllowingStateLoss();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                getFragmentManager().popBackStack();

            }
        });

        dialog.show();

    }

    public void successDialog2(String msg, String button1, String button2) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            dialog.setContentView(R.layout.sucess_dialog_box_ar);
        }else {
            dialog.setContentView(R.layout.sucess_dialog_box);
        }
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

        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        Button cancelBtn= (Button) dialog.findViewById(R.id.cancelBtn);

        okBtn.setText(button1);
        cancelBtn.setText(button2);

        alertMessage.setText(msg);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                WishListFragment fragment = new WishListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, "WishListFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                        .commitAllowingStateLoss();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });

        dialog.show();

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

//                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        dialog.show();

    }


}
