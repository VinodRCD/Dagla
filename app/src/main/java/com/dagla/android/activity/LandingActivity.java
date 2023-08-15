package com.dagla.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dagla.android.GlobalFunctions;
import com.dagla.android.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import cz.msebera.android.httpclient.Header;

public class LandingActivity extends AppCompatActivity {

    Activity act;
    EditText txtEmail;
    Button btnShop,btnTailoring;

    Dialog dlgLoading = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        super.onCreate(savedInstanceState);
        //
        GlobalFunctions.setLanguage(this);

        if(GlobalFunctions.getLang(LandingActivity.this).equals("ar")){
            setContentView(R.layout.activity_landing_ar);
        }else {
            setContentView(R.layout.activity_landing);
        }


        act = this;
        btnShop = (Button)findViewById(R.id.btnShop);
        btnTailoring = (Button)findViewById(R.id.btnTailoring);


        //
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                Intent intent = new Intent(act, MainActivity.class);
                GlobalFunctions.setPrefrences(act, "landing", "Shop");
                intent.putExtra("Title", getString(R.string.shop));

                startActivity(intent);
            }
        });

        btnTailoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                Intent intent = new Intent(act, MainActivity.class);
                GlobalFunctions.setPrefrences(act, "landing", "Tailoring");
                intent.putExtra("Title", getString(R.string.tailoring));

                startActivity(intent);

            }
        });
    }








}