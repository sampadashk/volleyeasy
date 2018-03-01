package com.samiapps.kv.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
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

public class EditBillingAddressTActivity extends BaseActivity implements View.OnClickListener{
    public RelativeLayout addAddress;
    public LinearLayout turn_left;
    public ListView lv;
    public TextView title;
    GlobalProvider globalProvider;
    public ChooseBillingAddressTAdapter adapter;
    public List<String> address;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        globalProvider=GlobalProvider.getInstance(EditBillingAddressTActivity.this);
        init();
        initAction();
        loadAddress();
    }

    private void loadAddress() {
        address = globalProvider.me._customerProfile.billingAddresses;
        Log.d("cks",address.size()+"");
        for(int i=0;i<address.size();i++)

        Log.d("ckad",address.get(i));
        if(address.size() == 0 && !globalProvider.me._customerProfile.billingAddress.equals("")){
            address.add(globalProvider.me._customerProfile.billingAddress);
            doReset(address);
        }
        adapter=new ChooseBillingAddressTAdapter(this,address);
        lv.setAdapter(adapter);
    }

    private void init() {
        turn_left = (LinearLayout) findViewById(R.id.turn_left);
        lv = (ListView) findViewById(R.id.lv);
        addAddress = (RelativeLayout) findViewById(R.id.addAddress);
        title = (TextView) findViewById(R.id.title);
        address = new ArrayList<String>();
    }

    private void initAction() {
        turn_left.setOnClickListener(this);
        addAddress.setOnClickListener(this);
        title.setText(R.string.billingAddress);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.turn_left:
                setResult(Activity.RESULT_OK);
                finish();
                break;
            case R.id.addAddress:


                Intent intent = new Intent(getApplication(),NewBillingAddressActivity.class);
                startActivityForResult(intent, Constants.SHIPPINGADDRESS);
                break;

        }
    }

    public void doReset(final String w){
        Log.d("checkadd","dorest called");
        for(int i = 0;i<globalProvider.me._customerProfile.billingAddresses.size();i++){
            if(w.equals(globalProvider.me._customerProfile.billingAddresses.get(i))){
                globalProvider.me._customerProfile.billingAddresses.remove(i);
            }
        }
        BillingAddress body=new BillingAddress();
        body.setBillingAddresses(globalProvider.me._customerProfile.billingAddresses);

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(body);
            Log.d("checkjson", json);
            ByteArrayEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));


            if (globalProvider.me != null && globalProvider.me._customerProfile != null && globalProvider.me._customerProfile.get_id() != null) {
                String url = Constants.customerUrlStr + "/" + globalProvider.me._customerProfile.get_id();
                JSONObject jsonObject=new JSONObject(json);


                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        loadMe();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })   {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        String token = Constants.getToken(EditBillingAddressTActivity.this);
                        Log.d("gettoken", token);
                        headers.put("Authorization",token);
                        return headers;
                    }
                };
                globalProvider.addRequest(jsonObjectRequest);

              /*  Utf8JsonRequest utf8JsonRequest = new Utf8JsonRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        loadMe();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers = globalProvider.addHeaderToken(EditBillingAddressTActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }

                    @Override
                    public Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("billingAddresses", w);
                        return param;

                    }

                };
                globalProvider.addRequest(utf8JsonRequest);

*/
            }

        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void doChange(String w,final String y){
        for(int i = 0;i<globalProvider.me._customerProfile.billingAddresses.size();i++){
            if(w.equals(globalProvider.me._customerProfile.billingAddresses.get(i))){
                globalProvider.me._customerProfile.billingAddresses.remove(i);
            }
        }
        globalProvider.me._customerProfile.billingAddresses.add(y);
        BillingAddress body=new BillingAddress();
        body.setBillingAddresses(globalProvider.me._customerProfile.billingAddresses);

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(body);
            Log.d("checkchange",json);
            ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));

            if(globalProvider.me!=null&&globalProvider.me._customerProfile!=null&&globalProvider.me._customerProfile.get_id()!=null){
                String url= Constants.customerUrlStr+"/"+globalProvider.me._customerProfile.get_id();
                JSONObject jsonObject=new JSONObject(json);

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        loadMe();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditBillingAddressTActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
                {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers=globalProvider.addHeaderToken(EditBillingAddressTActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }




                };
                globalProvider.addRequest(jsonObjectRequest);

               /* Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        loadMe();

                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EditBillingAddressTActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                            }
                        })
                {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers=globalProvider.addHeaderToken(EditBillingAddressTActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }
                    @Override
                    public Map<String,String> getParams() throws AuthFailureError
                    {
                        Map<String,String> param=new HashMap<>();
                        param.put("billingAddresses",y);
                        return param;

                    }




                };
                globalProvider.addRequest(utf8JsonRequest);
                */
                /*

                globalProvider.put(this,url, entity, "application/json", new RequestListener() {
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        loadMe();
                    }
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(EditBillingAddressTActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();

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

    public void loadMe(){

        Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, Constants.meUrlStr, new Response.Listener<String>() {
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
                Map<String, String> headers = new HashMap<>();
                headers=globalProvider.addHeaderToken(EditBillingAddressTActivity.this);


                //headers=globalProvider.addHeaderToken(getActivity());
                return headers;
            }




        };
        globalProvider.addRequest(utf8JsonRequest);


    }
    public void parseMe(String json) {
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonParser jsonParser = jsonFactory.createParser(json);
            Me me = (Me) objectMapper.readValue(jsonParser, Me.class);
            globalProvider.me=me;
            address.clear();
            address.addAll(me._customerProfile.billingAddresses);
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doResetAddress(final String y){
        OldBillingAddress body=new OldBillingAddress();
        body.setBillingAddress(y);

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(body);
            ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));
            Log.d("chkjsoo",json);
            Log.d("checka",y);
            boolean vl=globalProvider.me._customerProfile==null;
            Log.d("checknull",vl+" ");
            JSONObject jsobj=new JSONObject(json);

            if(globalProvider.me!=null&&globalProvider.me._customerProfile!=null&&globalProvider.me._customerProfile.get_id()!=null){
                String url= Constants.customerUrlStr+"/"+globalProvider.me._customerProfile.get_id();
                Log.d("checkru",url);
               JsonObjectRequest objreq=new JsonObjectRequest(Request.Method.PUT, url, jsobj,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("addressupdatereport","success");
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("addressupdateerror",error.toString());
                        Toast.makeText(EditBillingAddressTActivity.this, error.toString(), Toast.LENGTH_SHORT).show();


                    }
                }

                )
                {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers=globalProvider.addHeaderToken(EditBillingAddressTActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }





                };
                globalProvider.addRequest(objreq);
                /*
                globalProvider.put(this,url, entity, "application/json", new RequestListener() {
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
//                        setResult(RESULT_OK);
//                        finish();
                    }
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(EditBillingAddressTActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();

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


    public void doReset(final List<String> list){
        Log.d("checkreset","here");
        BillingAddress body=new BillingAddress();
        body.setBillingAddresses(list);
        Log.d("checklist",list.get(0)+" "+list.get(1));

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(body);
            ByteArrayEntity entity= new ByteArrayEntity(json.getBytes("UTF-8"));
            Log.d("checkjs",json);





            if(globalProvider.me!=null&&globalProvider.me._customerProfile!=null&&globalProvider.me._customerProfile.get_id()!=null){
                String url= Constants.customerUrlStr+"/"+globalProvider.me._customerProfile.get_id();
                JSONObject jsonObject=new JSONObject(json);

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditBillingAddressTActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                    }
                })
                {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers=globalProvider.addHeaderToken(EditBillingAddressTActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }





                };

/*
                Utf8JsonRequest utf8JsonRequest=new Utf8JsonRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditBillingAddressTActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<>();
                        headers=globalProvider.addHeaderToken(EditBillingAddressTActivity.this);


                        //headers=globalProvider.addHeaderToken(getActivity());
                        return headers;
                    }
                    @Override
                    public Map<String,String> getParams() throws AuthFailureError
                    {
                        Map<String,String> param=new HashMap<>();
                        StringBuilder sb = new StringBuilder();
                        for (String s : list){
                            sb.append(s);
                            sb.append("\t");
                        }
                        param.put("billingAddresses", sb.toString());

                        return param;


                    }
                }

                        ;
                        */
                globalProvider.addRequest(jsonObjectRequest);

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.SHIPPINGADDRESS:
                loadMe();
                break;
        }
    }
}