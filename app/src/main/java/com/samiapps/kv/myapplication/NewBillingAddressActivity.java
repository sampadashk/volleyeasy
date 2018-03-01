package com.samiapps.kv.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by KV on 26/2/18.
 */

public class NewBillingAddressActivity extends BaseActivity implements View.OnClickListener{
    public TextView save;
    public EditText billingAddress;
    public ImageView turn_left;
    public List<String> billing;
    GlobalProvider globalProvider;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_activity);
        globalProvider=GlobalProvider.getInstance(NewBillingAddressActivity.this);
        init();
        initAction();
    }

    private void init() {
        turn_left = (ImageView) findViewById(R.id.turn_left);
        save = (TextView) findViewById(R.id.save);
        billingAddress = (EditText) findViewById(R.id.shippingAddress);
        billing = new ArrayList<String>();
    }

    private void initAction() {
        turn_left.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.turn_left:
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.save:
                if((!billingAddress.getText().toString().equals(""))){
                    billing = globalProvider.me._customerProfile.getBillingAddresses();
                    billing.add(billingAddress.getText().toString());
                    doReset(billing);
                }else{
                    Toast.makeText(getApplication(), "请输入您的发票地址", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void doReset(final List<String> list){
        BillingAddress body=new BillingAddress();
        body.setBillingAddresses(list);
        for(int i=0;i<list.size();i++)
        Log.d("checkaslist",list.get(i));

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(body);
            ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));
            JSONObject jsonObject=new JSONObject(json);



            if(globalProvider.me!=null&&globalProvider.me._customerProfile!=null&&globalProvider.me._customerProfile.get_id()!=null){
                String url= Constants.customerUrlStr+"/"+globalProvider.me._customerProfile.get_id();
                JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(NewBillingAddressActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewBillingAddressActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers = globalProvider.addHeaderToken(NewBillingAddressActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }
                };
                globalProvider.addRequest(jsonArrayRequest);
               /* Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(NewBillingAddressActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewBillingAddressActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers = globalProvider.addHeaderToken(NewBillingAddressActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }

                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> param=new HashMap<>();
                        StringBuilder sb = new StringBuilder();
                        for (String s : list){
                            sb.append(s);
                            sb.append("\t");
                        }
                        param.put("billingAddresses", sb.toString());

                        return param;

                    }
                };
                globalProvider.addRequest(utf8JsonRequest);
                */
                /*
                globalProvider.put(this,url, entity, "application/json", new RequestListener() {
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(NewBillingAddressActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(NewBillingAddressActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();

                    }

                    public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                    }
                });
                */
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
