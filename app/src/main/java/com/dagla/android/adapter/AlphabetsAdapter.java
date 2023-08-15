package com.dagla.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dagla.android.R;


public class AlphabetsAdapter extends BaseAdapter {

    private String alpha[];
    private Context mContext;

    public AlphabetsAdapter(Context mContext, String alpha[]) {
        this.mContext = mContext;
        this.alpha = alpha;
    }

    @Override
    public int getCount() {
        return alpha.length;
    }

    @Override
    public Object getItem(int i) {
        return alpha[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
//        final SortModel mContent = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.alphabets_item, null);
            viewHolder.alphabetsTxt = (TextView) view.findViewById(R.id.alphabetsTxt);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }


//        viewHolder.alphabetsTxt.setTypeface(custom_fontnormal);

        viewHolder.alphabetsTxt.setText(this.alpha[i]);

        return view;
    }

    final static class ViewHolder {
        TextView alphabetsTxt;
    }
}
