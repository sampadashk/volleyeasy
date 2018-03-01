package com.samiapps.kv.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

/**
 * Created by KV on 21/2/18.
 */

public class CompanyInfoActivity extends BaseActivity{
    public TextView turn_text;
    public EditText name;
    public EditText contactPerson;
    public EditText contactNumber;
    public TextView edit;
    GlobalProvider globalProvider;
    public Me me;
    public LinearLayout back;
    public boolean isEditable=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalProvider=GlobalProvider.getInstance(getApplicationContext());
        me=globalProvider.me;

        setContentView(R.layout.activity_company_info);
        turn_text= (TextView) findViewById(R.id.turn_text);
        name= (EditText) findViewById(R.id.name);
        contactPerson= (EditText) findViewById(R.id.contactPerson);
        contactNumber= (EditText) findViewById(R.id.contactNumber);
        edit= (TextView) findViewById(R.id.edit);
        edit.setText(getString(R.string.edit));
        name.setEnabled(false);
        contactNumber.setEnabled(false);
        contactPerson.setEnabled(false);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditable) {
                    edit.setText(getString(R.string.finish));
                    name.setEnabled(true);
                    contactPerson.setEnabled(true);
                    contactNumber.setEnabled(true);
                } else {
                    edit.setText(getString(R.string.edit));
                    name.setEnabled(false);
                    contactPerson.setEnabled(false);
                    contactNumber.setEnabled(false);
                    resetInfo();
                }
                isEditable=!isEditable;
            }
        });
        back = (LinearLayout) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(me!=null){
            if(me.name!=null){
                turn_text.setText(me.name);
                name.setText(me.name);
            }
            if(me._customerProfile!=null){
                if(me._customerProfile.contactNumber!=null){
                    contactNumber.setText(me._customerProfile.contactNumber);
                }
                if(me._customerProfile.getContactPerson()!=null){
                    contactPerson.setText(me._customerProfile.getContactPerson());
                }
            }
        }
    }

    public void resetInfo(){
        ReSetComInfo body=new ReSetComInfo();
        body.setName(name.getText().toString());
        body.setContactNumber(contactNumber.getText().toString());
        body.setContactPerson(contactPerson.getText().toString());

        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter ow = objectMapper.writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(body);
            ByteArrayEntity entity = new ByteArrayEntity(json.getBytes("UTF-8"));

            if (globalProvider.me != null && globalProvider.me._customerProfile != null && globalProvider.me._customerProfile.get_id() != null) {
                String url = Constants.customerUrlStr + "/" + globalProvider.me._customerProfile.get_id();


                JSONObject jsonObject=new JSONObject(json);

              //TODO DO COMPANY

                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.PUT, url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(CompanyInfoActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Toast.makeText(CompanyInfoActivity.this, error.toString(), Toast.LENGTH_SHORT).show();


                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/json");
                        String token = Constants.getToken(CompanyInfoActivity.this);
                        Log.d("gettoken", token);
                        headers.put("Authorization",token);
                        return headers;
                    }
                };
                globalProvider.addRequest(jsonObjectRequest);

                /*globalProvider.put(this,url, entity, "application/json", new RequestListener() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(CompanyInfoActivity.this, getString(R.string.suctoupdate), Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(CompanyInfoActivity.this, new String(responseBody), Toast.LENGTH_SHORT).show();
//                    new AlertDialog.Builder(PeisongActivity.this)
//                            .setMessage("修改失败！请检查输入是否有误！")
//                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//
//                                }
//                            }).show();

                    }

                    @Override
                    public void onPostProcessResponse(ResponseHandlerInterface instance, HttpResponse response) {
                    }
                });}
                */
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

