package com.dagla.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.parser.HomeBannerDetails;
import com.dagla.android.parser.HomeCateDetails;

import java.util.ArrayList;

public class HomeCateAdapter extends RecyclerView.Adapter<HomeCateAdapter.ViewHolder> {

    Context mContext;
    ArrayList<HomeCateDetails> mList;

    ClickListener clickListener;

    int row_index = 0;

    public void setOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;

    }

    public interface ClickListener {
        void OnItemClick(int position, View v);
    }


    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView lblCateName;
        public RelativeLayout cateLayout;
        public CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);

            lblCateName = (TextView) itemView.findViewById(R.id.lblCateName);
            cateLayout = (RelativeLayout) itemView.findViewById(R.id.cateLayout);
            cardview = (CardView) itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            clickListener.OnItemClick(getAdapterPosition(), v);
        }

    }




    // Provide a suitable constructor (depends on the kind of dataset)
    public HomeCateAdapter(Context context, ArrayList<HomeCateDetails> mList) {
        super();
        this.mContext = context;
        this.mList = mList;


    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v;

//        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_1_item_layout, parent, false);


        if(GlobalFunctions.getLang(mContext).equals("ar")){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cate_item_layout_ar, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cate_item_layout, parent, false);
        }

        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder1, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element holder1.imgLike.setTag(position);

        holder1.cardview.getLayoutParams().width = (int) (GlobalFunctions.get_device_width(mContext) / 3) - 50;
//        holder1.cateLayout.getLayoutParams().height = (int) (GlobalFunctions.get_device_width(mContext) / 2) - 50;

        holder1.lblCateName.setText(mList.get(position).getCateName());

        if(row_index==position){
            holder1.cardview.setBackgroundResource(R.drawable.bg_1);
            holder1.lblCateName.setTextColor(Color.parseColor("#FFFFFF"));
        }else {
            holder1.cardview.setBackgroundResource(R.drawable.bg_2);
            holder1.lblCateName.setTextColor(Color.parseColor("#222222"));
        }




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void Selected(int pos){
        this.row_index = pos;
        notifyDataSetChanged();
    }
}
