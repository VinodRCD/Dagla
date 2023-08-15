package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.BrandsFragmentNew2;
import com.dagla.android.fragments.CartFragmentNew;
import com.dagla.android.fragments.CategoriesFragment;
import com.dagla.android.fragments.HomeFragment;
import com.dagla.android.fragments.HomeFragmentNew;
import com.dagla.android.fragments.HomeVisitServiceFragment;
import com.dagla.android.fragments.MyAccountFragment;
import com.dagla.android.fragments.ProductsFragment;
import com.dagla.android.fragments.ProductsFromHomeBannerFragment;
import com.dagla.android.fragments.ProductsFromHomeViewMoreFragment;
import com.dagla.android.fragments.TailoringFragment;
import com.dagla.android.fragments.VisitUsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

//import ly.count.android.sdk.Countly;
//import ly.count.android.sdk.RemoteConfig;

//import ly.count.android.sdk.Countly;
//import ly.count.android.sdk.RemoteConfig;
//import ly.count.android.sdk.messaging.CountlyPush;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Activity act;

    TextView lblTitle,lblWishlistCount,lblCartCount;

    EditText txtSearch;

    Button imgSearch,imgCart,imgBack2,imgSort,btnDone,imgFilter;
    ImageView imgDaglaLogo;
    ImageButton imgBack;

    RelativeLayout layoutWishList,layoutCart,titleLayout,searchLayout,pnlDim;

    public TabLayout tabLayout;

    BottomNavigationView bottomNavigation;

    String screenPosition;

//    private int[] tabIcons_en = {
//            R.drawable.tab_home_icon,
//            R.drawable.tab_brands_icon,
//            R.drawable.tab_categories_icon,
//            R.drawable.tab_visit_us_icon,
//            R.drawable.tab_account_icon
//    };

//    private int[] tabIcons_ar = {
//            R.drawable.tab_account_icon,
//            R.drawable.tab_categories_icon,
//            R.drawable.tab_brands_icon,
//            R.drawable.tab_home_icon
//    };

//    private int[] tabIcons_ar = {
//            R.drawable.tab_home_icon,
//            R.drawable.tab_brands_icon,
//            R.drawable.tab_categories_icon,
//            R.drawable.tab_visit_us_icon,
//            R.drawable.tab_account_icon
//    };
//
//
//    private String[] tabLabels_en = {"Home","Brands","Categories","Tailoring","My Account"};
//
////    private String[] tabLabels_ar = {"حسابي","الأقسام","ماركات","الرئسيه"};
//
//    private String[] tabLabels_ar = {"الرئسيه","ماركات","الأقسام","تفصال ","حسابي"};

    final String COUNTLY_SERVER_URL = "https://dagla.count.ly";
    final String COUNTLY_APP_KEY = "43744905b9d494a663d22beafbdaee47bcfb05f5";

    Dialog dlgLoading = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //

        if(GlobalFunctions.getLang(MainActivity.this).equals("ar")){
            setContentView(R.layout.main_activity_ar_new);
        }else {
            setContentView(R.layout.main_activity_new);
        }

//        facebookHashKey();




        //
        act = this;

        Context appC = getApplicationContext();

        HashMap<String, String> customHeaderValues = new HashMap<>();
        customHeaderValues.put("foo", "bar");

//        Countly.onCreate(this);
//        Countly.sharedInstance().setLoggingEnabled(true);
//        Countly.sharedInstance().enableCrashReporting();
//        Countly.sharedInstance().setViewTracking(true);
//        Countly.sharedInstance().setAutoTrackingUseShortName(true);
//        Countly.sharedInstance().setRequiresConsent(true);
//        Countly.sharedInstance().addCustomNetworkRequestHeaders(customHeaderValues);
//        Countly.sharedInstance().setRemoteConfigAutomaticDownload(true, new RemoteConfig.RemoteConfigCallback() {
//            @Override
//            public void callback(String error) {
//                if(error == null) {
////                    Toast.makeText(act, "Automatic remote config download has completed", Toast.LENGTH_LONG).show();
//                } else {
////                    Toast.makeText(act, "Automatic remote config download encountered a problem, " + error, Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        Countly.sharedInstance().setConsent(new String[]{Countly.CountlyFeatureNames.push, Countly.CountlyFeatureNames.sessions, Countly.CountlyFeatureNames.location, Countly.CountlyFeatureNames.attribution, Countly.CountlyFeatureNames.crashes, Countly.CountlyFeatureNames.events, Countly.CountlyFeatureNames.starRating, Countly.CountlyFeatureNames.users, Countly.CountlyFeatureNames.views}, true);
//        //Countly.sharedInstance().setConsent(new String[]{Countly.CountlyFeatureNames.push, Countly.CountlyFeatureNames.sessions, Countly.CountlyFeatureNames.location, Countly.CountlyFeatureNames.attribution, Countly.CountlyFeatureNames.crashes, Countly.CountlyFeatureNames.events, Countly.CountlyFeatureNames.starRating, Countly.CountlyFeatureNames.users, Countly.CountlyFeatureNames.views}, false);
//        //Countly.sharedInstance().setHttpPostForced(true);
//        //Log.i(demoTag, "Before calling init. This should return 'false', the value is:" + Countly.sharedInstance().isInitialized());
//        Countly.sharedInstance().init(appC, COUNTLY_SERVER_URL, COUNTLY_APP_KEY);




        idMappings();
        setOnClickListeners();


//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
//
//        if(GlobalFunctions.getLang(MainActivity.this).equals("ar")){
//            tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        }else {
//            tabLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        }
//
//
//        // loop through all navigation tabs
//        for (int i = 0; i < tabLayout.getTabCount(); i++) {
//            // inflate the Parent LinearLayout Container for the tab
//            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
//
//            LinearLayout tab;
//
//            if(GlobalFunctions.getLang(act).equals("ar")){
//                tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_layout_ar, null);
//            }else {
//                tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_layout, null);
//            }
//
//
//            // get child TextView and ImageView from this layout for the icon and label
//            TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
//            ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);
//
//            // set the label text by getting the actual string value by its id
//            // by getting the actual resource value `getResources().getString(string_id)`
//
//            if(GlobalFunctions.getLang(act).equals("ar")){
//                tab_label.setText(tabLabels_ar[i]);
//                tab_icon.setImageResource(tabIcons_ar[i]);
//            }else {
//                tab_label.setText(tabLabels_en[i]);
//                tab_icon.setImageResource(tabIcons_en[i]);
//            }
//
//
//            tab_label.setTextColor(Color.parseColor("#FFFFFF"));
//
//            // finally publish this custom view to navigation tab
//            tabLayout.getTabAt(i).setCustomView(tab);
//        }
//
//
//
////        if(GlobalFunctions.getLang(act).equals("ar")){
////            TabLayout.Tab tab = tabLayout.getTabAt(3);
////            tab.select();
////        }else {
////            TabLayout.Tab tab = tabLayout.getTabAt(0);
////            tab.select();
////        }
//
//
////        tabLayout.addTab(tabLayout.newTab().setText("Home").setIcon(R.drawable.icon_cart));
////        tabLayout.addTab(tabLayout.newTab().setText("Brands").setIcon(R.drawable.icon_cart));
////        tabLayout.addTab(tabLayout.newTab().setText("Categories").setIcon(R.drawable.icon_cart));
////        tabLayout.addTab(tabLayout.newTab().setText("Account").setIcon(R.drawable.icon_cart));
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
////                if(GlobalFunctions.getLang(act).equals("ar")){
////                    if(tabLayout.getSelectedTabPosition() == 3){
////                        changeFragment(new HomeFragment());
////                    }else if(tabLayout.getSelectedTabPosition() == 2){
////                        changeFragment(new BrandsFragmentNew2());
////                    }else if(tabLayout.getSelectedTabPosition() == 1){
////                        changeFragment(new CategoriesFragment());
////                    }else if(tabLayout.getSelectedTabPosition() == 0){
////                        changeFragment(new MyAccountFragment());
////
////                    }
////                }else {
//                    if(tabLayout.getSelectedTabPosition() == 0){
//                        changeFragment(new HomeFragment());
//                    }else if(tabLayout.getSelectedTabPosition() == 1){
//                        changeFragment(new BrandsFragmentNew2());
//                    }else if(tabLayout.getSelectedTabPosition() == 2){
//                        changeFragment(new CategoriesFragment());
//                    }else if(tabLayout.getSelectedTabPosition() == 3){
////                        changeFragment(new VisitUsFragment());
//
//                        changeFragment(new HomeVisitServiceFragment());
////                        if (GlobalFunctions.getLang(MainActivity.this).equals("ar")) {
////                            GlobalFunctions.showToastError(MainActivity.this, "هذه الخدمة غير متاحة حالياً");
////                        }else {
////                            GlobalFunctions.showToastError(MainActivity.this, "This service is no longer available for now");
////                        }
//
//                    }else if(tabLayout.getSelectedTabPosition() == 4){
//                        changeFragment(new MyAccountFragment());
//
//                    }
////                }
//
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
////                if(GlobalFunctions.getLang(act).equals("ar")){
////                    if(tabLayout.getSelectedTabPosition() == 3){
////                        changeFragment(new HomeFragment());
////                    }else if(tabLayout.getSelectedTabPosition() == 2){
////                        changeFragment(new BrandsFragmentNew2());
////                    }else if(tabLayout.getSelectedTabPosition() == 1){
////                        changeFragment(new CategoriesFragment());
////                    }else if(tabLayout.getSelectedTabPosition() == 0){
////                        changeFragment(new MyAccountFragment());
////
////                    }
////                }else {
//
//                if(imgDaglaLogo.getVisibility()!=View.VISIBLE){
//                    if(tabLayout.getSelectedTabPosition() == 0){
//                        changeFragment(new HomeFragment());
//                    }else if(tabLayout.getSelectedTabPosition() == 1){
//                        changeFragment(new BrandsFragmentNew2());
//                    }else if(tabLayout.getSelectedTabPosition() == 2){
//                        changeFragment(new CategoriesFragment());
//                    }else if(tabLayout.getSelectedTabPosition() == 3){
////                        changeFragment(new VisitUsFragment());
//
//                        changeFragment(new HomeVisitServiceFragment());
//
////                        if (GlobalFunctions.getLang(MainActivity.this).equals("ar")) {
////                            GlobalFunctions.showToastError(MainActivity.this, "هذه الخدمة غير متاحة حالياً");
////                        }else {
////                            GlobalFunctions.showToastError(MainActivity.this, "This service is no longer available for now");
////                        }
//
//                    }else if(tabLayout.getSelectedTabPosition() == 4){
//                        changeFragment(new MyAccountFragment());
//
//                    }
//                }
//
////                }
//
//            }
//        });


//        if(pnlDim.getVisibility()==View.GONE){
            bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
            bottomNavigation.setOnNavigationItemReselectedListener(navigationItemReselectedListener);
//        }




        registerDevice();
//        changeFragment(new HomeFragment());
//        changeFragment(new HomeFragmentNew());


        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String s = txtSearch.getText().toString();

                    if (s.length() > 0) {

                        handled = true;

//                        Intent intent = new Intent(act, ItemsActivity.class);
//
//                        intent.putExtra("title", s);
//                        intent.putExtra("search", s);
//
//                        startActivity(intent);

                        searchLayout.setVisibility(View.GONE);
                        pnlDim.setVisibility(View.GONE);
                        titleLayout.setVisibility(View.VISIBLE);

                        txtSearch.setText("");

//                        HashMap<String, String> segmentation = new HashMap<String, String>();
//                        segmentation.put("Searched With", ""+s);
//
//                        Countly.sharedInstance().recordEvent("Searched Mostly", segmentation, 1);


                        ProductsFragment fragment = new ProductsFragment();
                        Bundle b = new Bundle();
                        b.putString("search", s);
                        b.putString("title", s);

                        fragment.setArguments(b);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment, "ProductsFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }
                }

                return handled;
            }
        });
    }


    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(pnlDim.getVisibility()==View.GONE){
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
//                                changeFragment(new HomeFragment());
                                changeFragment(new HomeFragmentNew());
                                return true;
//                            case R.id.navigation_brands:
//                                changeFragment(new BrandsFragmentNew2());
//                                return true;
                            case R.id.navigation_categories:
                                changeFragment(new CategoriesFragment());
                                return true;
                            case R.id.navigation_visit_us:
//                                changeFragment(new HomeVisitServiceFragment());
                                changeFragment(new TailoringFragment());
                                return true;
                            case R.id.navigation_my_account:
                                changeFragment(new MyAccountFragment());
                                return true;
                        }
                    }

                    return false;
                }
            };



    BottomNavigationView.OnNavigationItemReselectedListener navigationItemReselectedListener =
            new BottomNavigationView.OnNavigationItemReselectedListener() {
                @Override
                public void onNavigationItemReselected(@NonNull MenuItem item) {
                    if(pnlDim.getVisibility()==View.GONE){
                        switch (item.getItemId()) {
                            case R.id.navigation_home:

                                if(imgDaglaLogo.getVisibility()!=View.VISIBLE){
//                                    changeFragment(new HomeFragment());
                                    changeFragment(new HomeFragmentNew());
                                }

                                break;

//                            case R.id.navigation_brands:
//                                if(imgDaglaLogo.getVisibility()!=View.VISIBLE){
//                                    changeFragment(new BrandsFragmentNew2());
//                                }
//
//                                break;

                            case R.id.navigation_categories:
                                if(imgDaglaLogo.getVisibility()!=View.VISIBLE){
                                    changeFragment(new CategoriesFragment());
                                }
                                break;

                            case R.id.navigation_visit_us:
                                if(imgDaglaLogo.getVisibility()!=View.VISIBLE){
//                                    changeFragment(new HomeVisitServiceFragment());
                                    changeFragment((new TailoringFragment()));
                                }

                                break;

                            case R.id.navigation_my_account:
                                if(imgDaglaLogo.getVisibility()!=View.VISIBLE){
                                    changeFragment(new MyAccountFragment());
                                }

                                break;

                        }
                    }

                }
            };
    
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void idMappings() {

        lblTitle = (TextView) findViewById(R.id.lblTitle);

        imgDaglaLogo = (ImageView) findViewById(R.id.imgDaglaLogo);
        imgSearch = (Button) findViewById(R.id.imgSearch);
        imgCart = (Button) findViewById(R.id.imgCart);
        imgBack = (ImageButton) findViewById(R.id.imgBack);
        imgSort = (Button) findViewById(R.id.imgSort);
        btnDone = (Button) findViewById(R.id.btnDone);
        imgFilter = (Button) findViewById(R.id.imgFilter);

        lblWishlistCount = (TextView) findViewById(R.id.lblWishlistCount);
        lblCartCount = (TextView) findViewById(R.id.lblCartCount);

        layoutWishList = (RelativeLayout) findViewById(R.id.layoutWishList);
        layoutCart = (RelativeLayout) findViewById(R.id.layoutCart);

//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        searchLayout = (RelativeLayout) findViewById(R.id.searchLayout);
        pnlDim = (RelativeLayout) findViewById(R.id.pnlDim);

        imgBack2 = (Button) findViewById(R.id.imgBack2);

        txtSearch = (EditText) findViewById(R.id.txtSearch);

        bottomNavigation = findViewById(R.id.bottom_navigation);


    }

    private void setOnClickListeners() {

//        imgDaglaLogo.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgCart.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgSort.setOnClickListener(this);

        pnlDim.setOnClickListener(this);
        imgBack2.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        imgFilter.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

//            case R.id.imgWishlist:
//
//                changeFragment(new WishListFragment());
//
//                break;

            case R.id.imgSearch:

                titleLayout.setVisibility(View.GONE);
                searchLayout.setVisibility(View.VISIBLE);
                pnlDim.setVisibility(View.VISIBLE);

                txtSearch.requestFocus();

                break;

            case R.id.pnlDim:

                searchLayout.setVisibility(View.GONE);
                pnlDim.setVisibility(View.GONE);
                titleLayout.setVisibility(View.VISIBLE);

                txtSearch.setText("");

                break;

            case R.id.imgBack2:

                searchLayout.setVisibility(View.GONE);
                pnlDim.setVisibility(View.GONE);
                titleLayout.setVisibility(View.VISIBLE);

                txtSearch.setText("");

                break;

            case R.id.imgCart:

//                CartFragment fragment = new CartFragment();
                CartFragmentNew fragment = new CartFragmentNew();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, "CartFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                        .commitAllowingStateLoss();

                break;

            case R.id.imgBack:

//                onBackPressed();

                super.onBackPressed();

                break;

            case R.id.btnDone:

//                onBackPressed();

                super.onBackPressed();

                break;

            case R.id.imgSort:

                if(screenPosition.equals("1")){
                    ProductsFragment fragment2 = (ProductsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    fragment2.productsSorting();
                }else if(screenPosition.equals("2")){
                    ProductsFromHomeViewMoreFragment fragment2 = (ProductsFromHomeViewMoreFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    fragment2.productsSorting();
                }else if(screenPosition.equals("3")){
                    ProductsFromHomeBannerFragment fragment2 = (ProductsFromHomeBannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    fragment2.productsSorting();
                }

                break;

            case R.id.imgFilter:

                if(screenPosition.equals("1")){
                    ProductsFragment fragment2 = (ProductsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    fragment2.filterDialog();
                }else if(screenPosition.equals("2")){
//                    ProductsFromHomeViewMoreFragment fragment2 = (ProductsFromHomeViewMoreFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//                    fragment2.filterDialog();
                }else if(screenPosition.equals("3")){
                    ProductsFromHomeBannerFragment fragment2 = (ProductsFromHomeBannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    fragment2.filterDialog();
                }

                break;

        }
    }

    public void setHeaders(String headerTitle ,boolean homeLogo, boolean searchBtn, boolean cartBtn, boolean backBtn, boolean sortBtn, String pos, boolean doneBtn) {

        lblTitle.setText(headerTitle);
//        title_name = headerTitle;
        screenPosition = pos;

        if (lblTitle.getText().toString().length() > 20) {
            lblTitle.setTextSize(14);
        }

        if(homeLogo){
            imgDaglaLogo.setVisibility(View.VISIBLE);
            lblTitle.setVisibility(View.GONE);
        }else {
            imgDaglaLogo.setVisibility(View.GONE);
            lblTitle.setVisibility(View.VISIBLE);
        }

        if(searchBtn){
            imgSearch.setVisibility(View.VISIBLE);
        }else {
            imgSearch.setVisibility(View.GONE);
        }

        if(cartBtn){
            layoutCart.setVisibility(View.VISIBLE);
        }else {
            layoutCart.setVisibility(View.GONE);
        }

        if(backBtn){
            imgBack.setVisibility(View.VISIBLE);
        }else {
            imgBack.setVisibility(View.GONE);
        }

        if(sortBtn){
            imgSort.setVisibility(View.VISIBLE);
            imgFilter.setVisibility(View.VISIBLE);
        }else {
            imgSort.setVisibility(View.GONE);
            imgFilter.setVisibility(View.GONE);
        }

        if(doneBtn){
            btnDone.setVisibility(View.VISIBLE);
        }else {
            btnDone.setVisibility(View.GONE);
        }

    }



    private void changeFragment(Fragment targetFragment) {

        if (targetFragment instanceof HomeFragment) {

            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, targetFragment, "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                    .commitAllowingStateLoss();

        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, targetFragment, "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                    .commitAllowingStateLoss();

        }
    }

    public void setCartCount() {
        //
        int cnt = GlobalFunctions.getCartCount();
        //
        if (cnt > 0) {
            //
            lblCartCount.setText(String.valueOf(cnt));
            lblCartCount.setVisibility(View.VISIBLE);
            //
        } else {
            //
            lblCartCount.setText("");
            lblCartCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {

//        super.onBackPressed();

    }


    @Override
    public void onStart()
    {
        super.onStart();
//        Countly.sharedInstance().onStart(this);
    }

    @Override
    public void onStop()
    {
//        Countly.sharedInstance().onStop();
        super.onStop();
    }


    private void facebookHashKey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.dagla.android", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashCode  = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                System.out.println("Print the hashKey for Facebook :"+hashCode);
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }



    private void registerDevice() {


        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("androidToken", GlobalFunctions.getPrefrences(act, "token"));
            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "adddevicetoken", params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                    //
                    hideLoading();
                    //
                    JSONObject obj;
                    //
                    String response = new String(bytes);
                    //
                    Log.d("onSuccess", response);
                    //
                    try {
                        //
                        obj = ((JSONObject) new JSONTokener(response).nextValue()).getJSONObject("result");
                        //
                        if (obj.getString("status").equalsIgnoreCase("1")) {


                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }
                        //
                    } catch (JSONException e) {

                        if (GlobalFunctions.getLang(act).equals("ar")) {
                            GlobalFunctions.showToastError(act, getString(R.string.msg_error_ar));
                        }else {
                            GlobalFunctions.showToastError(act, getString(R.string.msg_error));
                        }

//                    } catch (ClassNotFoundException e) {

                        //
                    }
                    //
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    if (GlobalFunctions.getLang(act).equals("ar")) {
                        GlobalFunctions.showToastError(act, getString(R.string.msg_error_ar));
                    }else {
                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));
                    }
                }
            });

//            if (getIntent().hasExtra("Title")) {
//                Bundle b = getIntent().getExtras();
//
//                assert b != null;
//                if(Objects.equals(b.getString("Title"), getString(R.string.shop))){
//                    bottomNavigation.setSelectedItemId(R.id.navigation_home);
//                    changeFragment(new HomeFragmentNew());
//                }else {
//                    bottomNavigation.setSelectedItemId(R.id.navigation_visit_us);
//                    changeFragment(new TailoringFragment());
//                }
//
//            }

            if (GlobalFunctions.getPrefrences(MainActivity.this, "landing").equals("Shop")) {
                bottomNavigation.setSelectedItemId(R.id.navigation_home);
                changeFragment(new HomeFragmentNew());
            }else {
                bottomNavigation.setSelectedItemId(R.id.navigation_visit_us);
                changeFragment(new TailoringFragment());
            }

//            changeFragment(new HomeFragment());
        } else {

            if (GlobalFunctions.getLang(act).equals("ar")) {
                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));
            }

        }

    }

    private void showLoading() {

        if (dlgLoading == null) {

            dlgLoading = GlobalFunctions.showLoading(act);

        } else {

            dlgLoading.show();
        }

    }

    private void hideLoading() {

        dlgLoading.dismiss();

    }
}
