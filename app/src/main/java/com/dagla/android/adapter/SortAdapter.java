package com.dagla.android.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.ProductsFragment;
import com.dagla.android.parser.SortModel;

import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private Context mContext;

    FragmentActivity activity;
    FragmentTransaction ft;

    public SortAdapter(Context mContext, List<SortModel> list) {
        this.mContext = mContext;
        this.list = list;

    }

    /**
     * When the ListView data changes, call this method to update ListView
     * @param list
     */
    public void updateListView(List<SortModel> list){
        this.list = list;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            if(GlobalFunctions.getLang(mContext).equals("ar")){
                view = LayoutInflater.from(mContext).inflate(R.layout.item_ar, null);
            }else {
                view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            }

            viewHolder.sort_key1 = (TextView) view.findViewById(R.id.sort_key1);
            viewHolder.brandName1 = (TextView) view.findViewById(R.id.brandName1);
            viewHolder.sort_key_layout1 = (LinearLayout) view.findViewById(R.id.sort_key_layout1);
            viewHolder.brand_name_layout1 = (LinearLayout) view.findViewById(R.id.brand_name_layout1);
            viewHolder.view1 = (View) view.findViewById(R.id.view1);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //According to the position classification of the first letter of the char ASCII value
        int section = getSectionForPosition(position);

        //If the current position is equal to the classification of the first letter of the Char position, is believed to be the first to appear
//        if(position == getPositionForSection(section)){
//            viewHolder.brandName.setVisibility(View.VISIBLE);
//            viewHolder.brandName.setText(mContent.getSortLetters());
//        }else{
//            viewHolder.brandName.setVisibility(View.GONE);
//        }
//
//        viewHolder.tvTitle.setText(this.list.get(position).getName());

//        viewHolder.sort_key.setTypeface(custom_fontbold);
//        viewHolder.brandName.setTypeface(custom_fontnormal);

        if(position == getPositionForSection(section)){
            viewHolder.sort_key_layout1.setVisibility(View.VISIBLE);
            viewHolder.sort_key1.setVisibility(View.VISIBLE);
            viewHolder.sort_key1.setText(mContent.getSortLetters());
            viewHolder.view1.setVisibility(View.GONE);
        }else{
            viewHolder.sort_key_layout1.setVisibility(View.GONE);
            viewHolder.sort_key1.setVisibility(View.GONE);
            viewHolder.view1.setVisibility(View.VISIBLE);
        }

        viewHolder.brandName1.setText(this.list.get(position).getName());

        viewHolder.brand_name_layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(mContext, list.get(position).getName(), Toast.LENGTH_SHORT).show();
//
//
//                ProductsFragment fragment = new ProductsFragment();
//                Bundle b = new Bundle();
//                b.putString("brand_id", list.get(position).getId());
//                b.putString("title", list.get(position).getName());
//                fragment.setArguments(b);
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, fragment, "ProductsFragment")
//                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                        .addToBackStack(null)
//                        // .setCustomAnimations(R.anim.right_to_left, R.anim.fadeout_2,0, R.anim.left_to_right)
//                        .commitAllowingStateLoss();



                ProductsFragment fragment = new ProductsFragment();
                Bundle b = new Bundle();
                b.putString("brand_id", list.get(position).getId());
                b.putString("title", list.get(position).getName());
                fragment.setArguments(b);


                activity = ((FragmentActivity) mContext);
                ft = activity.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, fragment, "ProductsFragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
            }
        });

        return view;

    }



    final static class ViewHolder {
        TextView brandName1;
        TextView sort_key1;
        LinearLayout sort_key_layout1,brand_name_layout1;
        View view1;
    }


    /**
     * According to the current position of the ListView classification of the first letter of the char ASCII value
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * According to the classification of the first letter of the Char ASCII value to obtain the first appeared in the initial position
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i <getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public Object[] getSections() {
        return null;
    }
}
