package com.samiapps.kv.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by KV on 21/2/18.
 */


    public class SupplyInfoActivity extends Activity implements View.OnClickListener {
        private Intent intent;
        private Contract contract;
        private ImageView turnLeft,head,phone;
        GlobalProvider globalProvider;
        private TextView look,name,produce,supplyTel,supplyAddress,supplyIntroduce,resFriend;
        private String str = " ";

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_supply_info);
            globalProvider=GlobalProvider.getInstance(SupplyInfoActivity.this);
            init();
            initAction();
            intent = this.getIntent();
            if (intent.getSerializableExtra("contract") != null) {
                contract = (Contract) intent.getSerializableExtra("contract");
                loadCategory(contract);

                String url = Constants.imgBaseUrlStr + "/" + contract._supplierInfo.getLogo();
                Picasso.with(this).load(url).resize(150, 150).centerCrop().into(head);
                name.setText(contract._supplierInfo.getCompanyName());
                produce.setText(contract._supplierInfo.getCompanyDescription());
                supplyTel.setText(getString(R.string.supplierphone) + contract._supplierInfo.getContactNumber());
                supplyAddress.setText(getString(R.string.supplierAddress) + contract._supplierInfo.getBillingAddress());
                if (Constants.getGetLanguage(this).equals("english") || (Locale.getDefault().getLanguage().equals("en"))) {
                    supplyIntroduce.setText(contract._supplierInfo.getCompanyDescription_english());
                } else
                    supplyIntroduce.setText(contract._supplierInfo.getCompanyDescription());

            }
        }

        private void init() {
            turnLeft = (ImageView) findViewById(R.id.turn_left);
            head = (ImageView) findViewById(R.id.head);
            name = (TextView) findViewById(R.id.name);
            produce = (TextView) findViewById(R.id.produce);
            supplyTel = (TextView) findViewById(R.id.supplyTel);
            supplyAddress = (TextView) findViewById(R.id.supplyAddress);
            supplyIntroduce = (TextView) findViewById(R.id.supplyIntroduce);
            resFriend = (TextView) findViewById(R.id.resFriend);
            phone = (ImageView) findViewById(R.id.phone);
            look = (TextView) findViewById(R.id.look);
        }

        private void initAction() {
            turnLeft.setOnClickListener(this);
            phone.setOnClickListener(this);
            look.setOnClickListener(this);
        }

        public void onClick(View view) {
            switch (view.getId()){
                case R.id.turn_left:
                    finish();
                    break;
                case R.id.phone:
                    Intent intent =new Intent(Intent.ACTION_DIAL);

                    intent.setData(Uri.parse("tel:" + contract._supplierInfo.getContactNumber()));
                    startActivity(intent);
                    break;
                case R.id.look:
                    globalProvider.contract=contract;
                    Intent intent1=new Intent(SupplyInfoActivity.this, MainActivity.class);
                    intent1.putExtra("singal","supInfoToProduct");
                    startActivity(intent1);
                    this.finish();
                    break;
            }
        }

        public void loadCategory(Contract contract){

            String url = Constants.categoryUrlStr+"/"+contract._supplierInfo.getCategory();

            Utf8JsonRequest stringRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    parseCategory(response);
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
                    String token = Constants.getToken(SupplyInfoActivity.this);
                    Log.d("gettoken", token);
                    headers.put("Authorization",token);
                    return headers;
                }
            };
            globalProvider.addRequest(stringRequest);
            //Log.d("checkurl",url);

        }

        private void parseCategory(String json) {
            JsonFactory jsonFactory = new JsonFactory();
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                JsonParser jsonParser = jsonFactory.createParser(json);
                Category category = (Category) objectMapper.readValue(jsonParser, Category.class);
                produce.setText(getString(R.string.mainbusiness)+category.getSupplierCategory());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }


