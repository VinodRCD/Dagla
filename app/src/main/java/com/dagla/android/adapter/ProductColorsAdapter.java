package com.dagla.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;


import com.dagla.android.R;

import java.util.ArrayList;

public class ProductColorsAdapter extends RecyclerView.Adapter<ProductColorsAdapter.ViewHolder> {

    Context mContext;
    ArrayList<String> mList;

    String user_Id,language;

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

        public ImageView selectImg;
        public RelativeLayout colorLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            selectImg = (ImageView) itemView.findViewById(R.id.selectImg);
            colorLayout = (RelativeLayout) itemView.findViewById(R.id.colorLayout);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            clickListener.OnItemClick(getAdapterPosition(), v);
        }

    }




    // Provide a suitable constructor (depends on the kind of dataset)
    public ProductColorsAdapter(Context context, ArrayList<String> mList) {
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
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item_layout, parent, false);

        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder1, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element holder1.imgLike.setTag(position);

        holder1.colorLayout.setBackgroundColor(Color.parseColor(mList.get(position)));
//
        if(row_index==position){
            holder1.selectImg.setVisibility(View.VISIBLE);
        }else {
            holder1.selectImg.setVisibility(View.GONE);
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
