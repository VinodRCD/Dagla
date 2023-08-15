package com.dagla.android.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.CircleTransform;
import com.dagla.android.activity.IntroductionActivity;
import com.dagla.android.activity.IntroductionActivity2;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.activity.SplashActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MyAccountFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    Button btnLoginRegister,btnMyAccountDetails,btnReturnsAndExchanges,btnShippingInformation,btnPaymentSecurity,btnPrivacyPolicy,
            btnCookiePolicy,btnTermsAndConditions,btnFaqs,btnContactEmailUs,btnLogout,btnChangeLanguage,btnCallUs,btnMyLocation,btnAboutUs;

    TextView lblAppVersion,lblDesignedBy,lblHelpAndInformation,lblContactUs,lblHelpAdvice,lblAvailable,lblCountryName,lblLanguageName;

    RelativeLayout btnLoginRegisterLayout,changeCountryLayout,changeLanguageLayout;

    View view1,view2;

    ImageView instagramIcon,imgFlag,imgCountryFlag;

    Dialog dlgLoading = null;

    String contact_us_desc;

    private static final int REQUEST_CALL_PHONE_PERMISSION = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        context = getActivity();
        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_my_account_ar),true,true,true,false, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_my_account),true,true,true,false, false ,"0", false);
        }


        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_my_account_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
            }


            btnLoginRegister = (Button)rootView.findViewById(R.id.btnLoginRegister);
            btnMyAccountDetails = (Button)rootView.findViewById(R.id.btnMyAccountDetails);
//            btnWishlist = (Button)rootView.findViewById(R.id.btnWishlist);
            btnReturnsAndExchanges = (Button)rootView.findViewById(R.id.btnReturnsAndExchanges);
            btnShippingInformation = (Button)rootView.findViewById(R.id.btnShippingInformation);
            btnPaymentSecurity = (Button)rootView.findViewById(R.id.btnPaymentSecurity);
            btnPrivacyPolicy = (Button)rootView.findViewById(R.id.btnPrivacyPolicy);
            btnCookiePolicy = (Button)rootView.findViewById(R.id.btnCookiePolicy);
            btnTermsAndConditions = (Button)rootView.findViewById(R.id.btnTermsAndConditions);
            btnFaqs = (Button)rootView.findViewById(R.id.btnFaqs);
            btnContactEmailUs = (Button)rootView.findViewById(R.id.btnContactEmailUs);
            btnLogout = (Button)rootView.findViewById(R.id.btnLogout);
            btnChangeLanguage = (Button) rootView.findViewById(R.id.btnChangeLanguage);

            btnCallUs = (Button) rootView.findViewById(R.id.btnCallUs);
            instagramIcon = (ImageView) rootView.findViewById(R.id.instagramIcon);

            btnMyLocation = (Button) rootView.findViewById(R.id.btnMyLocation);

            btnAboutUs = (Button) rootView.findViewById(R.id.btnAboutUs);

            btnPaymentSecurity.setVisibility(View.GONE);
            btnCookiePolicy.setVisibility(View.GONE);


//            btnAppVersion = (Button) rootView.findViewById(R.id.btnAppVersion);

            lblHelpAndInformation = (TextView) rootView.findViewById(R.id.lblHelpAndInformation);
            lblContactUs = (TextView) rootView.findViewById(R.id.lblContactUs);
            lblHelpAdvice = (TextView) rootView.findViewById(R.id.lblHelpAdvice);
            lblAvailable = (TextView) rootView.findViewById(R.id.lblAvailable);

            lblAppVersion = (TextView)rootView.findViewById(R.id.lblAppVersion);
            lblDesignedBy = (TextView)rootView.findViewById(R.id.lblDesignedBy);

            view1 = (View) rootView.findViewById(R.id.view1);
            view2 = (View) rootView.findViewById(R.id.view2);

            imgFlag = (ImageView) rootView.findViewById(R.id.imgFlag);
            imgCountryFlag = (ImageView) rootView.findViewById(R.id.imgCountryFlag);


            changeCountryLayout = (RelativeLayout) rootView.findViewById(R.id.changeCountryLayout);
            changeLanguageLayout = (RelativeLayout) rootView.findViewById(R.id.changeLanguageLayout);
            lblCountryName = (TextView) rootView.findViewById(R.id.lblCountryName);
            lblLanguageName = (TextView) rootView.findViewById(R.id.lblLanguageName);

            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                lblLanguageName.setText("English");
            }else {
                lblLanguageName.setText("Arabic");
            }



            lblAppVersion.setText(getResources().getString(R.string.app_version)+" "+GlobalFunctions.appVersion2);

            btnLoginRegisterLayout = (RelativeLayout) rootView.findViewById(R.id.btnLoginRegisterLayout);

            if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {

                btnLoginRegisterLayout.setVisibility(View.GONE);
                btnLoginRegister.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                btnLogout.setVisibility(View.VISIBLE);
                btnMyAccountDetails.setVisibility(View.VISIBLE);

            }else {

                btnLoginRegisterLayout.setVisibility(View.GONE);
                btnLoginRegister.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                btnLogout.setVisibility(View.GONE);
                btnMyAccountDetails.setVisibility(View.GONE);

            }

            lblDesignedBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = "http://hayakw.com";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            instagramIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = "https://www.instagram.com/dagla_kw/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);

                }
            });

            btnLoginRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {

                    }else {
                        LoginFragment fragment1 = new LoginFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment1, "LoginFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();
                    }

                }
            });

            btnMyAccountDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {

                        MyAccountDetailsFragment fragment1 = new MyAccountDetailsFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, fragment1, "MyAccountDetailsFragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .addToBackStack(null)
                                // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                                .commitAllowingStateLoss();

                    }else {

                        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                            loginDialog2("يرجى تسجيل الدخول لعرض تفاصيل الحساب");
                        }else {
                            loginDialog2("Please login to view account details");
                        }


                    }



                }
            });

            btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {

                        GlobalFunctions.setPrefrences(getActivity(), "lang", "en");

                    } else {

                        GlobalFunctions.setPrefrences(getActivity(), "lang", "ar");

                    }
//
                    GlobalFunctions.setLanguage(getActivity());
//
//
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));

//                    ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                    MyAccountFragment fragment = new MyAccountFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, fragment, "MyAccountFragment")
//                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                            .commitAllowingStateLoss();
                }
            });

//            btnWishlist.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    WishListFragment fragment1 = new WishListFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, fragment1, "WishListFragment")
//                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                            .addToBackStack(null)
//                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                            .commitAllowingStateLoss();
//
//                }
//            });

            btnReturnsAndExchanges.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "a4gCDLs6glA=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.returns_exchanges_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.returns_exchanges));
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

            btnShippingInformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "uT4LCYxkiFY=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.shipping_information_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.shipping_information));
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

            btnPaymentSecurity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "2ucHke6VLD8=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.payment_security_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.payment_security));
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


            btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "9QWXE0GhLlg=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.privacy_policy_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.privacy_policy));
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

            btnCookiePolicy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "ne23R9jwRkc=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.cookie_policy_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.cookie_policy));
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

            btnTermsAndConditions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "7yfr6aCwW0c=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.terms_n_conditions_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.terms_n_conditions));
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

            btnFaqs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "CuOSpECKzwU=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.faqs_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.faqs));
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


            btnContactEmailUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    ContactUsFragment fragment1 = new ContactUsFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, fragment1, "ContactUsFragment")
//                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                            .addToBackStack(null)
//                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                            .commitAllowingStateLoss();

                    Intent intent=new Intent(Intent.ACTION_SEND);
                    String[] recipients={"info@dagla.com"};
                    intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
                    intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
//                    intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                    intent.setType("text/html");
                    intent.setPackage("com.google.android.gm");
                    startActivity(Intent.createChooser(intent, "Send mail"));

                }
            });

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!GlobalFunctions.getPrefrences(getActivity(), "user_id").equalsIgnoreCase("")) {
//                        logOutDialog();
                        logoutDialog();
                    }else {

                    }


                }
            });

            btnCallUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CALL_PHONE)
                                == PackageManager.PERMISSION_GRANTED) {
                            //Location Permission already granted
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+"98847163"));
                            startActivity(callIntent);
                        } else {
                            //Request Location Permission
                            if(checkAndRequestPhoneCallPermissions()){
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:"+"98847163"));
                                startActivity(callIntent);
                            }
                        }
                    }
                    else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+"98847163"));
                        startActivity(callIntent);
                    }
                }
            });

            btnMyLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    CountriesFragment fragment = new CountriesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, "CountriesFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });



            btnAboutUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Information2Fragment fragment = new Information2Fragment();
                    Bundle b = new Bundle();
                    b.putString("page_id", "uT4LCYxkiFY=");
                    if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                        b.putString("title", getResources().getString(R.string.about_us_ar));
                    }else {
                        b.putString("title", getResources().getString(R.string.about_us));
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



            changeCountryLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CountriesFragment fragment = new CountriesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, "CountriesFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });

            changeLanguageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {

                        GlobalFunctions.setPrefrences(getActivity(), "lang", "en");

                    } else {

                        GlobalFunctions.setPrefrences(getActivity(), "lang", "ar");

                    }
//
                    GlobalFunctions.setLanguage(getActivity());
                    GlobalFunctions.setPrefrences(getActivity(), "landing", "Shop");
//
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));

                }
            });





//            btnHelpAndInformation.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//
//            btnContactUs.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    ContactUsFragment fragment1 = new ContactUsFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.fragment_container, fragment1, "ContactUsFragment")
//                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                            .addToBackStack(null)
//                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                            .commitAllowingStateLoss();
//
//                }
//            });



            loadData();


        }

        return rootView;

    }


    public void logoutDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout_dialog_box);
        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            dialog.setContentView(R.layout.logout_dialog_box_ar);
        }else {
            dialog.setContentView(R.layout.logout_dialog_box);
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

        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            alertMessage.setText(getString(R.string.are_you_sure_you_want_to_logout_ar));
            okBtn.setText(getString(R.string.yes_ar));
            cancelBtn.setText(getString(R.string.no_ar));
        }else {
            alertMessage.setText(getString(R.string.are_you_sure_you_want_to_logout));
            okBtn.setText(getString(R.string.yes));
            cancelBtn.setText(getString(R.string.no));
        }


        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                GlobalFunctions.setPrefrences(context, "id", "");
                GlobalFunctions.setPrefrences(context, "user_id", "");
                GlobalFunctions.setPrefrences(context, "name", "");
                GlobalFunctions.setPrefrences(context, "email", "");

                getActivity().finish();
                startActivity(new Intent(getActivity(), SplashActivity.class));

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

    public void logOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(getString(R.string.are_you_sure_you_want_to_logout));

        builder.setPositiveButton(getString(R.string.yes).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


//                dialog.dismiss();

                GlobalFunctions.setPrefrences(context, "id", "");
                GlobalFunctions.setPrefrences(context, "user_id", "");
                GlobalFunctions.setPrefrences(context, "name", "");
                GlobalFunctions.setPrefrences(context, "email", "");

//                checkLoggedIn();

//                Intent intent = getActivity().getIntent();
//                getActivity().overridePendingTransition(0, 0);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtra("flag", 1);
//                getActivity().finish();
//                getActivity().overridePendingTransition(0, 0);
//                startActivity(intent);

                getActivity().finish();
//                            GlobalFunctions.showToastSuccess(act, getString(R.string.your_login_was_successful));
//                startActivity(new Intent(getActivity(), IntroductionActivity.class));
                startActivity(new Intent(getActivity(), IntroductionActivity2.class));

            }
        });

        builder.setNegativeButton(getString(R.string.no).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void loginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(getString(R.string.are_you_sure_you_want_to_logout));

        builder.setPositiveButton(getString(R.string.yes).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                GlobalFunctions.setPrefrences(context, "id", "");
                GlobalFunctions.setPrefrences(context, "user_id", "");
                GlobalFunctions.setPrefrences(context, "name", "");
                GlobalFunctions.setPrefrences(context, "email", "");

//                checkLoggedIn();

            }
        });

        builder.setNegativeButton(getString(R.string.no).toUpperCase(), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }


    public void loginDialog2(String msg) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            dialog.setContentView(R.layout.logout_dialog_box_ar);
        }else {
            dialog.setContentView(R.layout.logout_dialog_box);
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
        alertMessage.setText(msg);
        Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
        Button cancelBtn= (Button) dialog.findViewById(R.id.cancelBtn);

        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            okBtn.setText("نعم");
            cancelBtn.setText("لا");
        }else {
            okBtn.setText("Yes");
            cancelBtn.setText("No");
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                LoginFragment fragment1 = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment1, "LoginFragment")
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


    private void loadData() {

        if (GlobalFunctions.hasConnection(getActivity())) {

            showLoading();

            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();

            params.put("ran", GlobalFunctions.getRandom());
            params.put("lang", GlobalFunctions.getLang(getActivity()));
            client.get(GlobalFunctions.serviceURL + "getContactUsContentData" , params, new AsyncHttpResponseHandler() {

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


                            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                                contact_us_desc = obj.getString("description_ar");
                            }else {
                                contact_us_desc = obj.getString("description");
                            }

                            lblHelpAdvice.setText(contact_us_desc);

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


    // Reading Phone storage Permissions
    private boolean checkAndRequestPhoneCallPermissions() {

        int gallerypermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (gallerypermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }


        if (!listPermissionsNeeded.isEmpty()) {

            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CALL_PHONE_PERMISSION);

            return false;
        }
        return true;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {

            case REQUEST_CALL_PHONE_PERMISSION:{

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        // process the normal flow
                        //else any one or both the permissions are not granted
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+"98847163"));
                        startActivity(callIntent);
                    } else {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CALL_PHONE)) {
                            showDialogOK("Phone Call Permissions required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPhoneCallPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {


                            new android.app.AlertDialog.Builder(getActivity())
                                    .setTitle("Phone Call Permissions required for this app")
                                    .setMessage("Go to settings and enable permissions from the App, Settings>Apps>Dagla>Permissions>Enable Phone Call service")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //Prompt the user once explanation has been shown

                                        }
                                    })
                                    .create()
                                    .show();

                        }
                    }


                }

            }

            break;

        }

    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
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


    public void onResume(){
        super.onResume();


        imgFlag.setBackgroundResource(R.drawable.flag_kuwait);
//        imgCountryFlag.setBackgroundResource(R.drawable.flag_kuwait);
        Picasso.with(getActivity()).load(R.drawable.flag_kuwait).into(imgCountryFlag);
        if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
            btnMyLocation.setText("الكويت");
            lblCountryName.setText("الكويت");
        }else {
            btnMyLocation.setText("Kuwait");
            lblCountryName.setText("Kuwait");
        }



        if(!GlobalFunctions.getPrefrences(getActivity(), "CountryName").equals("")&&!GlobalFunctions.getPrefrences(getActivity(), "CountryName").equals("")){
            if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
                lblCountryName.setText(GlobalFunctions.getPrefrences(getActivity(), "CountryNameAr"));
                btnMyLocation.setText(GlobalFunctions.getPrefrences(context, "CountryNameAr"));
            }else {
                lblCountryName.setText(GlobalFunctions.getPrefrences(getActivity(), "CountryName"));
                btnMyLocation.setText(GlobalFunctions.getPrefrences(context, "CountryName"));
            }

        }



        if(!GlobalFunctions.getPrefrences(context, "CountryFlag").equals("")){
//            Picasso.with(context).load(GlobalFunctions.getPrefrences(context, "CountryFlag")).into(imgFlag);
//            Picasso.with(getActivity()).load(GlobalFunctions.getPrefrences(context, "CountryFlag")).into(imgCountryFlag);

            Glide.with(context)
                    .load(GlobalFunctions.getPrefrences(context, "CountryFlag"))
//                    .placeholder(R.drawable.place_holder)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

//                            progress2.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            progress2.setVisibility(View.GONE);
                            return false;
                        }


                    })
                    .into(imgFlag);

            Glide.with(context)
                    .load(GlobalFunctions.getPrefrences(context, "CountryFlag"))
//                    .placeholder(R.drawable.place_holder)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

//                            progress2.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            progress2.setVisibility(View.GONE);
                            return false;
                        }


                    })
                    .into(imgCountryFlag);

        }

//        if(GlobalFunctions.getPrefrences(context, "CountryName").equals("Kuwait")){

//            imgFlag.setBackgroundResource(R.drawable.flag_kuwait);
//            imgCountryFlag.setBackgroundResource(R.drawable.flag_kuwait);

//        }else if(GlobalFunctions.getPrefrences(context, "CountryName").equals("Saudi Arabia")){
//            imgFlag.setBackgroundResource(R.drawable.flag_saudi_arabia);
////            imgCountryFlag.setBackgroundResource(R.drawable.flag_saudi_arabia);
//            Picasso.with(getActivity()).load(R.drawable.flag_saudi_arabia).into(imgCountryFlag);
//        }else if(GlobalFunctions.getPrefrences(context, "CountryName").equals("Qatar")){
//            imgFlag.setBackgroundResource(R.drawable.flag_qatar);
////            imgCountryFlag.setBackgroundResource(R.drawable.flag_qatar);
//            Picasso.with(getActivity()).load(R.drawable.flag_qatar).into(imgCountryFlag);
//        }else if(GlobalFunctions.getPrefrences(context, "CountryName").equals("Bahrain")){
//            imgFlag.setBackgroundResource(R.drawable.flag_bahrain);
////            imgCountryFlag.setBackgroundResource(R.drawable.flag_bahrain);
//            Picasso.with(getActivity()).load(R.drawable.flag_bahrain).into(imgCountryFlag);
//        }else if(GlobalFunctions.getPrefrences(context, "CountryName").equals("Oman")){
//            imgFlag.setBackgroundResource(R.drawable.flag_oman);
////            imgCountryFlag.setBackgroundResource(R.drawable.flag_oman);
//            Picasso.with(getActivity()).load(R.drawable.flag_oman).into(imgCountryFlag);
//        }else if(GlobalFunctions.getPrefrences(context, "CountryName").equals("United Arab Emirates")){
//            imgFlag.setBackgroundResource(R.drawable.flag_united_arab_emirates);
////            imgCountryFlag.setBackgroundResource(R.drawable.flag_united_arab_emirates);
//            Picasso.with(getActivity()).load(R.drawable.flag_united_arab_emirates).into(imgCountryFlag);
//        }

    }


}
