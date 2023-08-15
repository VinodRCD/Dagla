package com.dagla.android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;

public class MyAccountDetailsFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;

    Button btnOrders,btnMyProfile,btnWishList,btnMyAddresses,btnPrivacySettings,btnChangePassword,btnChangeLanguage,btnReturns,btnWallet;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        context = getActivity();

        if(GlobalFunctions.getLang(getActivity()).equals("ar")){
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_my_account_ar),true,false,false,true, false ,"0", false);
        }else {
            ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_my_account),true,false,false,true, false ,"0", false);
        }

        if (rootView == null) {
            if(GlobalFunctions.getLang(getActivity()).equals("ar")){
                rootView = inflater.inflate(R.layout.fragment_my_account_details_ar, container, false);
            }else {
                rootView = inflater.inflate(R.layout.fragment_my_account_details, container, false);
            }


            btnOrders = (Button)rootView.findViewById(R.id.btnOrders);
            btnReturns = (Button)rootView.findViewById(R.id.btnReturns);
            btnMyProfile = (Button)rootView.findViewById(R.id.btnMyProfile);
            btnWishList = (Button)rootView.findViewById(R.id.btnWishList);
            btnMyAddresses = (Button)rootView.findViewById(R.id.btnMyAddresses);
//            btnPrivacySettings = (Button)rootView.findViewById(R.id.btnPrivacySettings);
            btnChangePassword = (Button)rootView.findViewById(R.id.btnChangePassword);
            btnWallet = (Button)rootView.findViewById(R.id.btnWallet);


            btnOrders.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyOrdersFragment fragment1 = new MyOrdersFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "MyOrdersFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });


            btnReturns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyReturnsFragment fragment1 = new MyReturnsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "MyReturnsFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();
                }
            });

            btnMyProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyProfileFragment fragment1 = new MyProfileFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "MyProfileFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();
                }
            });

            btnWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WishListFragment fragment1 = new WishListFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "WishListFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });

            btnMyAddresses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MyAddressesFragment fragment1 = new MyAddressesFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "MyAddressesFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();

                }
            });

//            btnPrivacySettings.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });

            btnChangePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ChangePasswordFragment fragment1 = new ChangePasswordFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "ChangePasswordFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();
                }
            });

            btnWallet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WalletFragment fragment1 = new WalletFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment1, "WalletFragment")
                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
                            .commitAllowingStateLoss();
                }
            });

//            btnChangeLanguage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (GlobalFunctions.getLang(getActivity()).equals("ar")) {
//
//                        GlobalFunctions.setPrefrences(getActivity(), "lang", "en");
//
//                    } else {
//
//                        GlobalFunctions.setPrefrences(getActivity(), "lang", "ar");
//
//                    }
////
//                    GlobalFunctions.setLanguage(getActivity());
////
////
//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//
////                    ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
////
////                    MyAccountFragment fragment = new MyAccountFragment();
////                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                    fragmentManager.beginTransaction()
////                            .replace(R.id.fragment_container, fragment, "MyAccountFragment")
////                            .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
////                            // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
////                            .commitAllowingStateLoss();
//                }
//            });

        }

        return rootView;

    }

    public void logOutDialog(){
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
}
