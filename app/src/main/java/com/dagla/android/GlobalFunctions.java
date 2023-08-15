package com.dagla.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.LocaleList;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GlobalFunctions {

    private static final double appVersion = 1.0;

    public static final double appVersion2 = 1.0;

//    public static String baseURL = "http://160.153.250.65:83/";
    public static String baseURL = "http://portal.dagla.com/";
    //public static String baseURL = "http://10.211.55.3/dagla/";

    public static String serviceURL = baseURL + "services/ajax_v1.aspx?app=ios&lang=en&ver="+ appVersion + "&cat=";
//    public static String serviceURL = baseURL + "services/ajax_v2.aspx?app=android&lang=en&ver="+ appVersion + "&cat=";
//    public static String serviceURL = "https://portal.dagla.com/Staging/services/ajax_v2.aspx?app=android&lang=en&ver="+ appVersion + "&cat=";
    public static String cartIds = "";

//    https://portal.dagla.com/services/ajax_v2.aspx?app=ios&lang=en&ver=1.0&cat=getCurrencyList&ran=71


    ArrayList<String> colorIdList = new ArrayList<>();
    ArrayList<String> colorNameList = new ArrayList<>();

    ArrayList<String> sizeIdList = new ArrayList<>();
    ArrayList<String> sizeNameList = new ArrayList<>();

    public static void setPrefrences(Context context, String name, String val) {

        SharedPreferences settings = context.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(name, val);

        editor.commit();
    }

    public static void setPrefrences(Context context, String[] name, String[] val) {

        SharedPreferences settings = context.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        for (int i=0; i<name.length; i++) {

            editor.putString(name[i], val[i]);

        }

        editor.commit();
    }

    public static String getPrefrences(Context context, String name) {

        SharedPreferences settings = context.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        return settings.getString(name, "");

    }

    public static String getCountryPrefrences(Context context, String name) {

        SharedPreferences settings = context.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        return settings.getString(name, "");

    }

    public static String getRandom() {

        Random ran = new Random();
        return String.valueOf(ran.nextInt(1000));

    }

    public static void showToast(Context context, String msg) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        //
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextSize(18);
        //
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }

    public static void showToastSuccess(Context context, String msg) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        //
        View v = toast.getView();
        v.setBackgroundResource(R.drawable.bg_success);
        v.setPadding(50, 25, 50, 25);
        //
        TextView txt = (TextView)v.findViewById(android.R.id.message);
        txt.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        txt.setShadowLayer(0, 0, 0, 0);
        //
        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

    }

    public static void showToastError(Context context, String msg) {

//        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
//        //
//        View v = toast.getView();
//        v.setBackgroundResource(R.drawable.bg_error);
//        v.setPadding(50, 25, 50, 25);
//        //
//        TextView txt = (TextView)v.findViewById(android.R.id.message);
//        txt.setTextColor(ContextCompat.getColor(context, android.R.color.white));
//        txt.setShadowLayer(0,0,0,0);
//        //
//        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//        toast.show();

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog_box);
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

        if (getLang(context).equalsIgnoreCase("ar")) {
            okBtn.setText("تم");
        }else {
            okBtn.setText("Ok");
        }

        alertMessage.setText(msg);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        dialog.show();

    }

    public static void showPopup(Context context, String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(context.getString(R.string.ok), null);
        builder.show();

    }

    public static Dialog showLoading(Context context) {

        LayoutInflater factory = LayoutInflater.from(context);
        View DialogView = factory.inflate(R.layout.layout_progress_dialog, null);

        Dialog dlg = new Dialog(context, R.style.Theme_AppCompat_Dialog);

        dlg.setContentView(DialogView);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        return dlg;
    }

    public static void addToCart(String cartId) {

        if (cartIds.length() > 0) {

            cartIds = cartIds.concat(String.format(",%s", cartId));

        } else {

            cartIds = cartId;

        }
    }

    public static void deleteFromCart(String cartId) {

        if (cartIds.length() > 0) {

            cartIds = cartIds.concat(",");

            cartIds = cartIds.replace(String.format("%s,", cartId), "");

            if (cartIds.length() > 0) {

                cartIds = cartIds.substring(0, cartIds.length()-1);

            }

        }

    }

    public static void clearCart() {

        cartIds = "";
    }

    public static String getCart() {

        return cartIds;
    }

    public static int getCartCount() {

        if (cartIds.length() > 0) {

            String[] arr = cartIds.split(",");

            return arr.length;

        } else {

            return  0;

        }
    }

    public static String padLeft(int s) {
        if (s < 10) {
            return "0"+Integer.toString(s);
        } else {
            return Integer.toString(s);
        }
    }

    public static String formatNumber(String s) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###");
        //
        String r = "";
        //
        try {
            r = formatter.format(Integer.parseInt(s));
        } catch (Exception e) {
            r = s;
        }
        //
        return r;
    }

    public static int convertDpToPx(Context context, int dp) {
        return (int)(dp * context.getResources().getDisplayMetrics().density);
    }

    public static int convertPxToDp(Context context, int px) {
        return (int)(px / context.getResources().getDisplayMetrics().density);
    }

    public static boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }

    public static String getPath(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        //
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else {
            return null;
        }
    }

    public static int getActionBarSize(Context context) {
        TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();
        return actionBarSize;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNewHeightFromScreenWidth(Activity context, int w, int h) {

        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //
        int nw = displaymetrics.widthPixels;
        int nh = h;
        //
        if (nw != w) {
            //
            float ratio = (float)nw / (float)w;
            //
            nh = (int)(h * ratio);
        }
        //
        return  nh;

    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.bg_img)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .threadPoolSize(2)
                .threadPriority(Thread.MIN_PRIORITY + 3)
                .writeDebugLogs() // Remove for release app
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static String getLang(Context context) {

        String lang = GlobalFunctions.getPrefrences(context, "lang");

        if (lang.equalsIgnoreCase("")) {

            lang = "en";

        }

        return lang;

    }

    public static void setWebViewParams(WebView wv) {

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.getSettings().setSaveFormData(false);
        wv.getSettings().setSavePassword(false);
        wv.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.getSettings().setSupportZoom(false);

    }

    public static void setLanguage(Context context) {

        String lang = getPrefrences(context, "lang");

        lang  = (lang.equalsIgnoreCase("") ? "en" : lang);

        serviceURL = baseURL + "services/ajax_v2.aspx?app=android&ver=" + appVersion + "&lang=" + lang + "&cat=";


        Resources res = context.getResources();
        Configuration configuration = res.getConfiguration();
        Locale newLocale = new Locale("en");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(newLocale);
            LocaleList localeList = new LocaleList(newLocale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context.createConfigurationContext(configuration);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(newLocale);
            context.createConfigurationContext(configuration);

        } else {
            configuration.locale = newLocale;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }

    }


    public static void setLanguage2(Context context) {

        String lang = getPrefrences(context, "lang");

        lang  = (lang.equalsIgnoreCase("") ? "en" : lang);

        serviceURL = baseURL + "services/ajax_v2.aspx?app=android&ver=" + appVersion + "&lang=" + lang + "&cat=";
    }

    public static boolean isEmptyText(EditText txt) {

        if (txt.getText().toString().trim().equalsIgnoreCase("")) {

            return true;

        } else {

            return false;

        }

    }

    public static boolean isTextLengthLessThan(EditText txt, int len) {

        if (txt.getText().toString().trim().length() < len) {

            return true;

        } else {

            return false;

        }

    }

    public static boolean isTextNotEqualsText(EditText txt1, EditText txt2) {

        if (txt1.getText().toString().trim().equalsIgnoreCase(txt2.getText().toString().trim())) {

            return false;

        } else {

            return true;

        }

    }

    public static boolean containsSpaces(EditText txt) {

        if (txt.getText().toString().trim().contains(" ")) {

            return true;

        } else {

            return false;

        }

    }

    public static String checkQueryParam(String txt) {

        try {

            txt = URLEncoder.encode(txt,"UTF-8");

        } catch (UnsupportedEncodingException e) {


        }

        return txt;

    }


    public static int get_device_height(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int height = metrics.heightPixels;

        return height;
    }

    public static int get_device_width(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        int width = metrics.widthPixels;

        return width;
    }

}
