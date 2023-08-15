package com.dagla.android.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.dagla.android.fragments.MyAddressesFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressesAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> names;
    MyAddressesFragment fragment;


    static class ViewHolder {
        public TextView lblArea, lblAddressName, lblAddress;
        public ImageView imgDelete;
    }

    public AddressesAdapter(Activity context, ArrayList<String> names, MyAddressesFragment fragment) {
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
            ViewHolder viewHolder = new ViewHolder();
            //
            viewHolder.lblArea = (TextView)rowView.findViewById(R.id.lblArea);
            viewHolder.lblAddressName = (TextView)rowView.findViewById(R.id.lblAddressName);
            viewHolder.lblAddress = (TextView)rowView.findViewById(R.id.lblAddress);
            viewHolder.imgDelete = (ImageView)rowView.findViewById(R.id.imgDelete);
            //
            if (fragment instanceof MyAddressesFragment) {
                //
                viewHolder.imgDelete.setVisibility(View.VISIBLE);
            }
            //
            rowView.setTag(viewHolder);
        }

        final ViewHolder holder = (ViewHolder)rowView.getTag();

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
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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


                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
                        dialog.setContentView(R.layout.logout_dialog_box_ar);
                    }else {
                        dialog.setContentView(R.layout.logout_dialog_box);
                    }
                    WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                    layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = Gravity.BOTTOM;

                    dialog.getWindow().setAttributes(layoutParams);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    final TextView alertMessage = (TextView) dialog.findViewById(R.id.alertMessage);
//                    alertMessage.setText(msg);
                    Button okBtn = (Button) dialog.findViewById(R.id.okBtn);
                    Button cancelBtn= (Button) dialog.findViewById(R.id.cancelBtn);

                    if (GlobalFunctions.getLang(context).equalsIgnoreCase("ar")) {
                        alertMessage.setText(R.string.are_you_sure_ar);
                        okBtn.setText("نعم");
                        cancelBtn.setText("لا");
                    }else {
                        alertMessage.setText(R.string.are_you_sure);
                        okBtn.setText("Yes");
                        cancelBtn.setText("No");
                    }

                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            dialog.dismiss();
                            fragment.delAddress(position);


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
            });

        } catch (JSONException e) {

            Log.d("AddressesAdapter", "JSONException:" + e.getMessage());

        }

        return rowView;
    }

}