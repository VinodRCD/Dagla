package com.dagla.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dagla.android.R;
import com.dagla.android.parser.FilterSizesDetails;

import java.util.ArrayList;

public class FilterSizesAdapter extends RecyclerView.Adapter<FilterSizesAdapter.ViewHolder> {

    Context mContext;
    ArrayList<FilterSizesDetails> mList;

    String user_Id,language;

    ClickListener clickListener;

    int row_index = -1;

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
        public TextView lblSize;

        public ViewHolder(View itemView) {
            super(itemView);

            selectImg = (ImageView) itemView.findViewById(R.id.selectImg);
            lblSize = (TextView) itemView.findViewById(R.id.lblSize);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            clickListener.OnItemClick(getAdapterPosition(), v);
        }

    }




    // Provide a suitable constructor (depends on the kind of dataset)
    public FilterSizesAdapter(Context context, ArrayList<FilterSizesDetails> mList) {
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
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_sizes_item, parent, false);

        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder1, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element holder1.imgLike.setTag(position);

        holder1.lblSize.setText(mList.get(position).getSizeName());
//


        if(mList.get(position).isSelected()){
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
//        mList.get(pos).setSelected(true);
        notifyDataSetChanged();
    }
}
