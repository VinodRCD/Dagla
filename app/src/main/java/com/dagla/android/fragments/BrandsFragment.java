package com.dagla.android.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.dagla.android.R;
import com.dagla.android.activity.MainActivity;
import com.dagla.android.adapter.BrandsAdapter;

import java.util.ArrayList;

public class BrandsFragment extends Fragment {


    View rootView;
    Context context;
    Bundle savedInstanceState;

    ListView lv;

    BaseAdapter arrAdapter;

    Typeface custom_fontbold, custom_fontnormal;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //just change the fragment_dashboard
        //with the fragment you want to inflate
        //like if the class is HomeFragment it should have R.layout.home_fragment
        //if it is DashboardFragment it should have R.layout.fragment_dashboard

        context = getActivity();

        custom_fontbold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avenir-next-bold.ttf");
        custom_fontnormal = Typeface.createFromAsset(getActivity().getAssets(), "fonts/avenir-next-regular.ttf");

        ((MainActivity) getActivity()).setHeaders(getResources().getString(R.string.title_brands),true,true,true,false, false ,"0", false);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_brands, container, false);

            lv = rootView.findViewById(R.id.lv);

            ArrayList arrNames = new ArrayList<String>();

            arrNames.add("Acne Studios");
            arrNames.add("Alexander McQueen");
            arrNames.add("Aquazzura");
            arrNames.add("Balenciaga");
            arrNames.add("Balmain");
            arrNames.add("Bottega Veneta");
            arrNames.add("Burberry");
            arrNames.add("Chloe");
            arrNames.add("Etro");
            arrNames.add("Fendi");
            arrNames.add("Gianvito Rossi");
            arrNames.add("Givenchy");
            arrNames.add("Gucci");
            arrNames.add("Isabel Marant");
            arrNames.add("J Crew");
            arrNames.add("Jimmy Choo");
            arrNames.add("Joseph");
            arrNames.add("Lanvin");
            arrNames.add("Marni");
            arrNames.add("Miu Miu");
            arrNames.add("Oscar de le Renta");
            arrNames.add("Prada");
            arrNames.add("Saint Laurent");
            arrNames.add("Self-Portrait");
            arrNames.add("Stella McCartney");
            arrNames.add("The Row");
            arrNames.add("Tom Ford");

            arrAdapter = new BrandsAdapter(getActivity(), arrNames);

            lv.setAdapter(arrAdapter);

        }

        return rootView;

    }

}
