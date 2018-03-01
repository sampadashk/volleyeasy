package com.samiapps.kv.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KV on 21/2/18.
 */
public class MyInfoActivity extends BaseActivity{
    public RelativeLayout MyAccount;
    public RelativeLayout comPanyInfo;
    public RelativeLayout huowupeisong;
    public RelativeLayout fapiaopeisong;
    public TextView MyAccountComfirm;
    public TextView comPanyComfirm;
    public TextView huowupeisongComfirm;
    public TextView fapiaopeisongComfirm;
    public TextView name;
    public LinearLayout back;
    GlobalProvider globalProvider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        globalProvider=GlobalProvider.getInstance(getApplicationContext());
        MyAccount = (RelativeLayout) findViewById(R.id.myAccount);
        comPanyInfo = (RelativeLayout) findViewById(R.id.comPanyInfo);
        huowupeisong = (RelativeLayout) findViewById(R.id.peisongAddress);
        fapiaopeisong = (RelativeLayout) findViewById(R.id.fapiaoAddress);
        name = (TextView) findViewById(R.id.name);
        if (globalProvider.me != null)
        {
            if(globalProvider.me.name!=null){name.setText(globalProvider.me.name);}
        }
        back= (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

      MyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyInfoActivity.this,MyAccountActivity.class);
                startActivity(intent);
            }
        });
        comPanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyInfoActivity.this,CompanyInfoActivity.class);
                startActivityForResult(intent,Constants.UPDATEADDRESSINTENT);
            }
        });
        fapiaopeisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyInfoActivity.this,EditBillingAddressTActivity.class);
                startActivityForResult(intent, Constants.UPDATEADDRESSINTENT);
            }
        });

       /*

        huowupeisong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MyInfoActivity.this,EditShippingAddressTActivity.class);
                startActivityForResult(intent, Constants.UPDATEADDRESSINTENT);
            }
        });
        */


        MyAccountComfirm= (TextView) findViewById(R.id.one_text);
        comPanyComfirm= (TextView) findViewById(R.id.two_text);
        huowupeisongComfirm= (TextView) findViewById(R.id.three_text);
        fapiaopeisongComfirm= (TextView) findViewById(R.id.four_text);


        MyAccountComfirm.setText(getString(R.string.yiwanshan));
        comPanyComfirm.setText(getString(R.string.yiwanshan));
        huowupeisongComfirm.setText(getString(R.string.yiwanshan));
        fapiaopeisongComfirm.setText(getString(R.string.yiwanshan));

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.UPDATEADDRESSINTENT:
                if (resultCode == RESULT_OK) {
                    loadMe();
                }
                break;
        }
    }
    public void loadMe(){
        Utf8JsonRequest stringRequest=new Utf8JsonRequest(Request.Method.GET, Constants.meUrlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseMe(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                String token = Constants.getToken(MyInfoActivity.this);
                Log.d("gettoken", token);
                headers.put("Authorization",token);
                return headers;
            }
        };
     globalProvider.addRequest(stringRequest);
    }
    public void parseMe(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            Me me = (Me) objectMapper.readValue(jsonParser, Me.class);
            globalProvider.me=me;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
