package com.dagla.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;

public class ContactUsFragment extends Fragment {

    View rootView;
    Context context;
    Bundle savedInstanceState;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.contact_us),false,false,false,true, false ,"0", false);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_contact_us, container, false);

        }

        return rootView;

    }
}
