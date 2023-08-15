package com.dagla.android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;

//import ly.count.android.sdk.Countly;

//import ly.count.android.sdk.Countly;
//import ly.count.android.sdk.RemoteConfig;


public class SplashActivity extends AppCompatActivity {

    Activity activity;

    final String COUNTLY_SERVER_URL = "https://dagla.count.ly";
    final String COUNTLY_APP_KEY = "43744905b9d494a663d22beafbdaee47bcfb05f5";
    String currentVersion, latestVersion;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        GlobalFunctions.setLanguage(this);

        setContentView(R.layout.activity_splash);

//        Resources res = this.getResources();
//        Configuration configuration = res.getConfiguration();
//        Locale newLocale = new Locale("en");
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            configuration.setLocale(newLocale);
//            LocaleList localeList = new LocaleList(newLocale);
//            LocaleList.setDefault(localeList);
//            configuration.setLocales(localeList);
//            this.createConfigurationContext(configuration);
//
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            configuration.setLocale(newLocale);
//            this.createConfigurationContext(configuration);
//
//        } else {
//            configuration.locale = newLocale;
//            res.updateConfiguration(configuration, res.getDisplayMetrics());
//        }

        activity = this;

        if(GlobalFunctions.getPrefrences(this, "CountryCurrency").equals("")){
            SharedPreferences sharedPreferences = this.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("CountryNameAr","الكويت");
            editor.putString("CountryName","Kuwait");
            editor.putString("CountryCurrency","KWD");
            editor.putString("CountryFlag","https://portal.dagla.com/thumbnail.ashx?Width=129&Height=87&Cat=c&Image=file/country_icons/Kuwait.jpeg");
            editor.commit();
        }else {
//            params.put("curr", "KWD");
        }






        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Glide.with(this)
                .load(R.drawable.splash_screen_05_mar_2023)
//                .placeholder(R.drawable.place_holder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

//                        progress1.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        progress1.setVisibility(View.GONE);
                        return false;
                    }


                })
                .into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                Intent mainIntent = new Intent(SplashActivity.this, LanguageSelectionActivity.class);

                if (!GlobalFunctions.getPrefrences(SplashActivity.this, "intro").equals("1")) {
//                    Intent mainIntent = new Intent(SplashActivity.this, IntroductionActivity.class);
                    Intent mainIntent = new Intent(SplashActivity.this, IntroductionActivity2.class);
                    startActivity(mainIntent);
                    finish();
                }else {

                    if (!GlobalFunctions.getPrefrences(SplashActivity.this, "user_id").equalsIgnoreCase("")) {
                        startActivity(new Intent(SplashActivity.this, LandingActivity.class));
                        finish();

                    }else {
                        startActivity(new Intent(SplashActivity.this, StartingActivity.class));
                        finish();

                    }

//                    Intent mainIntent = new Intent(SplashActivity.this, StartingActivity.class);
//                    startActivity(mainIntent);
//                    finish();
                }
            }
        }, 3000);

//        forceUpdate();

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



    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;

        public ForceUpdateAsync(String currentVersion, Context context) {
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&hl=en")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select("div[itemprop=softwareVersion]")
//                        .first()
//                        .ownText();
                        .timeout(60000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {

                if (!currentVersion.equalsIgnoreCase(latestVersion)){
                    // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
//                    if(!(context instanceof SplashActivity)) {
//                        if(!((Activity)context).isFinishing()){

                    if(!isFinishing()) {
                        showForceUpdateDialog();
                    }


//                        }
//                    }
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
//                Intent mainIntent = new Intent(SplashActivity.this, LanguageSelectionActivity.class);

                            if (!GlobalFunctions.getPrefrences(SplashActivity.this, "intro").equals("1")) {
                                Intent mainIntent = new Intent(SplashActivity.this, IntroductionActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }else {

                                if (!GlobalFunctions.getPrefrences(SplashActivity.this, "user_id").equalsIgnoreCase("")) {
                                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                    finish();

                                }else {
                                    startActivity(new Intent(SplashActivity.this, StartingActivity.class));
                                    finish();

                                }

//                    Intent mainIntent = new Intent(SplashActivity.this, StartingActivity.class);
//                    startActivity(mainIntent);
//                    finish();
                            }
                        }
                    }, 3000);

                }
            }
            super.onPostExecute(jsonObject);
        }

    }

    // check version on play store and force update
    public void forceUpdate(){
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;

        if (GlobalFunctions.hasConnection(SplashActivity.this)) {
            new ForceUpdateAsync(currentVersion,SplashActivity.this).execute();
        }else {

            if (GlobalFunctions.getLang(SplashActivity.this).equals("ar")) {
                GlobalFunctions.showToastError(SplashActivity.this, getString(R.string.msg_no_internet_ar));
            }else {
                GlobalFunctions.showToastError(SplashActivity.this, getString(R.string.msg_no_internet));
            }

        }

    }


    public void showForceUpdateDialog(){


        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.app_update_dialog_box);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        final TextView alertTitle = (TextView) dialog.findViewById(R.id.alertTitle);
        final TextView alertMessage = (TextView) dialog.findViewById(R.id.alertMessage);

        alertTitle.setText(SplashActivity.this.getString(R.string.app_name));
        alertMessage.setText("A New Update is Available");


        TextView okText = (TextView) dialog.findViewById(R.id.okText);
        TextView cancelText = (TextView) dialog.findViewById(R.id.cancelText);

        okText.setText("Update");
        cancelText.setText("Cancel");

        okText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + SplashActivity.this.getPackageName())));
                finish();
            }
        });

        cancelText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                finish();
            }
        });

        dialog.show();
    }


}
