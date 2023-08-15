package com.dagla.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Activity act;
    DrawerLayout drawer;
    NavigationView navigationView;
    ImageView imgCart, imgSearch, imgCloseMenu, imgBack;
    TextView lblCartCount, lblTitle;
    ListView lv, lvMenu;
    EditText txtSearch;
    LinearLayout pnlActionBarButtons;
    RelativeLayout pnlDim;
    Toolbar toolbar;

    Dialog dlgLoading = null;

    ArrayList<String> arrList, arrListMenu, arrListMenuFiltered;

    ArrayAdapter<String> arrAdapter, arrAdapterMenu;

    String lang = "en";

    String currentMenuIndex = "-1", currentMenuSubIndex = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);
        //
        setContentView(R.layout.activity_main);
        //
        act = this;
        navigationView = findViewById(R.id.nav_view);
        imgCart = findViewById(R.id.imgCart);
        imgSearch = findViewById(R.id.imgSearch);
        imgCloseMenu = findViewById(R.id.imgCloseMenu);
        imgBack = findViewById(R.id.imgBack);
        lblCartCount = findViewById(R.id.lblCartCount);
        lblTitle = findViewById(R.id.lblTitle);
        lv = findViewById(R.id.lv);
        lvMenu = findViewById(R.id.lvMenu);
        txtSearch = findViewById(R.id.txtSearch);
        pnlActionBarButtons = findViewById(R.id.pnlActionBarButtons);
        pnlDim = findViewById(R.id.pnlDim);
        //
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        //
        drawer = findViewById(R.id.drawer_layout);
        //
        ActionBarDrawerToggle toggle =
            new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //
        drawer.addDrawerListener(toggle);
        //
        toggle.syncState();
        //
        lang = GlobalFunctions.getLang(act);
        //
        if (lang.equalsIgnoreCase("ar")) {

            toolbar.setNavigationIcon(R.drawable.icon_menu_ar);

            imgBack.setImageResource(R.drawable.icon_back_ar);

        } else {

            toolbar.setNavigationIcon(R.drawable.icon_menu);

            imgBack.setImageResource(R.drawable.icon_back);
        }
        //
        GlobalFunctions.initImageLoader(act);
        //
        setCartCount();
        //
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(act, CartActivity.class));

            }
        });
        //
        imgCloseMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });
        //
        pnlDim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch();
            }
        });
        //
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSearch();
            }
        });
        //
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pnlDim.setVisibility(View.VISIBLE);

                imgBack.setVisibility(View.VISIBLE);

                txtSearch.setVisibility(View.VISIBLE);

                lblTitle.setVisibility(View.GONE);

                pnlActionBarButtons.setVisibility(View.GONE);

                toolbar.setNavigationIcon(null);

                txtSearch.requestFocus();

            }
        });
        //
        txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                boolean handled = false;

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String s = txtSearch.getText().toString();

                    if (s.length() > 0) {

                        handled = true;

                        Intent intent = new Intent(act, ItemsActivity.class);

                        intent.putExtra("title", s);
                        intent.putExtra("search", s);

                        startActivity(intent);

                    }
                }

                return handled;
            }
        });
        //
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                arg1.setEnabled(false);

                try {

                    JSONObject obj = new JSONObject(lv.getItemAtPosition(position).toString());

                    if (!obj.getString("cat_id").equalsIgnoreCase("")) {

                        Intent intent = new Intent(act, ItemsActivity.class);

                        intent.putExtra("cat_id", obj.getString("cat_id"));
                        intent.putExtra("title", obj.getString("cat"));

                        startActivity(intent);

                    } else if (!obj.getString("product_id").equalsIgnoreCase("")) {

                        Intent intent = new Intent(act, ItemDetailsActivity.class);

                        intent.putExtra("obj", obj.toString());

                        startActivity(intent);
                    }

                } catch (JSONException e) {

                    Log.d("JSONException", e.getMessage());
                }

                arg1.setEnabled(true);

            }
        });
        //
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                arg1.setEnabled(false);

                try {

                    JSONObject obj = new JSONObject(lvMenu.getItemAtPosition(position).toString());

                    String type = obj.getString("type");

                    if (type.equalsIgnoreCase("shop_by") || type.equalsIgnoreCase("shop_first")) {

                        if (currentMenuIndex.equalsIgnoreCase(obj.getString("id"))) {

                            currentMenuIndex = "-1";
                            currentMenuSubIndex = "-1";

                        } else {

                            currentMenuIndex = obj.getString("id");

                        }

                        checkCurrentMenuItems(true);

                    } else if (type.equalsIgnoreCase("shop_1st")) {

                        if (currentMenuSubIndex.equalsIgnoreCase(obj.getString("id"))) {

                            currentMenuSubIndex = "-1";

                        } else {

                            currentMenuSubIndex = obj.getString("id");

                        }

                        checkCurrentMenuItems(true);

                    } else if (type.equalsIgnoreCase("shop")) {

                        Intent intent = new Intent(act, ItemsActivity.class);

                        intent.putExtra("cat_id", "0");
                        intent.putExtra("sub_cat_id", obj.getString("id"));
                        intent.putExtra("title", obj.getString("name"));

                        startActivity(intent);

                    } else if (type.equalsIgnoreCase("login")) {

                        startActivity(new Intent(act, LoginActivity.class));

                    } else if (type.equalsIgnoreCase("register")) {

                        startActivity(new Intent(act, RegisterActivity.class));

                    } else if (type.equalsIgnoreCase("my_account")) {

                        currentMenuSubIndex = "-1";

                        if (currentMenuIndex.equalsIgnoreCase(obj.getString("id"))) {

                            currentMenuIndex = "-1";

                        } else {

                            currentMenuIndex = obj.getString("id");

                        }

                        checkCurrentMenuItems(true);

                    } else if (type.equalsIgnoreCase("my_profile")) {

                        startActivity(new Intent(act, MyProfileActivity.class));

                    } else if (type.equalsIgnoreCase("my_addresses")) {

                        startActivity(new Intent(act, MyAddressesActivity.class));

                    } else if (type.equalsIgnoreCase("my_orders")) {

                        startActivity(new Intent(act, MyOrdersActivity.class));

                    } else if (type.equalsIgnoreCase("change_password")) {

                        startActivity(new Intent(act, ChangePasswordActivity.class));

                    } else if (type.equalsIgnoreCase("logout")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(act);

                        builder.setMessage(getString(R.string.are_you_sure_you_want_to_logout));

                        builder.setPositiveButton(getString(R.string.yes).toUpperCase(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                GlobalFunctions.setPrefrences(act, "id", "");
                                GlobalFunctions.setPrefrences(act, "user_id", "");
                                GlobalFunctions.setPrefrences(act, "name", "");
                                GlobalFunctions.setPrefrences(act, "email", "");

                                checkLoggedIn();

                            }
                        });

                        builder.setNegativeButton(getString(R.string.no).toUpperCase(), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                //
                            }
                        });

                        AlertDialog dialog = builder.create();

                        dialog.show();

                    } else if (type.equalsIgnoreCase("contact_us")) {

                        startActivity(new Intent(act, ContactUsActivity.class));

                    } else if (type.equalsIgnoreCase("lang")) {

                        if (GlobalFunctions.getLang(act).equalsIgnoreCase("ar")) {

                            GlobalFunctions.setPrefrences(act, "lang", "en");

                        } else {

                            GlobalFunctions.setPrefrences(act, "lang", "ar");

                        }

                        GlobalFunctions.setLanguage(act);

                        act.startActivity(new Intent(act, MainActivity.class));
                        act.finishAffinity();

                    }

                } catch (JSONException e) {

                    Log.d("JSONException", e.getMessage());
                }

                arg1.setEnabled(true);

            }
        });
        //
        loadData();
    }

    private void loadData() {

        if (GlobalFunctions.hasConnection(act)) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());

            client.get(GlobalFunctions.serviceURL + "getHomeData", params, new AsyncHttpResponseHandler() {

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

                            DisplayMetrics dm = new DisplayMetrics();

                            act.getWindowManager().getDefaultDisplay().getMetrics(dm);

                            arrAdapter = new HomeBannersAdapter(act, arrList, dm.widthPixels);

                            lv.setAdapter(arrAdapter);

                            arrListMenu = new ArrayList<String>();
                            arrListMenuFiltered = new ArrayList<String>();

                            arrListMenu.add(
                                putMultiple(new String[] {"id", "name", "is_heading", "type"},
                                            new String[] {"0", getString(R.string.shop_by), "1", "shop_by"}).toString());

                            arr = obj.getJSONArray("cats");

                            for (int i = 0; i < arr.length(); i++) {

                                obj1 = arr.getJSONObject(i);

                                arrListMenu.add(
                                    putMultiple(new String[] {"id", "name", "parent_id", "is_heading", "type"},
                                                new String[] {obj1.getString("cat_id"),
                                                                obj1.getString("cat"), "0", "0",
                                                                "shop_1st"}).toString());

                                arr1 = obj1.getJSONArray("sub_cats");

                                for (int j = 0; j < arr1.length(); j++) {

                                    obj2 = arr1.getJSONObject(j);

                                    arrListMenu.add(
                                        putMultiple(new String[] {"id", "name", "parent_id", "is_heading", "type"},
                                                    new String[] {obj2.getString("cat_id"),
                                                                    obj2.getString("cat"),
                                                                    obj1.getString("cat_id"),
                                                                    "0", "shop"}).toString());

                                }
                            }

                            arrListMenu.add(
                                putMultiple(new String[] {"id", "name", "is_heading", "type"},
                                            new String[] {"1", getString(R.string.my_account), "1", "my_account"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"name", "is_heading", "parent_id", "type"},
                                            new String[] {getString(R.string.my_profile), "0", "1", "my_profile"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"name", "is_heading", "parent_id", "type"},
                                            new String[] {getString(R.string.my_addresses), "0", "1", "my_addresses"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"name", "is_heading", "parent_id", "type"},
                                            new String[] {getString(R.string.my_orders), "0", "1", "my_orders"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"name", "is_heading", "parent_id", "type"},
                                            new String[] {getString(R.string.change_password), "0", "1", "change_password"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"name", "is_heading", "parent_id", "type"},
                                            new String[] {getString(R.string.logout), "0", "1", "logout"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"id", "name", "is_heading", "type"},
                                            new String[] {"2", getString(R.string.login), "1", "login"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"id", "name", "is_heading", "type"},
                                            new String[] {"3", getString(R.string.register), "1", "register"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"id", "name", "is_heading", "type"},
                                            new String[] {"4", getString(R.string.contact_us), "1", "contact_us"}).toString());

                            arrListMenu.add(
                                putMultiple(new String[] {"id", "name", "is_heading", "type"},
                                            new String[] {"5", getString(R.string.language), "1", "lang"}).toString());

                            checkCurrentMenuItems(false);

                            arrAdapterMenu = new M3nuAdapter(act, arrListMenuFiltered);

                            lvMenu.setAdapter(arrAdapterMenu);

                        } else {

                            GlobalFunctions.showToastError(act, obj.getString("msg"));

                        }

                    } catch (JSONException e) {

                        GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                    hideLoading();

                    GlobalFunctions.showToastError(act, getString(R.string.msg_error));

                }
            });

        } else {

            GlobalFunctions.showToastError(act, getString(R.string.msg_no_internet));

        }

    }

    private JSONObject putMultiple(String[] name, String[] val) {

        JSONObject obj = new JSONObject();

        for (int i=0; i<name.length; i++) {

            try {

                obj.put(name[i], val[i]);

            } catch (JSONException e) {

                Log.d("JSONException", e.getMessage());
            }

        }

        return obj;

    }

    private void checkCurrentMenuItems(boolean notifyChange) {

        String id = "", parentId = "";

        String userId = GlobalFunctions.getPrefrences(act, "user_id");

        arrListMenuFiltered.clear();

        try {

            for (int i=0; i<arrListMenu.size(); i++) {

                JSONObject obj = new JSONObject(arrListMenu.get(i));

                if (obj.getString("is_heading").equalsIgnoreCase("1")) {

                    id = obj.getString("id");

                    if (id.equalsIgnoreCase("1") || id.equalsIgnoreCase("2") ||
                            id.equalsIgnoreCase("3")) {

                        if (id.equalsIgnoreCase("1") && !userId.equalsIgnoreCase("")) {

                            arrListMenuFiltered.add(obj.toString());

                        } else if ((id.equalsIgnoreCase("2") || id.equalsIgnoreCase("3")) &&
                                    userId.equalsIgnoreCase("")) {

                            arrListMenuFiltered.add(obj.toString());

                        }

                    } else {

                        arrListMenuFiltered.add(obj.toString());

                    }

                } else if (!currentMenuIndex.equalsIgnoreCase("-1") ||
                            !currentMenuSubIndex.equalsIgnoreCase("-1")) {

                    Log.d("currentMenuIndex", currentMenuIndex);
                    Log.d("currentMenuSubIndex", currentMenuSubIndex);

                    parentId = obj.getString("parent_id");

                    if (parentId.equalsIgnoreCase(currentMenuIndex) ||
                            parentId.equalsIgnoreCase(currentMenuSubIndex)) {

                        arrListMenuFiltered.add(obj.toString());

                    }

                }

            }

        } catch (JSONException e) {

            Log.d("JSONException", e.getMessage());
        }

        if (notifyChange) {

            arrAdapterMenu.notifyDataSetChanged();

        }

    }

    private void setCartCount() {
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

    private void checkLoggedIn() {

        if (arrListMenuFiltered != null) {

            currentMenuIndex = "-1";
            currentMenuSubIndex = "-1";

            checkCurrentMenuItems(true);

        }

    }

    private void closeSearch() {

        pnlDim.setVisibility(View.GONE);

        imgBack.setVisibility(View.GONE);

        txtSearch.setText("");

        txtSearch.setVisibility(View.GONE);

        lblTitle.setVisibility(View.VISIBLE);

        pnlActionBarButtons.setVisibility(View.VISIBLE);

        if (lang.equalsIgnoreCase("ar")) {

            toolbar.setNavigationIcon(R.drawable.icon_menu_ar);

        } else {

            toolbar.setNavigationIcon(R.drawable.icon_menu);
        }

    }

    @SuppressLint("WrongConstant")
    private void closeDrawer() {

        drawer.closeDrawer(GravityCompat.START);

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

    @Override
    protected void onResume() {

        super.onResume();

        setCartCount();

        checkLoggedIn();
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
