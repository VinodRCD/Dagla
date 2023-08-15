package com.dagla.android.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.CheckoutDeliveryFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressesFromCheckOutAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    CheckoutDeliveryFragment fragment;


    static class ViewHolder {
        public TextView lblArea, lblAddressName, lblAddress;
        public ImageView imgDelete;
    }

    public AddressesFromCheckOutAdapter(Activity context, ArrayList<String> names, CheckoutDeliveryFragment fragment) {
        super(context, R.layout.layout_address, names);
        this.context = context;
        this.names = names;
        this.fragment = fragment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //
        View rowView = convertView;
        //
        if (rowView == null) {
            //
            LayoutInflater inflater = context.getLayoutInflater();
            if(GlobalFunctions.getLang(context).equals("ar")){
                rowView = inflater.inflate(R.layout.layout_address_ar, null);
            }else {
                rowView = inflater.inflate(R.layout.layout_address, null);
            }

            //
            AddressesAdapter.ViewHolder viewHolder = new AddressesAdapter.ViewHolder();
            //
            viewHolder.lblArea = (TextView)rowView.findViewById(R.id.lblArea);
            viewHolder.lblAddressName = (TextView)rowView.findViewById(R.id.lblAddressName);
            viewHolder.lblAddress = (TextView)rowView.findViewById(R.id.lblAddress);
            viewHolder.imgDelete = (ImageView)rowView.findViewById(R.id.imgDelete);
            //
//            if (fragment instanceof CheckoutDeliveryFragment) {
                //
                viewHolder.imgDelete.setVisibility(View.GONE);
//            }
            //
            rowView.setTag(viewHolder);
        }

        final AddressesAdapter.ViewHolder holder = (AddressesAdapter.ViewHolder)rowView.getTag();

        String s = names.get(position);

        try {

            final JSONObject obj = new JSONObject(s);


//            holder.lblArea.setTypeface(custom_fontmedium);
//            holder.lblAddressName.setTypeface(custom_fontmedium);
//            holder.lblAddress.setTypeface(custom_fontmedium);


            holder.lblArea.setText(obj.getString("area").toUpperCase());
            holder.lblAddressName.setText(obj.getString("address_name"));
            holder.lblAddress.setText(obj.getString("address"));

            //
//            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//
//                    builder.setMessage(R.string.are_you_sure);
//
//                    builder.setPositiveButton(context.getString(R.string.yes).toUpperCase(), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            fragment.delAddress(position);
//
//                        }
//                    });
//
//                    builder.setNegativeButton(context.getString(R.string.no).toUpperCase(), new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                            //
//
//                        }
//                    });
//
//                    AlertDialog dialog = builder.create();
//
//                    dialog.show();
//
//
//                }
//            });

        } catch (JSONException e) {

            Log.d("AddressesAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }
}
